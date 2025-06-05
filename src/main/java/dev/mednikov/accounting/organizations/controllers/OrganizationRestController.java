package dev.mednikov.accounting.organizations.controllers;

import dev.mednikov.accounting.organizations.dto.OrganizationDto;
import dev.mednikov.accounting.organizations.services.OrganizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationRestController {

    private final OrganizationService organizationService;

    public OrganizationRestController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody OrganizationDto createOrganization(@RequestBody OrganizationDto organizationDto) {
        return this.organizationService.createOrganization(organizationDto);
    }

    @PutMapping("/update")
    public @ResponseBody OrganizationDto updateOrganization(@RequestBody OrganizationDto organizationDto) {
        return this.organizationService.updateOrganization(organizationDto);
    }

    @DeleteMapping("/delete/{organizationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization(@PathVariable Long organizationId) {
        this.organizationService.deleteOrganization(organizationId);
    }

    @GetMapping("/organization/{organizationId}")
    public @ResponseBody ResponseEntity<OrganizationDto> getOrganization(@PathVariable Long organizationId) {
        Optional<OrganizationDto> result = this.organizationService.getOrganization(organizationId);
        return ResponseEntity.of(result);
    }

}
