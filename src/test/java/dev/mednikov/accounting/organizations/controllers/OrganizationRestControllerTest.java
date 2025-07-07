package dev.mednikov.accounting.organizations.controllers;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mednikov.accounting.organizations.dto.OrganizationDto;
import dev.mednikov.accounting.organizations.services.OrganizationService;
import dev.mednikov.accounting.users.models.User;
import dev.mednikov.accounting.users.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(OrganizationRestController.class)
class OrganizationRestControllerTest {

    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mvc;
    @MockitoBean private OrganizationService organizationService;
    @MockitoBean private UserService userService;

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Test
    void createOrganizationTest() throws Exception {
        // Create payload
        OrganizationDto payload = new OrganizationDto();
        payload.setName("Kern Urban GmbH & Co. KGaA");
        payload.setCurrency("EUR");

        // Create mock response
        String id = snowflakeGenerator.next().toString();
        OrganizationDto result = new OrganizationDto();
        result.setId(id);
        result.setName("Kern Urban GmbH & Co. KGaA");
        result.setCurrency("EUR");

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        User user = new User();
        user.setEmail("ilbxgj62taf@yahoo.com");
        user.setKeycloakId(keycloakId);
        user.setFirstName("Gertraude");
        user.setLastName("Schüler");

        Mockito.when(userService.getOrCreateUser(Mockito.any())).thenReturn(user);
        Mockito.when(organizationService.createOrganization(Mockito.any(), Mockito.any())).thenReturn(result);

        mvc.perform(post("/api/organizations/create")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Kern Urban GmbH & Co. KGaA"));
    }

    @Test
    void updateOrganizationTest() throws Exception {
        String id = snowflakeGenerator.next().toString();
        // Create payload
        OrganizationDto payload = new OrganizationDto();
        payload.setId(id);
        payload.setCurrency("EUR");
        payload.setName("Hartung Hoppe AG");

        String body = objectMapper.writeValueAsString(payload);

        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(organizationService.updateOrganization(Mockito.any(OrganizationDto.class))).thenReturn(payload);
        mvc.perform(put("/api/organizations/update")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Hartung Hoppe AG"))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void deleteOrganizationTest() throws Exception {
        String keycloakId = UUID.randomUUID().toString();
        String id = snowflakeGenerator.next().toString();
        mvc.perform(delete("/api/organizations/delete/{id}", id).with(jwt().jwt(jwt -> jwt
                .claim("sub", keycloakId)))).andExpect(status().isNoContent());
    }

    @Test
    void getOrganizationById_existsTest() throws Exception {
        Long id = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();
        // Create mock response
        OrganizationDto result = new OrganizationDto();
        result.setId(id.toString());
        result.setName("Gärtner GmbH & Co. KG");
        result.setCurrency("EUR");
        Mockito.when(organizationService.getOrganization(id)).thenReturn(Optional.of(result));
        mvc.perform(get("/api/organizations/organization/{id}", id)
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Gärtner GmbH & Co. KG"))
                .andExpect(jsonPath("$.currency").value("EUR"));

    }

    @Test
    void getOrganizationById_doesNotExistTest() throws Exception {
        String id = snowflakeGenerator.next().toString();
        String keycloakId = UUID.randomUUID().toString();
        Mockito.when(organizationService.getOrganization(Long.valueOf(id))).thenReturn(Optional.empty());
        mvc.perform(get("/api/organizations/organization/{id}", id)
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId))))
                .andExpect(status().isNotFound());
    }

}
