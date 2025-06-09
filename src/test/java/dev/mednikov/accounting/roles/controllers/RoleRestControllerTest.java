package dev.mednikov.accounting.roles.controllers;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.roles.dto.RoleDto;
import dev.mednikov.accounting.roles.services.RoleService;
import dev.mednikov.accounting.users.models.User;
import dev.mednikov.accounting.users.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoleRestController.class)
class RoleRestControllerTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Autowired private MockMvc mvc;
    @MockitoBean private RoleService roleService;
    @MockitoBean private UserService userService;

    @Test
    void getActiveRole_existsTest() throws Exception {
        String keycloakId = UUID.randomUUID().toString();
        User user = new User();
        user.setEmail("jixafordywtyv4@rediffmail.com");
        user.setFirstName("Gretel");
        user.setLastName("Dittrich");
        user.setKeycloakId(keycloakId);
        user.setId(snowflakeGenerator.next());

        RoleDto roleDto = new RoleDto();
        roleDto.setActive(true);
        roleDto.setId(snowflakeGenerator.next().toString());

        Mockito.when(userService.getOrCreateUser(Mockito.any())).thenReturn(user);
        Mockito.when(roleService.getActiveRole(user)).thenReturn(Optional.of(roleDto));

        mvc.perform(get("/api/roles/active")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getActiveRole_notExistsTest() throws Exception {
        String keycloakId = UUID.randomUUID().toString();
        User user = new User();
        user.setEmail("o6aged4sk3@msn.com");
        user.setFirstName("Gabriela");
        user.setLastName("Beier");
        user.setKeycloakId(keycloakId);
        user.setId(snowflakeGenerator.next());

        Mockito.when(userService.getOrCreateUser(Mockito.any())).thenReturn(user);
        Mockito.when(roleService.getActiveRole(user)).thenReturn(Optional.empty());

        mvc.perform(get("/api/roles/active")
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void setActiveRoleTest() throws Exception {
        String keycloakId = UUID.randomUUID().toString();
        User user = new User();
        user.setEmail("3f4r0p9p0pgf0w8@gmail.com");
        user.setFirstName("Johanne");
        user.setLastName("Schröder");
        user.setKeycloakId(keycloakId);
        user.setId(snowflakeGenerator.next());

        Long roleId = snowflakeGenerator.next();
        RoleDto roleDto = new RoleDto();
        roleDto.setActive(true);
        roleDto.setId(roleId.toString());

        Mockito.when(userService.getOrCreateUser(Mockito.any())).thenReturn(user);
        Mockito.when(roleService.setActiveRole(user, roleId)).thenReturn(roleDto);

        mvc.perform(post("/api/roles/active/{roleId}", roleId)
                        .with(jwt().jwt(jwt -> jwt
                                .claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
