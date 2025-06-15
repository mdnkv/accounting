package dev.mednikov.accounting.transactions.controllers;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mednikov.accounting.transactions.dto.TransactionDto;
import dev.mednikov.accounting.transactions.dto.TransactionLineDto;
import dev.mednikov.accounting.transactions.exceptions.UnbalancedTransactionException;
import dev.mednikov.accounting.transactions.services.TransactionService;
import dev.mednikov.accounting.users.models.User;
import dev.mednikov.accounting.users.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionRestController.class)
class TransactionRestControllerTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @MockitoBean private TransactionService transactionService;
    @MockitoBean private UserService userService;
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void createTransaction_notBalancedTest() throws Exception {
        List<TransactionLineDto> lines = new ArrayList<>();
        TransactionLineDto line1 = new TransactionLineDto();
        line1.setAccountId(snowflakeGenerator.next().toString());
        line1.setCreditAmount(BigDecimal.valueOf(500.00));
        line1.setDebitAmount(BigDecimal.ZERO);
        lines.add(line1);

        TransactionLineDto line2 = new TransactionLineDto();
        line2.setAccountId(snowflakeGenerator.next().toString());
        line2.setCreditAmount(BigDecimal.ZERO);
        line2.setDebitAmount(BigDecimal.valueOf(100.00));
        lines.add(line2);

        TransactionDto payload = new TransactionDto();
        payload.setCurrency("EUR");
        payload.setDate(LocalDate.now().minusDays(10));
        payload.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
        payload.setLines(lines);
        payload.setOrganizationId(snowflakeGenerator.next().toString());

        String keycloakId = UUID.randomUUID().toString();
        String body = objectMapper.writeValueAsString(payload);

        User user = new User();
        user.setKeycloakId(keycloakId);
        user.setId(snowflakeGenerator.next());
        user.setEmail("hqd7o71xqwyx@googlemail.com");
        user.setFirstName("Margaretha");
        user.setLastName("Scherer-Neuhaus");

        Mockito.when(userService.getOrCreateUser(Mockito.any())).thenReturn(user);
        Mockito.when(transactionService.createTransaction(Mockito.any(), Mockito.any())).thenThrow(UnbalancedTransactionException.class);

        mockMvc.perform(post("/api/transactions/create")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

    }

    @Test
    void createTransaction_successTest() throws Exception {
        List<TransactionLineDto> lines = new ArrayList<>();
        TransactionLineDto line1 = new TransactionLineDto();
        line1.setAccountId(snowflakeGenerator.next().toString());
        line1.setCreditAmount(BigDecimal.valueOf(500.00));
        line1.setDebitAmount(BigDecimal.ZERO);
        lines.add(line1);

        TransactionLineDto line2 = new TransactionLineDto();
        line2.setAccountId(snowflakeGenerator.next().toString());
        line2.setCreditAmount(BigDecimal.ZERO);
        line2.setDebitAmount(BigDecimal.valueOf(500.00));
        lines.add(line2);

        TransactionDto payload = new TransactionDto();
        payload.setId(snowflakeGenerator.next().toString());
        payload.setCurrency("EUR");
        payload.setDate(LocalDate.now().minusDays(10));
        payload.setDescription("Pellentesque condimentum magna at iaculis consequat.");
        payload.setLines(lines);
        payload.setOrganizationId(snowflakeGenerator.next().toString());

        String keycloakId = UUID.randomUUID().toString();
        String body = objectMapper.writeValueAsString(payload);

        User user = new User();
        user.setKeycloakId(keycloakId);
        user.setId(snowflakeGenerator.next());
        user.setEmail("mbwq6d141hsn6mg5rbqh@ymail.com");
        user.setFirstName("Tanja");
        user.setLastName("Krüger");

        Mockito.when(userService.getOrCreateUser(Mockito.any())).thenReturn(user);

        Mockito.when(transactionService.createTransaction(Mockito.any(), Mockito.any())).thenReturn(payload);

        mockMvc.perform(post("/api/transactions/create")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    void getTransactionsTest() throws Exception {
        Long organizationId = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(transactionService.getTransactions(organizationId)).thenReturn(List.of());

        mockMvc.perform(get("/api/transactions/organization/{organizationId}", organizationId)
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId))))
                .andExpect(status().isOk());
    }

}
