package com.feelt.fleet.config;

import com.feelt.fleet.model.*;
import com.feelt.fleet.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class SecurityDataInitializer implements CommandLineRunner {
    private static final Set<String> DEFAULT_ROLES = Set.of("ROLE_ADMIN", "ROLE_FLEET_MANAGER", "ROLE_DISPATCHER", "ROLE_DRIVER", "ROLE_CUSTOMER");
    private final RoleRepository roles;
    private final UserRepository users;
    private final PasswordEncoder passwords;
    private final String adminEmail;
    private final String adminPassword;

    public SecurityDataInitializer(RoleRepository roles, UserRepository users, PasswordEncoder passwords,
            @Value("${app.security.bootstrap-admin-email}") String adminEmail,
            @Value("${app.security.bootstrap-admin-password}") String adminPassword) {
        this.roles = roles; this.users = users; this.passwords = passwords;
        this.adminEmail = adminEmail; this.adminPassword = adminPassword;
    }

    @Override @Transactional
    public void run(String... args) {
        DEFAULT_ROLES.forEach(name -> roles.findByName(name).orElseGet(() -> roles.save(new Role(name))));
        users.findByEmailIgnoreCase(adminEmail).orElseGet(() -> {
            AppUser admin = new AppUser();
            admin.setFullName("System Administrator");
            admin.setEmail(adminEmail.toLowerCase());
            admin.setPasswordHash(passwords.encode(adminPassword));
            admin.setRoles(new LinkedHashSet<>(roles.findAll()));
            return users.save(admin);
        });
    }
}
