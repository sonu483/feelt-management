package com.feelt.fleet.controller;

import com.feelt.fleet.exception.ResourceNotFoundException;
import com.feelt.fleet.model.MaintenanceRecord;
import com.feelt.fleet.model.MaintenanceStatus;
import com.feelt.fleet.repository.MaintenanceRecordRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    private final MaintenanceRecordRepository maintenanceRepository;

    public MaintenanceController(MaintenanceRecordRepository maintenanceRepository) {
        this.maintenanceRepository = maintenanceRepository;
    }

    @GetMapping
    public List<MaintenanceRecord> all() { return maintenanceRepository.findAll(); }

    @GetMapping("/page")
    public Page<MaintenanceRecord> page(@RequestParam(required = false) MaintenanceStatus status, Pageable pageable) {
        return status == null ? maintenanceRepository.findAll(pageable) : maintenanceRepository.findByStatus(status, pageable);
    }

    @GetMapping("/{id}")
    public MaintenanceRecord one(@PathVariable Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance record not found: " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MaintenanceRecord create(@Valid @RequestBody MaintenanceRecord record) {
        if (record.getStatus() == null) {
            record.setStatus(MaintenanceStatus.SCHEDULED);
        }
        return maintenanceRepository.save(record);
    }

    @PutMapping("/{id}")
    public MaintenanceRecord update(@PathVariable Long id, @Valid @RequestBody MaintenanceRecord updated) {
        MaintenanceRecord record = one(id);
        record.setVehicle(updated.getVehicle());
        record.setServiceType(updated.getServiceType());
        record.setDescription(updated.getDescription());
        record.setSpareParts(updated.getSpareParts());
        record.setCost(updated.getCost());
        record.setServiceDate(updated.getServiceDate());
        record.setNextServiceDate(updated.getNextServiceDate());
        record.setStatus(updated.getStatus() == null ? MaintenanceStatus.SCHEDULED : updated.getStatus());
        return maintenanceRepository.save(record);
    }

    @PatchMapping("/{id}/status/{status}")
    public MaintenanceRecord status(@PathVariable Long id, @PathVariable MaintenanceStatus status) {
        MaintenanceRecord record = one(id);
        record.setStatus(status);
        return maintenanceRepository.save(record);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!maintenanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Maintenance record not found: " + id);
        }
        maintenanceRepository.deleteById(id);
    }
}
