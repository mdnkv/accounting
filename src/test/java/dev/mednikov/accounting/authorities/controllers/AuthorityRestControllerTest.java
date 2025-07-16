package dev.mednikov.accounting.authorities.controllers;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mednikov.accounting.authorities.dto.AuthorityDto;
import dev.mednikov.accounting.authorities.exceptions.AuthorityAlreadyExistsException;
import dev.mednikov.accounting.authorities.exceptions.AuthorityNotFoundException;
import dev.mednikov.accounting.authorities.services.AuthorityService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(AuthorityRestController.class)
class AuthorityRestControllerTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @MockitoBean private AuthorityService authorityService;
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void createAuthority_alreadyExistsTest() throws Exception {
        Long organizationId = snowflakeGenerator.next();
        AuthorityDto payload = new AuthorityDto();
        payload.setOrganizationId(organizationId.toString());
        payload.setName("transactions:create");

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(authorityService.createAuthority(Mockito.any())).thenThrow(AuthorityAlreadyExistsException.class);
        mockMvc.perform(post("/api/authorities/create")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAuthority_successTest() throws Exception {
        Long organizationId = snowflakeGenerator.next();
        AuthorityDto payload = new AuthorityDto();
        payload.setOrganizationId(organizationId.toString());
        payload.setName("transactions:create");

        AuthorityDto result = new AuthorityDto();
        result.setOrganizationId(organizationId.toString());
        result.setName("transactions:create");
        result.setId(snowflakeGenerator.next().toString());

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(authorityService.createAuthority(Mockito.any())).thenReturn(result);

        mockMvc.perform(post("/api/authorities/create")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    void updateAuthority_successTest() throws Exception {
        Long authorityId = snowflakeGenerator.next();
        Long organizationId = snowflakeGenerator.next();
        AuthorityDto payload = new AuthorityDto();
        payload.setOrganizationId(organizationId.toString());
        payload.setName("transactions:create");
        payload.setId(authorityId.toString());

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(authorityService.updateAuthority(Mockito.any())).thenReturn(payload);
        mockMvc.perform(put("/api/authorities/update")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void updateAuthority_notFoundTest() throws Exception {
        Long authorityId = snowflakeGenerator.next();
        Long organizationId = snowflakeGenerator.next();
        AuthorityDto payload = new AuthorityDto();
        payload.setOrganizationId(organizationId.toString());
        payload.setName("transactions:create");
        payload.setId(authorityId.toString());

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(authorityService.updateAuthority(Mockito.any())).thenThrow(AuthorityNotFoundException.class);
        mockMvc.perform(put("/api/authorities/update")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAuthority_alreadyExistsTest() throws Exception {
        Long authorityId = snowflakeGenerator.next();
        Long organizationId = snowflakeGenerator.next();
        AuthorityDto payload = new AuthorityDto();
        payload.setOrganizationId(organizationId.toString());
        payload.setName("transactions:create");
        payload.setId(authorityId.toString());

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(authorityService.updateAuthority(Mockito.any())).thenThrow(AuthorityAlreadyExistsException.class);
        mockMvc.perform(put("/api/authorities/update")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteAuthority_successTest() throws Exception {
        Long authorityId = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();
        Mockito.doNothing().when(authorityService).deleteAuthority(authorityId);
        mockMvc.perform(delete("/api/authorities/delete/{id}", authorityId)
                        .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))).andExpect(status().isNoContent());
    }

    @Test
    void getAuthoritiesTest() throws Exception {
        Long organizationId = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();
        Mockito.when(authorityService.getAuthorities(organizationId)).thenReturn(List.of());
        mockMvc.perform(get("/api/authorities/organization/{id}", organizationId)
                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))).andExpect(status().isOk());
    }

}
