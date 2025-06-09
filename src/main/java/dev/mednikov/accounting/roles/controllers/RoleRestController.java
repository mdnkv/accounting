package dev.mednikov.accounting.roles.controllers;

import dev.mednikov.accounting.roles.dto.RoleDto;
import dev.mednikov.accounting.roles.services.RoleService;
import dev.mednikov.accounting.users.models.User;
import dev.mednikov.accounting.users.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleRestController {

    private final RoleService roleService;
    private final UserService userService;

    public RoleRestController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @PostMapping("/active/{roleId}")
    public @ResponseBody RoleDto setActiveRole (@PathVariable Long roleId, @AuthenticationPrincipal Jwt jwt) {
        User user = this.userService.getOrCreateUser(jwt);
        return this.roleService.setActiveRole(user, roleId);
    }

    @GetMapping("/active")
    public ResponseEntity<RoleDto> getActiveRole (@AuthenticationPrincipal Jwt jwt) {
        User user = this.userService.getOrCreateUser(jwt);
        Optional<RoleDto> role = this.roleService.getActiveRole(user);
        return ResponseEntity.of(role);
    }

    @GetMapping("/user")
    public List<RoleDto> getRoles (@AuthenticationPrincipal Jwt jwt) {
        User user = this.userService.getOrCreateUser(jwt);
        return this.roleService.getRolesForUser(user);
    }

}
