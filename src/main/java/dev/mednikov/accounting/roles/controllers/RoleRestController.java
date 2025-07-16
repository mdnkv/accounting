package dev.mednikov.accounting.roles.controllers;

import dev.mednikov.accounting.roles.dto.RoleDto;
import dev.mednikov.accounting.roles.services.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleRestController {

    private final RoleService roleService;

    public RoleRestController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody RoleDto createRole (@RequestBody RoleDto body){
        return this.roleService.createRole(body);
    }

    @PutMapping("/update")
    public @ResponseBody RoleDto updateRole (@RequestBody RoleDto body){
        return this.roleService.updateRole(body);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole (@PathVariable Long id){
        this.roleService.deleteRole(id);
    }

    @GetMapping("/organization/{organizationId}")
    public @ResponseBody List<RoleDto> getRoles (@PathVariable Long organizationId){
        return this.roleService.getRoles(organizationId);
    }

    @PostMapping("/authority/add/{roleId}/{authorityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addAuthorityToRole (@PathVariable Long roleId, @PathVariable Long authorityId){
        this.roleService.addAuthorityToRole(roleId, authorityId);
    }

    @PostMapping("/authority/remove/{roleId}/{authorityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAuthorityFromRole (@PathVariable Long roleId, @PathVariable Long authorityId){
        this.roleService.removeAuthorityFromRole(roleId, authorityId);
    }

}
