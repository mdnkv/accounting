package dev.mednikov.accounting.organizations.controllers;

import dev.mednikov.accounting.organizations.dto.OrganizationDto;
import dev.mednikov.accounting.organizations.services.OrganizationService;
import dev.mednikov.accounting.users.models.User;
import dev.mednikov.accounting.users.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationRestController {

    private final UserService userService;
    private final OrganizationService organizationService;

    public OrganizationRestController(OrganizationService organizationService, UserService userService) {
        this.organizationService = organizationService;
        this.userService = userService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody OrganizationDto createOrganization(
            @RequestBody OrganizationDto organizationDto,
            @AuthenticationPrincipal Jwt jwt
            ) {
        User user = this.userService.getOrCreateUser(jwt);
        return this.organizationService.createOrganization(user, organizationDto);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('organizations:update')")
    public @ResponseBody OrganizationDto updateOrganization(@RequestBody OrganizationDto organizationDto) {
        return this.organizationService.updateOrganization(organizationDto);
    }

    @DeleteMapping("/delete/{organizationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('organizations:delete')")
    public void deleteOrganization(@PathVariable Long organizationId) {
        this.organizationService.deleteOrganization(organizationId);
    }

    @GetMapping("/organization/{organizationId}")
    public @ResponseBody ResponseEntity<OrganizationDto> getOrganization(@PathVariable Long organizationId) {
        Optional<OrganizationDto> result = this.organizationService.getOrganization(organizationId);
        return ResponseEntity.of(result);
    }

}
