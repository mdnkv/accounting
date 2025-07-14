package dev.mednikov.accounting.accounts.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mednikov.accounting.accounts.models.Account;
import dev.mednikov.accounting.accounts.repositories.AccountRepository;
import dev.mednikov.accounting.organizations.events.OrganizationCreatedEvent;
import dev.mednikov.accounting.organizations.models.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountBootstrapService {

    private final static Logger logger = LoggerFactory.getLogger(AccountBootstrapService.class);
    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    private final AccountRepository accountRepository;
    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;

    public AccountBootstrapService(
            AccountRepository accountRepository,
            ObjectMapper objectMapper,
            ResourceLoader resourceLoader)
    {
        this.accountRepository = accountRepository;
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
    }

    @EventListener
    public void onOrganizationCreatedEventListener(OrganizationCreatedEvent e){
        final Organization organization = e.getOrganization();

        try {
            // read chart of accounts template from resources
            final Resource resource = this.resourceLoader.getResource("classpath:bootstrap/accounts.json");

            // parse
            final TypeReference<List<Account>> typeReference = new TypeReference<>() {};
            final List<Account> chartOfAccounts = this.objectMapper.readValue(resource.getInputStream(), typeReference);

            // prepare accounts for organization
            final List<Account> organizationAccounts = chartOfAccounts
                    .stream()
                    .map(result -> {
                        Account account = new Account();
                        account.setOrganization(organization);
                        account.setCode(result.getCode());
                        account.setName(result.getName());
                        account.setAccountType(result.getAccountType());
                        account.setId(snowflakeGenerator.next());
                        account.setDeprecated(false);
                        return account;
                    })
                    .toList();

            // save results
            accountRepository.saveAll(organizationAccounts);
        } catch (Exception ex){
            logger.error(ex.getMessage());
        }
    }
}
