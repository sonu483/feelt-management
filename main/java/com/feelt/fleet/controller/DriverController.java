package com.feelt.fleet.controller;

import com.feelt.fleet.exception.ResourceNotFoundException;
import com.feelt.fleet.model.Driver;
import com.feelt.fleet.model.DriverStatus;
import com.feelt.fleet.repository.DriverRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverRepository driverRepository;

    public DriverController(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @GetMapping
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    @GetMapping("/page")
    public Page<Driver> getDriversPage(
            @RequestParam(required = false) DriverStatus status,
            @RequestParam(defaultValue = "") String search,
            Pageable pageable
    ) {
        if (status != null) {
            return driverRepository.findByStatus(status, pageable);
        }
        if (!search.isBlank()) {
            return driverRepository.findByFullNameContainingIgnoreCaseOrLicenseNumberContainingIgnoreCase(
                    search,
                    search,
                    pageable
            );
        }
        return driverRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Driver getDriver(@PathVariable Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found: " + id));
    }

    @GetMapping("/available")
    public List<Driver> getAvailableDrivers() {
        return driverRepository.findByStatus(DriverStatus.AVAILABLE);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Driver createDriver(@Valid @RequestBody Driver driver) {
        if (driver.getStatus() == null) {
            driver.setStatus(DriverStatus.AVAILABLE);
        }
        return driverRepository.save(driver);
    }

    @PutMapping("/{id}")
    public Driver updateDriver(@PathVariable Long id, @Valid @RequestBody Driver updatedDriver) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found: " + id));
        driver.setFullName(updatedDriver.getFullName());
        driver.setLicenseNumber(updatedDriver.getLicenseNumber());
        driver.setLicenseValidUntil(updatedDriver.getLicenseValidUntil());
        driver.setShiftHours(updatedDriver.getShiftHours());
        driver.setStatus(updatedDriver.getStatus() == null ? DriverStatus.AVAILABLE : updatedDriver.getStatus());
        driver.setPhone(updatedDriver.getPhone());
        return driverRepository.save(driver);
    }

    @PatchMapping("/{id}/status/{status}")
    public Driver updateStatus(@PathVariable Long id, @PathVariable DriverStatus status) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found: " + id));
        driver.setStatus(status);
        return driverRepository.save(driver);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDriver(@PathVariable Long id) {
        if (!driverRepository.existsById(id)) {
            throw new ResourceNotFoundException("Driver not found: " + id);
        }
        driverRepository.deleteById(id);
    }
}
