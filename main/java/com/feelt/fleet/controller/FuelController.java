package com.feelt.fleet.controller;

import com.feelt.fleet.exception.ResourceNotFoundException;
import com.feelt.fleet.model.FuelRecord;
import com.feelt.fleet.repository.FuelRecordRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fuel")
public class FuelController {

    private final FuelRecordRepository fuelRepository;

    public FuelController(FuelRecordRepository fuelRepository) {
        this.fuelRepository = fuelRepository;
    }

    @GetMapping
    public List<FuelRecord> all() { return fuelRepository.findAll(); }

    @GetMapping("/page")
    public Page<FuelRecord> page(Pageable pageable) { return fuelRepository.findAll(pageable); }

    @GetMapping("/{id}")
    public FuelRecord one(@PathVariable Long id) {
        return fuelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fuel record not found: " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FuelRecord create(@Valid @RequestBody FuelRecord fuelRecord) { return fuelRepository.save(fuelRecord); }

    @PutMapping("/{id}")
    public FuelRecord update(@PathVariable Long id, @Valid @RequestBody FuelRecord updated) {
        FuelRecord record = one(id);
        record.setVehicle(updated.getVehicle());
        record.setLitres(updated.getLitres());
        record.setCost(updated.getCost());
        record.setOdometerKm(updated.getOdometerKm());
        record.setFuelStation(updated.getFuelStation());
        record.setFuelDate(updated.getFuelDate());
        return fuelRepository.save(record);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!fuelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fuel record not found: " + id);
        }
        fuelRepository.deleteById(id);
    }
}
