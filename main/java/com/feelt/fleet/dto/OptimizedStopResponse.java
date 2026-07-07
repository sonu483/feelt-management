package com.feelt.fleet.dto;

public record OptimizedStopResponse(
        int sequence,
        String address,
        Double latitude,
        Double longitude,
        Double distanceFromPreviousKm
) {
}
