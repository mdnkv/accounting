package dev.mednikov.accounting.currencies.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.currencies.dto.CurrencyDto;
import dev.mednikov.accounting.currencies.dto.CurrencyDtoMapper;
import dev.mednikov.accounting.currencies.exceptions.CurrencyAlreadyExistsException;
import dev.mednikov.accounting.currencies.exceptions.CurrencyNotFoundException;
import dev.mednikov.accounting.currencies.exceptions.CurrencyDeletionException;
import dev.mednikov.accounting.currencies.models.Currency;
import dev.mednikov.accounting.currencies.repositories.CurrencyRepository;
import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.organizations.repositories.OrganizationRepository;

import dev.mednikov.accounting.transactions.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();
    private final static CurrencyDtoMapper currencyDtoMapper = new CurrencyDtoMapper();

    private final TransactionRepository transactionRepository;
    private final CurrencyRepository currencyRepository;
    private final OrganizationRepository organizationRepository;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository, OrganizationRepository organizationRepository, TransactionRepository transactionRepository) {
        this.currencyRepository = currencyRepository;
        this.organizationRepository = organizationRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public CurrencyDto createCurrency(CurrencyDto currencyDto) {
        Long organizationId = Long.parseLong(currencyDto.getOrganizationId());
        String code = currencyDto.getCode().toUpperCase();
        if (this.currencyRepository.findByCodeAndOrganizationId(code, organizationId).isPresent()) {
            throw new CurrencyAlreadyExistsException();
        }
        Organization organization = this.organizationRepository.getReferenceById(organizationId);

        // If the currency is first for this organization, then make it primary by default
        boolean primary = this.currencyRepository.findAllByOrganizationId(organizationId).isEmpty();
        Currency currency = new Currency();
        currency.setCode(code);
        currency.setOrganization(organization);
        currency.setName(currencyDto.getName());
        currency.setId(snowflakeGenerator.next());
        currency.setPrimary(primary);
        currency.setDeprecated(false);

        Currency result = this.currencyRepository.save(currency);
        return currencyDtoMapper.apply(result);
    }

    @Override
    public CurrencyDto updateCurrency(CurrencyDto currencyDto) {
        Objects.requireNonNull(currencyDto.getId());
        Long currencyId = Long.parseLong(currencyDto.getId());
        Long organizationId = Long.parseLong(currencyDto.getOrganizationId());
        Currency currency = this.currencyRepository.findById(currencyId).orElseThrow(CurrencyNotFoundException::new);
        if (!currency.getCode().equals(currencyDto.getCode())) {
            // validate that the currency does not exist yet
            if (this.currencyRepository.findByCodeAndOrganizationId(currencyDto.getCode(), organizationId).isPresent()) {
                throw new CurrencyAlreadyExistsException();
            }
        }
        currency.setName(currencyDto.getName());
        currency.setCode(currencyDto.getCode());
        currency.setDeprecated(currencyDto.isDeprecated());

        if (!currency.isPrimary() && currencyDto.isPrimary()) {
            // update currency to primary
            currency.setPrimary(true);

            // Update current primary currency
            Optional<Currency> currentPrimary = this.currencyRepository.findPrimaryCurrency(organizationId);
            if (currentPrimary.isPresent()) {
                Currency cp = currentPrimary.get();
                if (!cp.getId().equals(currencyId)) {
                    cp.setPrimary(false);
                    this.currencyRepository.save(cp);
                }
            }
        }

        Currency result = this.currencyRepository.save(currency);
        return currencyDtoMapper.apply(result);
    }

    @Override
    public Optional<CurrencyDto> getPrimaryCurrency(Long organizationId) {
        return this.currencyRepository.findPrimaryCurrency(organizationId).map(currencyDtoMapper);
    }

    @Override
    public List<CurrencyDto> getCurrencies(Long organizationId) {
        return this.currencyRepository
                .findAllByOrganizationId(organizationId)
                .stream()
                .map(currencyDtoMapper)
                .toList();
    }

    @Override
    public void deleteCurrency(Long id) {
        Optional<Currency> result = this.currencyRepository.findById(id);
        if (result.isPresent()) {
            Currency currency = result.get();
            // check that currency is not primary
            if (currency.isPrimary()) {
                throw new CurrencyDeletionException();
            } else {
               // check that currency is not associated with transactions
                if (this.transactionRepository.countByCurrencyId(currency.getId()) > 0) {
                    throw new CurrencyDeletionException();
                } else {
                    this.currencyRepository.delete(currency);
                }
            }
        }
    }
}
