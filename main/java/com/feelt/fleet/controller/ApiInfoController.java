package com.feelt.fleet.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class ApiInfoController {

    private final Optional<BuildProperties> buildProperties;

    @Value("${spring.application.name}")
    private String applicationName;

    public ApiInfoController(Optional<BuildProperties> buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping("/api")
    public Map<String, Object> apiInfo() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("name", applicationName);
        info.put("status", "UP");
        info.put("version", buildProperties.map(BuildProperties::getVersion).orElse("1.0.0"));
        info.put("timestamp", Instant.now());
        info.put("swagger", "/swagger-ui.html");
        info.put("health", "/actuator/health");
        return info;
    }
}
