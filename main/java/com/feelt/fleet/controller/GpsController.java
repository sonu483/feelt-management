package com.feelt.fleet.controller;

import com.feelt.fleet.exception.ResourceNotFoundException;
import com.feelt.fleet.model.GpsTracking;
import com.feelt.fleet.repository.GpsTrackingRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gps")
public class GpsController {

    private final GpsTrackingRepository gpsRepository;

    public GpsController(GpsTrackingRepository gpsRepository) {
        this.gpsRepository = gpsRepository;
    }

    @GetMapping
    public List<GpsTracking> all() {
        return gpsRepository.findAll();
    }

    @GetMapping("/{id}")
    public GpsTracking one(@PathVariable Long id) {
        return gpsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GPS record not found: " + id));
    }

    @GetMapping("/vehicle/{vehicleId}/latest")
    public List<GpsTracking> latestByVehicle(@PathVariable Long vehicleId,
                                             @RequestParam(defaultValue = "10") int limit) {
        return gpsRepository.findByVehicleIdOrderByRecordedAtDesc(vehicleId, PageRequest.of(0, Math.max(1, limit)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GpsTracking create(@Valid @RequestBody GpsTracking gpsTracking) {
        return gpsRepository.save(gpsTracking);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!gpsRepository.existsById(id)) {
            throw new ResourceNotFoundException("GPS record not found: " + id);
        }
        gpsRepository.deleteById(id);
    }
}
