package dev.mednikov.accounting.organizations.controllers;

import dev.mednikov.accounting.organizations.dto.OrganizationUserDto;
import dev.mednikov.accounting.organizations.services.OrganizationUserService;
import dev.mednikov.accounting.users.models.User;
import dev.mednikov.accounting.users.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/organization-users")
public class OrganizationUserRestController {

    private final OrganizationUserService organizationUserService;
    private final UserService userService;

    public OrganizationUserRestController(OrganizationUserService organizationUserService, UserService userService) {
        this.organizationUserService = organizationUserService;
        this.userService = userService;
    }

    @GetMapping("/current/all")
    public @ResponseBody List<OrganizationUserDto> getAllForUser(@AuthenticationPrincipal Jwt jwt) {
        User user = this.userService.getOrCreateUser(jwt);
        return this.organizationUserService.getAllForUser(user);
    }

    @PostMapping("/current/active/{id}")
    public @ResponseBody OrganizationUserDto setActiveForUser (
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id
    ) {
        User user = this.userService.getOrCreateUser(jwt);
        return this.organizationUserService.setActiveForUser(user, id);
    }

    @GetMapping("/current/active")
    public ResponseEntity<OrganizationUserDto> getActiveForUser (@AuthenticationPrincipal Jwt jwt) {
        User user = this.userService.getOrCreateUser(jwt);
        Optional<OrganizationUserDto> result = this.organizationUserService.getActiveForUser(user);
        return ResponseEntity.of(result);
    }

}
