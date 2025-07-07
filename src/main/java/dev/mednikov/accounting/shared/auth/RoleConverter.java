package dev.mednikov.accounting.shared.auth;

import dev.mednikov.accounting.roles.models.Role;
import dev.mednikov.accounting.roles.models.RoleType;
import dev.mednikov.accounting.roles.repositories.RoleRepository;
import dev.mednikov.accounting.users.models.User;
import dev.mednikov.accounting.users.services.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Profile({"dev", "prod"})
public class RoleConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final RoleRepository roleRepository;
    private final UserService userService;

    public RoleConverter(RoleRepository roleRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        User user = this.userService.getOrCreateUser(jwt);
        Optional<Role> activeRole = this.roleRepository.findActiveRoleForUser(user.getId());
        if (activeRole.isPresent()) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            Role role = activeRole.get();
            // add authorities for any user
            authorities.add(new SimpleGrantedAuthority("organizations:create"));
            authorities.add(new SimpleGrantedAuthority("accounts:view"));
            authorities.add(new SimpleGrantedAuthority("transactions:view"));
            authorities.add(new SimpleGrantedAuthority("transactions:create"));
            authorities.add(new SimpleGrantedAuthority("reports:view"));

            // add authorities based on the user role
            if (role.getRoleType() == RoleType.OWNER){
                // owner only
                authorities.add(new SimpleGrantedAuthority("organizations:delete"));
            }
            if (role.getRoleType() != RoleType.USER){
                // administrator, owner or accountant
                authorities.add(new SimpleGrantedAuthority("accounts:create"));
                authorities.add(new SimpleGrantedAuthority("accounts:update"));
                authorities.add(new SimpleGrantedAuthority("accounts:delete"));
            }
            if (role.getRoleType() == RoleType.OWNER || role.getRoleType() == RoleType.ADMINISTRATOR){
                // administrator or owner only
                authorities.add(new SimpleGrantedAuthority("roles:create"));
                authorities.add(new SimpleGrantedAuthority("roles:delete"));
                authorities.add(new SimpleGrantedAuthority("roles:view"));
            }

            return new JwtAuthenticationToken(jwt, authorities);
        } else {
            return new JwtAuthenticationToken(jwt, List.of());
        }
    }
}
