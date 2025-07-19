package dev.mednikov.accounting.shared.bootstrap;

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

import java.util.ArrayList;
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
        Organization organization = e.getOrganization();
        try {
            Resource resource = this.resourceLoader.getResource("classpath:bootstrap/accounts.json");
            TypeReference<List<Account>> typeReference = new TypeReference<>() {};
            List<Account> data = this.objectMapper.readValue(resource.getInputStream(), typeReference);
            List<Account> accounts = new ArrayList<>();
            for (Account item : data) {
                Account account = new Account();
                account.setOrganization(organization);
                account.setCode(item.getCode());
                account.setName(item.getName());
                account.setAccountType(item.getAccountType());
                account.setId(snowflakeGenerator.next());
                account.setDeprecated(false);
                accounts.add(account);
            }
            accountRepository.saveAll(accounts);
        } catch (Exception ex){
            logger.error(ex.getMessage());
        }
    }
}
