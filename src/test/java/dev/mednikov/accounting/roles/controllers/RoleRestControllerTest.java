package dev.mednikov.accounting.roles.controllers;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mednikov.accounting.authorities.dto.AuthorityDto;
import dev.mednikov.accounting.roles.dto.RoleDto;
import dev.mednikov.accounting.roles.exceptions.RoleAlreadyExistsException;
import dev.mednikov.accounting.roles.exceptions.RoleNotFoundException;
import dev.mednikov.accounting.roles.services.RoleService;
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
@WebMvcTest(RoleRestController.class)
class RoleRestControllerTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private RoleService roleService;

    @Test
    void createRole_alreadyExistsTest() throws Exception {
        Long organizationId = snowflakeGenerator.next();
        RoleDto payload = new RoleDto();
        payload.setName("User");
        payload.setAuthorities(List.of());
        payload.setOrganizationId(organizationId.toString());

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(roleService.createRole(Mockito.any())).thenThrow(RoleAlreadyExistsException.class);
        mockMvc.perform(post("/api/roles/create")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRole_successTest() throws Exception {
        Long organizationId = snowflakeGenerator.next();

        AuthorityDto authority = new AuthorityDto();
        authority.setName("User");
        authority.setOrganizationId(organizationId.toString());

        RoleDto payload = new RoleDto();
        payload.setName("User");
        payload.setAuthorities(List.of(authority));
        payload.setOrganizationId(organizationId.toString());

        RoleDto result = new RoleDto();
        result.setName("User");
        result.setAuthorities(List.of(authority));
        result.setOrganizationId(organizationId.toString());
        result.setId(snowflakeGenerator.next().toString());

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(roleService.createRole(Mockito.any())).thenReturn(result);
        mockMvc.perform(post("/api/roles/create")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    void updateRole_successTest() throws Exception {
        Long roleId = snowflakeGenerator.next();
        Long organizationId = snowflakeGenerator.next();
        AuthorityDto authority = new AuthorityDto();
        authority.setName("User");
        authority.setOrganizationId(organizationId.toString());
        RoleDto payload = new RoleDto();
        payload.setName("User");
        payload.setAuthorities(List.of(authority));
        payload.setId(roleId.toString());
        payload.setOrganizationId(organizationId.toString());

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(roleService.updateRole(Mockito.any())).thenReturn(payload);
        mockMvc.perform(put("/api/roles/update")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void updateRole_alreadyExistsTest() throws Exception {
        Long roleId = snowflakeGenerator.next();
        Long organizationId = snowflakeGenerator.next();
        RoleDto payload = new RoleDto();
        payload.setName("User");
        payload.setAuthorities(List.of());
        payload.setId(roleId.toString());
        payload.setOrganizationId(organizationId.toString());

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(roleService.updateRole(Mockito.any())).thenThrow(RoleAlreadyExistsException.class);
        mockMvc.perform(put("/api/roles/update")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateRole_notFoundTest() throws Exception {
        Long roleId = snowflakeGenerator.next();
        Long organizationId = snowflakeGenerator.next();
        RoleDto payload = new RoleDto();
        payload.setName("User");
        payload.setAuthorities(List.of());
        payload.setId(roleId.toString());
        payload.setOrganizationId(organizationId.toString());

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(roleService.updateRole(Mockito.any())).thenThrow(RoleNotFoundException.class);
        mockMvc.perform(put("/api/roles/update")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteRole_successTest() throws Exception {
        Long roleId = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();
        Mockito.doNothing().when(roleService).deleteRole(roleId);
        mockMvc.perform(delete("/api/roles/delete/{id}", roleId)
                        .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))).andExpect(status().isNoContent());
    }

    @Test
    void getRolesTest() throws Exception {
        Long organizationId = snowflakeGenerator.next();
        Mockito.when(roleService.getRoles(organizationId)).thenReturn(List.of());
        String keycloakId = UUID.randomUUID().toString();
        mockMvc.perform(get("/api/roles/organization/{id}", organizationId)
                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))).andExpect(status().isOk());
    }

    @Test
    void addAuthorityToRoleTest() throws Exception {
        Long roleId = snowflakeGenerator.next();
        Long authorityId = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();
        Mockito.doNothing().when(roleService).addAuthorityToRole(roleId, authorityId);
        mockMvc.perform(post("/api/roles/authority/add/{roleId}/{authorityId}", roleId, authorityId)
                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))).andExpect(status().isNoContent());
    }

    @Test
    void removeAuthorityFromRoleTest() throws Exception {
        Long roleId = snowflakeGenerator.next();
        Long authorityId = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();
        Mockito.doNothing().when(roleService).removeAuthorityFromRole(roleId, authorityId);
        mockMvc.perform(post("/api/roles/authority/remove/{roleId}/{authorityId}", roleId, authorityId)
                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))).andExpect(status().isNoContent());
    }

}
