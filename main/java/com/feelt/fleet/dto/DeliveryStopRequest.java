package com.feelt.fleet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DeliveryStopRequest(
        @NotBlank String customerName,
        @NotBlank String address,
        @NotNull Double latitude,
        @NotNull Double longitude,
        Integer packageWeightKg
) {
}
