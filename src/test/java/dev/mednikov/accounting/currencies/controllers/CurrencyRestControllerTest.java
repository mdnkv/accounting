package dev.mednikov.accounting.currencies.controllers;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mednikov.accounting.currencies.dto.CurrencyDto;
import dev.mednikov.accounting.currencies.exceptions.CurrencyAlreadyExistsException;
import dev.mednikov.accounting.currencies.services.CurrencyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(CurrencyRestController.class)
class CurrencyRestControllerTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mvc;

    @MockitoBean private CurrencyService currencyService;

    @Test
    void createCurrency_alreadyExistsTest() throws Exception {
        CurrencyDto payload = new CurrencyDto();
        payload.setCode("EUR");
        payload.setName("Euro");
        payload.setOrganizationId(snowflakeGenerator.next().toString());

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(currencyService.createCurrency(Mockito.any())).thenThrow(CurrencyAlreadyExistsException.class);
        mvc.perform(post("/api/currencies/create")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCurrency_successTest() throws Exception {
        String organizationId = snowflakeGenerator.next().toString();
        CurrencyDto payload = new CurrencyDto();
        payload.setCode("EUR");
        payload.setName("Euro");
        payload.setOrganizationId(organizationId);

        CurrencyDto result = new CurrencyDto();
        payload.setCode("EUR");
        payload.setName("Euro");
        payload.setOrganizationId(organizationId);
        payload.setPrimary(true);
        payload.setId(snowflakeGenerator.next().toString());

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(currencyService.createCurrency(Mockito.any())).thenReturn(result);
        mvc.perform(post("/api/currencies/create")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    void updateCurrency_alreadyExistsTest() throws Exception {
        CurrencyDto payload = new CurrencyDto();
        payload.setCode("EUR");
        payload.setName("Euro");
        payload.setOrganizationId(snowflakeGenerator.next().toString());
        payload.setId(snowflakeGenerator.next().toString());
        payload.setPrimary(true);

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(currencyService.updateCurrency(Mockito.any())).thenThrow(CurrencyAlreadyExistsException.class);
        mvc.perform(put("/api/currencies/update")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCurrency_successTest() throws Exception {
        CurrencyDto payload = new CurrencyDto();
        payload.setCode("EUR");
        payload.setName("Euro");
        payload.setOrganizationId(snowflakeGenerator.next().toString());
        payload.setId(snowflakeGenerator.next().toString());
        payload.setPrimary(true);

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(currencyService.updateCurrency(Mockito.any())).thenReturn(payload);
        mvc.perform(put("/api/currencies/update")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void getPrimaryCurrency_existsTest() throws Exception {
        Long organizationId = snowflakeGenerator.next();
        CurrencyDto payload = new CurrencyDto();
        payload.setCode("EUR");
        payload.setName("Euro");
        payload.setOrganizationId(organizationId.toString());
        payload.setId(snowflakeGenerator.next().toString());
        payload.setPrimary(true);

        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(currencyService.getPrimaryCurrency(organizationId)).thenReturn(Optional.of(payload));
        mvc.perform(get("/api/currencies/primary/{organizationId}", organizationId)
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void getPrimaryCurrency_doesNotExistTest() throws Exception {
        Long organizationId = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(currencyService.getPrimaryCurrency(organizationId)).thenReturn(Optional.empty());
        mvc.perform(get("/api/currencies/primary/{organizationId}", organizationId)
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCurrenciesTest() throws Exception {
        Long organizationId = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(currencyService.getCurrencies(organizationId)).thenReturn(List.of());
        mvc.perform(get("/api/currencies/organization/{organizationId}", organizationId)
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
