package dev.mednikov.accounting.authorities.controllers;

import dev.mednikov.accounting.authorities.dto.AuthorityDto;
import dev.mednikov.accounting.authorities.services.AuthorityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authorities")
public class AuthorityRestController {

    private final AuthorityService authorityService;

    public AuthorityRestController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody AuthorityDto createAuthority(@RequestBody AuthorityDto body) {
        return this.authorityService.createAuthority(body);
    }

    @PutMapping("/update")
    public @ResponseBody AuthorityDto updateAuthority(@RequestBody AuthorityDto body) {
        return this.authorityService.updateAuthority(body);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthority (@PathVariable Long id){
        this.authorityService.deleteAuthority(id);
    }

    @GetMapping("/organization/{organizationId}")
    public @ResponseBody List<AuthorityDto> getAuthorities(@PathVariable Long organizationId){
        return this.authorityService.getAuthorities(organizationId);
    }

}
