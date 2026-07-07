package com.feelt.fleet.controller;

import com.feelt.fleet.exception.ResourceNotFoundException;
import com.feelt.fleet.model.Vehicle;
import com.feelt.fleet.model.VehicleStatus;
import com.feelt.fleet.repository.VehicleRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleRepository vehicleRepository;

    public VehicleController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @GetMapping("/page")
    public Page<Vehicle> getVehiclesPage(
            @RequestParam(required = false) VehicleStatus status,
            @RequestParam(defaultValue = "") String search,
            Pageable pageable
    ) {
        if (status != null) {
            return vehicleRepository.findByStatus(status, pageable);
        }
        if (!search.isBlank()) {
            return vehicleRepository.findByLicensePlateContainingIgnoreCaseOrModelContainingIgnoreCase(
                    search,
                    search,
                    pageable
            );
        }
        return vehicleRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Vehicle getVehicle(@PathVariable Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + id));
    }

    @GetMapping("/available")
    public List<Vehicle> getAvailableVehicles() {
        return vehicleRepository.findByStatus(VehicleStatus.AVAILABLE);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicle createVehicle(@Valid @RequestBody Vehicle vehicle) {
        if (vehicle.getStatus() == null) {
            vehicle.setStatus(VehicleStatus.AVAILABLE);
        }
        return vehicleRepository.save(vehicle);
    }

    @PutMapping("/{id}")
    public Vehicle updateVehicle(@PathVariable Long id, @Valid @RequestBody Vehicle updatedVehicle) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + id));
        vehicle.setLicensePlate(updatedVehicle.getLicensePlate());
        vehicle.setModel(updatedVehicle.getModel());
        vehicle.setCapacityKg(updatedVehicle.getCapacityKg());
        vehicle.setStatus(updatedVehicle.getStatus() == null ? VehicleStatus.AVAILABLE : updatedVehicle.getStatus());
        vehicle.setLastMaintenanceDate(updatedVehicle.getLastMaintenanceDate());
        vehicle.setNextMaintenanceDate(updatedVehicle.getNextMaintenanceDate());
        vehicle.setCurrentLatitude(updatedVehicle.getCurrentLatitude());
        vehicle.setCurrentLongitude(updatedVehicle.getCurrentLongitude());
        return vehicleRepository.save(vehicle);
    }

    @PatchMapping("/{id}/status/{status}")
    public Vehicle updateStatus(@PathVariable Long id, @PathVariable VehicleStatus status) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + id));
        vehicle.setStatus(status);
        return vehicleRepository.save(vehicle);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVehicle(@PathVariable Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle not found: " + id);
        }
        vehicleRepository.deleteById(id);
    }
}
