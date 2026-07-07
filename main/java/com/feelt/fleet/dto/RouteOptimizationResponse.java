package com.feelt.fleet.dto;

import java.util.List;

public record RouteOptimizationResponse(
        List<OptimizedStopResponse> optimizedStops,
        Double totalDistanceKm,
        Double estimatedFuelLitres
) {
}
