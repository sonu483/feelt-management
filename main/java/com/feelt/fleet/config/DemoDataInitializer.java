package com.feelt.fleet.config;

import com.feelt.fleet.model.DeliveryStatus;
import com.feelt.fleet.model.DeliveryTask;
import com.feelt.fleet.model.Driver;
import com.feelt.fleet.model.DriverStatus;
import com.feelt.fleet.model.Route;
import com.feelt.fleet.model.Vehicle;
import com.feelt.fleet.model.VehicleStatus;
import com.feelt.fleet.repository.DeliveryTaskRepository;
import com.feelt.fleet.repository.DriverRepository;
import com.feelt.fleet.repository.RouteRepository;
import com.feelt.fleet.repository.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DemoDataInitializer implements CommandLineRunner {
    private final VehicleRepository vehicles;
    private final DriverRepository drivers;
    private final RouteRepository routes;
    private final DeliveryTaskRepository deliveries;

    public DemoDataInitializer(VehicleRepository vehicles, DriverRepository drivers, RouteRepository routes,
                               DeliveryTaskRepository deliveries) {
        this.vehicles = vehicles;
        this.drivers = drivers;
        this.routes = routes;
        this.deliveries = deliveries;
    }

    @Override
    public void run(String... args) {
        if (vehicles.count() > 0 || drivers.count() > 0 || routes.count() > 0 || deliveries.count() > 0) {
            return;
        }

        Vehicle alpha = vehicle("DL-01-FMS-2048", "Tata Prima 5530.S", 18500, VehicleStatus.ASSIGNED, 28.6139, 77.2090);
        Vehicle beta = vehicle("HR-26-FMS-1188", "Ashok Leyland AVTR", 12500, VehicleStatus.AVAILABLE, 28.4595, 77.0266);
        Vehicle gamma = vehicle("UP-16-FMS-7742", "Eicher Pro 3015", 9200, VehicleStatus.IN_MAINTENANCE, 28.5355, 77.3910);
        vehicles.saveAll(List.of(alpha, beta, gamma));

        Driver arjun = driver("Arjun Mehta", "DL-FMS-ARJ-9211", DriverStatus.ON_ROUTE, "9876501101", 10);
        Driver kavya = driver("Kavya Singh", "HR-FMS-KAV-3108", DriverStatus.AVAILABLE, "9876501102", 9);
        Driver imran = driver("Imran Khan", "UP-FMS-IMR-6640", DriverStatus.OFF_DUTY, "9876501103", 8);
        drivers.saveAll(List.of(arjun, kavya, imran));

        Route morningRoute = new Route();
        morningRoute.setVehicle(alpha);
        morningRoute.setDriver(arjun);
        morningRoute.setDispatchedAt(LocalDateTime.now().minusHours(2));
        morningRoute.setTotalDistanceKm(86.4);
        morningRoute.setEstimatedFuelLitres(18.7);
        routes.save(morningRoute);

        DeliveryTask first = delivery("Northwind Retail", "Karol Bagh, New Delhi", 28.6517, 77.1907, 1,
                DeliveryStatus.IN_TRANSIT, 320, morningRoute);
        DeliveryTask second = delivery("Metro Parts Hub", "Noida Sector 62", 28.6270, 77.3750, 2,
                DeliveryStatus.DISPATCHED, 540, morningRoute);
        DeliveryTask third = delivery("Cyber City Stores", "Gurugram Cyber City", 28.4950, 77.0890, 3,
                DeliveryStatus.DISPATCHED, 260, morningRoute);
        deliveries.saveAll(List.of(first, second, third));
    }

    private Vehicle vehicle(String plate, String model, int capacityKg, VehicleStatus status,
                            double latitude, double longitude) {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(plate);
        vehicle.setModel(model);
        vehicle.setCapacityKg(capacityKg);
        vehicle.setStatus(status);
        vehicle.setCurrentLatitude(latitude);
        vehicle.setCurrentLongitude(longitude);
        vehicle.setLastMaintenanceDate(LocalDate.now().minusDays(18));
        vehicle.setNextMaintenanceDate(LocalDate.now().plusDays(42));
        return vehicle;
    }

    private Driver driver(String name, String license, DriverStatus status, String phone, int shiftHours) {
        Driver driver = new Driver();
        driver.setFullName(name);
        driver.setLicenseNumber(license);
        driver.setStatus(status);
        driver.setPhone(phone);
        driver.setShiftHours(shiftHours);
        driver.setLicenseValidUntil(LocalDate.now().plusYears(3));
        return driver;
    }

    private DeliveryTask delivery(String customer, String address, double latitude, double longitude,
                                  int sequence, DeliveryStatus status, int weightKg, Route route) {
        DeliveryTask task = new DeliveryTask();
        task.setCustomerName(customer);
        task.setAddress(address);
        task.setLatitude(latitude);
        task.setLongitude(longitude);
        task.setSequenceNumber(sequence);
        task.setStatus(status);
        task.setPackageWeightKg(weightKg);
        task.setTimeWindowStart(LocalDateTime.now().plusHours(sequence));
        task.setTimeWindowEnd(LocalDateTime.now().plusHours(sequence + 2L));
        task.setRoute(route);
        return task;
    }
}
