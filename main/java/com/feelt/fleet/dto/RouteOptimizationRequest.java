package com.feelt.fleet.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RouteOptimizationRequest(
        @NotNull CoordinateRequest depot,
        @NotEmpty List<@Valid CoordinateRequest> stops
) {
}
