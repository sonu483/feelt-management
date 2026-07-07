package com.feelt.fleet.repository;

import com.feelt.fleet.model.Vehicle;
import com.feelt.fleet.model.VehicleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByStatus(VehicleStatus status);

    Page<Vehicle> findByStatus(VehicleStatus status, Pageable pageable);

    Page<Vehicle> findByLicensePlateContainingIgnoreCaseOrModelContainingIgnoreCase(
            String licensePlate,
            String model,
            Pageable pageable
    );
}
