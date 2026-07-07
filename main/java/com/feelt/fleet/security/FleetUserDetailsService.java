package com.feelt.fleet.security;

import com.feelt.fleet.model.AppUser;
import com.feelt.fleet.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class FleetUserDetailsService implements UserDetailsService {
    private final UserRepository users;
    public FleetUserDetailsService(UserRepository users) { this.users = users; }

    @Override
    public UserDetails loadUserByUsername(String email) {
        AppUser user = users.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return User.withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .disabled(!user.isEnabled())
                .authorities(user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList())
                .build();
    }
}
