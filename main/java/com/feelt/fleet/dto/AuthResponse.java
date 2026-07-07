package com.feelt.fleet.dto;

import java.util.Set;

public record AuthResponse(String accessToken, String tokenType, long expiresInSeconds, Long userId, String fullName, String email, Set<String> roles) {}
