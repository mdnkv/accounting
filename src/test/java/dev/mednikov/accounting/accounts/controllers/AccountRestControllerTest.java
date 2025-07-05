package dev.mednikov.accounting.accounts.controllers;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mednikov.accounting.accounts.dto.AccountDto;
import dev.mednikov.accounting.accounts.exceptions.AccountAlreadyExistsException;
import dev.mednikov.accounting.accounts.exceptions.AccountNotFoundException;
import dev.mednikov.accounting.accounts.models.AccountType;
import dev.mednikov.accounting.accounts.services.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountRestController.class)
class AccountRestControllerTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private AccountService accountService;

    @Test
    void createAccount_alreadyExistsTest() throws Exception {
        AccountDto payload = new AccountDto();
        payload.setOrganizationId(snowflakeGenerator.next().toString());
        payload.setAccountType(AccountType.ASSET);
        payload.setCode("10100");
        payload.setName("Cash – Regular Checking");

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(accountService.createAccount(Mockito.any())).thenThrow(AccountAlreadyExistsException.class);

        mvc.perform(post("/api/accounts/create")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAccount_successTest() throws Exception {
        AccountDto payload = new AccountDto();
        payload.setOrganizationId(snowflakeGenerator.next().toString());
        payload.setAccountType(AccountType.ASSET);
        payload.setCode("12100");
        payload.setName("Accounts Receivable");

        AccountDto result = new AccountDto();
        result.setId(snowflakeGenerator.next().toString());
        result.setOrganizationId(snowflakeGenerator.next().toString());
        result.setAccountType(AccountType.ASSET);
        result.setCode("12100");
        result.setName("Accounts Receivable");

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(accountService.createAccount(Mockito.any())).thenReturn(result);

        mvc.perform(post("/api/accounts/create")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Accounts Receivable"))
                .andExpect(jsonPath("$.organizationId").exists())
                .andExpect(jsonPath("$.accountType").value("ASSET"))
                .andExpect(jsonPath("$.code").value("12100"));
    }

    @Test
    void updateAccount_notFoundTest() throws Exception {
        AccountDto payload = new AccountDto();
        payload.setId(snowflakeGenerator.next().toString());
        payload.setOrganizationId(snowflakeGenerator.next().toString());
        payload.setAccountType(AccountType.LIABILITY);
        payload.setCode("21000");
        payload.setName("Accounts Payable");

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(accountService.updateAccount(Mockito.any())).thenThrow(AccountNotFoundException.class);
        mvc.perform(put("/api/accounts/update")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAccount_codeAlreadyExistsTest() throws Exception {
        AccountDto payload = new AccountDto();
        payload.setId(snowflakeGenerator.next().toString());
        payload.setOrganizationId(snowflakeGenerator.next().toString());
        payload.setAccountType(AccountType.LIABILITY);
        payload.setCode("21000");
        payload.setName("Accounts Payable");

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(accountService.updateAccount(Mockito.any())).thenThrow(AccountAlreadyExistsException.class);
        mvc.perform(put("/api/accounts/update")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAccount_successTest() throws Exception {
        AccountDto payload = new AccountDto();
        payload.setId(snowflakeGenerator.next().toString());
        payload.setOrganizationId(snowflakeGenerator.next().toString());
        payload.setAccountType(AccountType.LIABILITY);
        payload.setCode("21000");
        payload.setName("Accounts Payable");

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(accountService.updateAccount(Mockito.any())).thenReturn(payload);
        mvc.perform(put("/api/accounts/update")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAccountTest() throws Exception {
        Long id = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();
        Mockito.doNothing().when(accountService).deleteAccount(id);
        mvc.perform(delete("/api/accounts/delete/{id}", id)
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId))))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAccountsTest() throws Exception {
        Long organizationId = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();
        Mockito.when(accountService.getAccounts(organizationId)).thenReturn(List.of());
        mvc.perform(get("/api/accounts/organization/{id}", organizationId)
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId))))
                .andExpect(status().isOk());
    }

    @Test
    void getAccount_existsTest() throws Exception {
        Long accountId = snowflakeGenerator.next();
        AccountDto payload = new AccountDto();
        payload.setId(snowflakeGenerator.next().toString());
        payload.setOrganizationId(snowflakeGenerator.next().toString());
        payload.setAccountType(AccountType.LIABILITY);
        payload.setCode("21000");
        payload.setName("Accounts Payable");
        String keycloakId = UUID.randomUUID().toString();
        Mockito.when(accountService.getAccount(accountId)).thenReturn(Optional.of(payload));
        mvc.perform(get("/api/accounts/account/{id}", accountId)
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId))))
                .andExpect(status().isOk());
    }

    @Test
    void getAccount_doesNotTest() throws Exception {
        Long accountId = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();
        Mockito.when(accountService.getAccount(accountId)).thenReturn(Optional.empty());
        mvc.perform(get("/api/accounts/account/{id}", accountId)
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId))))
                .andExpect(status().isNotFound());
    }

}
