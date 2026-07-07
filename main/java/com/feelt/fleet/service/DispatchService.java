package com.feelt.fleet.service;

import com.feelt.fleet.dto.*;
import com.feelt.fleet.exception.InvalidDispatchException;
import com.feelt.fleet.exception.ResourceNotFoundException;
import com.feelt.fleet.model.*;
import com.feelt.fleet.repository.DriverRepository;
import com.feelt.fleet.repository.RouteRepository;
import com.feelt.fleet.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DispatchService {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final RouteRepository routeRepository;
    private final RouteOptimizationService routeOptimizationService;

    public DispatchService(
            VehicleRepository vehicleRepository,
            DriverRepository driverRepository,
            RouteRepository routeRepository,
            RouteOptimizationService routeOptimizationService
    ) {
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
        this.routeRepository = routeRepository;
        this.routeOptimizationService = routeOptimizationService;
    }

    @Transactional
    public Route dispatch(DispatchRequest request) {
        Vehicle vehicle = vehicleRepository.findById(request.vehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + request.vehicleId()));
        Driver driver = driverRepository.findById(request.driverId())
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found: " + request.driverId()));

        if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
            throw new InvalidDispatchException("Vehicle is not available for dispatch");
        }
        if (driver.getStatus() != DriverStatus.AVAILABLE) {
            throw new InvalidDispatchException("Driver is not available for dispatch");
        }

        List<CoordinateRequest> stops = request.deliveries().stream()
                .map(delivery -> new CoordinateRequest(delivery.address(), delivery.latitude(), delivery.longitude()))
                .toList();

        RouteOptimizationResponse optimized = routeOptimizationService.optimize(
                new RouteOptimizationRequest(request.depot(), stops)
        );

        Route route = new Route();
        route.setVehicle(vehicle);
        route.setDriver(driver);
        route.setDispatchedAt(LocalDateTime.now());
        route.setTotalDistanceKm(optimized.totalDistanceKm());
        route.setEstimatedFuelLitres(optimized.estimatedFuelLitres());

        List<DeliveryStopRequest> pendingDeliveries = new ArrayList<>(request.deliveries());
        for (OptimizedStopResponse stop : optimized.optimizedStops()) {
            DeliveryStopRequest original = pendingDeliveries.stream()
                    .filter(item -> item.address().equals(stop.address()))
                    .findFirst()
                    .orElseThrow();
            pendingDeliveries.remove(original);

            DeliveryTask task = new DeliveryTask();
            task.setCustomerName(original.customerName());
            task.setAddress(original.address());
            task.setLatitude(original.latitude());
            task.setLongitude(original.longitude());
            task.setPackageWeightKg(original.packageWeightKg());
            task.setSequenceNumber(stop.sequence());
            task.setStatus(DeliveryStatus.DISPATCHED);
            task.setRoute(route);
            route.getDeliveryTasks().add(task);
        }

        vehicle.setStatus(VehicleStatus.ASSIGNED);
        driver.setStatus(DriverStatus.ON_ROUTE);
        return routeRepository.save(route);
    }
}
