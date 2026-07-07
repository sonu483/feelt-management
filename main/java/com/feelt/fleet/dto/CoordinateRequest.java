package com.feelt.fleet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CoordinateRequest(
        @NotBlank String address,
        @NotNull Double latitude,
        @NotNull Double longitude
) {
}
