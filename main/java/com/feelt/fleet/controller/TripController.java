package com.feelt.fleet.controller;

import com.feelt.fleet.exception.ResourceNotFoundException;
import com.feelt.fleet.model.Trip;
import com.feelt.fleet.model.TripStatus;
import com.feelt.fleet.repository.TripRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripRepository tripRepository;

    public TripController(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @GetMapping
    public List<Trip> all() {
        return tripRepository.findAll();
    }

    @GetMapping("/page")
    public Page<Trip> page(@RequestParam(required = false) TripStatus status,
                           @RequestParam(defaultValue = "") String search,
                           Pageable pageable) {
        if (status != null) {
            return tripRepository.findByStatus(status, pageable);
        }
        if (!search.isBlank()) {
            return tripRepository.findByTripCodeContainingIgnoreCaseOrOriginContainingIgnoreCaseOrDestinationContainingIgnoreCase(
                    search, search, search, pageable);
        }
        return tripRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Trip one(@PathVariable Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found: " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Trip create(@Valid @RequestBody Trip trip) {
        if (trip.getStatus() == null) {
            trip.setStatus(TripStatus.PLANNED);
        }
        return tripRepository.save(trip);
    }

    @PutMapping("/{id}")
    public Trip update(@PathVariable Long id, @Valid @RequestBody Trip updated) {
        Trip trip = one(id);
        trip.setTripCode(updated.getTripCode());
        trip.setVehicle(updated.getVehicle());
        trip.setDriver(updated.getDriver());
        trip.setOrder(updated.getOrder());
        trip.setOrigin(updated.getOrigin());
        trip.setDestination(updated.getDestination());
        trip.setDistanceKm(updated.getDistanceKm());
        trip.setRevenue(updated.getRevenue());
        trip.setExpense(updated.getExpense());
        trip.setStartTime(updated.getStartTime());
        trip.setEndTime(updated.getEndTime());
        trip.setStatus(updated.getStatus() == null ? TripStatus.PLANNED : updated.getStatus());
        return tripRepository.save(trip);
    }

    @PatchMapping("/{id}/status/{status}")
    public Trip status(@PathVariable Long id, @PathVariable TripStatus status) {
        Trip trip = one(id);
        trip.setStatus(status);
        return tripRepository.save(trip);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!tripRepository.existsById(id)) {
            throw new ResourceNotFoundException("Trip not found: " + id);
        }
        tripRepository.deleteById(id);
    }
}
