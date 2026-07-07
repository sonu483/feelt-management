package com.feelt.fleet.controller;

import com.feelt.fleet.exception.ResourceNotFoundException;
import com.feelt.fleet.model.AppUser;
import com.feelt.fleet.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private final UserRepository users;
    public UserController(UserRepository users) { this.users = users; }
    @GetMapping public Page<AppUser> list(Pageable pageable) { return users.findAll(pageable); }
    @GetMapping("/{id}") public AppUser get(@PathVariable Long id) {
        return users.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }
    @PatchMapping("/{id}/enabled/{enabled}") public AppUser setEnabled(@PathVariable Long id, @PathVariable boolean enabled) {
        AppUser user = get(id); user.setEnabled(enabled); return users.save(user);
    }
}
