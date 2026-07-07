package com.feelt.fleet.service;

import com.feelt.fleet.dto.CoordinateRequest;
import com.feelt.fleet.dto.OptimizedStopResponse;
import com.feelt.fleet.dto.RouteOptimizationRequest;
import com.feelt.fleet.dto.RouteOptimizationResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RouteOptimizationService {

    private static final double AVERAGE_FUEL_KM_PER_LITRE = 8.0;

    private final DistanceService distanceService;

    public RouteOptimizationService(DistanceService distanceService) {
        this.distanceService = distanceService;
    }

    public RouteOptimizationResponse optimize(RouteOptimizationRequest request) {
        List<CoordinateRequest> remaining = new ArrayList<>(request.stops());
        CoordinateRequest current = request.depot();
        List<OptimizedStopResponse> optimizedStops = new ArrayList<>();
        double totalDistance = 0.0;
        int sequence = 1;

        while (!remaining.isEmpty()) {
            CoordinateRequest finalCurrent = current;
            CoordinateRequest next = remaining.stream()
                    .min(Comparator.comparingDouble(stop -> distanceService.distanceKm(finalCurrent, stop)))
                    .orElseThrow();

            double legDistance = distanceService.distanceKm(current, next);
            totalDistance += legDistance;
            optimizedStops.add(new OptimizedStopResponse(
                    sequence++,
                    next.address(),
                    next.latitude(),
                    next.longitude(),
                    distanceService.round(legDistance)
            ));
            current = next;
            remaining.remove(next);
        }

        totalDistance = distanceService.round(totalDistance);
        return new RouteOptimizationResponse(
                optimizedStops,
                totalDistance,
                distanceService.round(totalDistance / AVERAGE_FUEL_KM_PER_LITRE)
        );
    }
}
