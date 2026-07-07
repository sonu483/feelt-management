package com.feelt.fleet.service;

import com.feelt.fleet.dto.*;
import com.feelt.fleet.jwt.JwtService;
import com.feelt.fleet.model.*;
import com.feelt.fleet.repository.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder passwords;
    private final AuthenticationManager authentication;
    private final JwtService jwt;

    public AuthService(UserRepository users, RoleRepository roles, PasswordEncoder passwords, AuthenticationManager authentication, JwtService jwt) {
        this.users = users; this.roles = roles; this.passwords = passwords; this.authentication = authentication; this.jwt = jwt;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (users.existsByEmailIgnoreCase(request.email())) throw new IllegalArgumentException("Email is already registered");
        Role customer = roles.findByName("ROLE_CUSTOMER").orElseGet(() -> roles.save(new Role("ROLE_CUSTOMER")));
        AppUser user = new AppUser();
        user.setFullName(request.fullName().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPasswordHash(passwords.encode(request.password()));
        user.setRoles(Set.of(customer));
        users.save(user);
        return response(user, org.springframework.security.core.userdetails.User.withUsername(user.getEmail()).password(user.getPasswordHash()).authorities("ROLE_CUSTOMER").build());
    }

    public AuthResponse login(LoginRequest request) {
        var authenticated = authentication.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        AppUser user = users.findByEmailIgnoreCase(authenticated.getName()).orElseThrow();
        return response(user, (UserDetails) authenticated.getPrincipal());
    }

    private AuthResponse response(AppUser user, UserDetails details) {
        Set<String> roleNames = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        return new AuthResponse(jwt.generate(details), "Bearer", jwt.expirationSeconds(), user.getId(), user.getFullName(), user.getEmail(), roleNames);
    }
}
