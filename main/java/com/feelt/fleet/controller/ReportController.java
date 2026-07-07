package com.feelt.fleet.controller;

import com.feelt.fleet.model.TripStatus;
import com.feelt.fleet.repository.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final TripRepository tripRepository;
    private final FuelRecordRepository fuelRepository;
    private final MaintenanceRecordRepository maintenanceRepository;
    private final FleetOrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public ReportController(VehicleRepository vehicleRepository,
                            DriverRepository driverRepository,
                            TripRepository tripRepository,
                            FuelRecordRepository fuelRepository,
                            MaintenanceRecordRepository maintenanceRepository,
                            FleetOrderRepository orderRepository,
                            CustomerRepository customerRepository) {
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
        this.tripRepository = tripRepository;
        this.fuelRepository = fuelRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        BigDecimal fuelCost = fuelRepository.totalFuelCost();
        BigDecimal maintenanceCost = maintenanceRepository.totalMaintenanceCost();
        BigDecimal tripExpense = tripRepository.totalTripExpense();
        BigDecimal revenue = tripRepository.totalCompletedRevenue().add(orderRepository.totalOrderValue());
        BigDecimal totalExpense = fuelCost.add(maintenanceCost).add(tripExpense);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalVehicles", vehicleRepository.count());
        data.put("totalDrivers", driverRepository.count());
        data.put("totalCustomers", customerRepository.count());
        data.put("totalOrders", orderRepository.count());
        data.put("activeTrips", tripRepository.countByStatus(TripStatus.ACTIVE));
        data.put("totalFuelCost", fuelCost);
        data.put("maintenanceCost", maintenanceCost);
        data.put("revenue", revenue);
        data.put("profit", revenue.subtract(totalExpense));
        return data;
    }

    @GetMapping("/profit")
    public Map<String, Object> profitReport() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("revenue", tripRepository.totalCompletedRevenue().add(orderRepository.totalOrderValue()));
        data.put("fuelCost", fuelRepository.totalFuelCost());
        data.put("maintenanceCost", maintenanceRepository.totalMaintenanceCost());
        data.put("tripExpense", tripRepository.totalTripExpense());
        return data;
    }
}
