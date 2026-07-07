package com.feelt.fleet.service;

import com.feelt.fleet.dto.CoordinateRequest;
import com.feelt.fleet.dto.RouteOptimizationRequest;
import com.feelt.fleet.dto.RouteOptimizationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RouteOptimizationServiceTest {

    @Test
    void optimizeOrdersNearestStopsFirst() {
        DistanceService distanceService = new DistanceService(WebClient.builder(), "http://localhost", false);
        RouteOptimizationService service = new RouteOptimizationService(distanceService);

        RouteOptimizationResponse response = service.optimize(new RouteOptimizationRequest(
                new CoordinateRequest("Depot", 28.6139, 77.2090),
                List.of(
                        new CoordinateRequest("Far Stop", 28.7041, 77.1025),
                        new CoordinateRequest("Near Stop", 28.6200, 77.2200)
                )
        ));

        assertThat(response.optimizedStops()).hasSize(2);
        assertThat(response.optimizedStops().get(0).address()).isEqualTo("Near Stop");
        assertThat(response.totalDistanceKm()).isGreaterThan(0);
    }
}
