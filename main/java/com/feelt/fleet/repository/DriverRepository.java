package com.feelt.fleet.repository;

import com.feelt.fleet.model.Driver;
import com.feelt.fleet.model.DriverStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    List<Driver> findByStatus(DriverStatus status);

    Page<Driver> findByStatus(DriverStatus status, Pageable pageable);

    Page<Driver> findByFullNameContainingIgnoreCaseOrLicenseNumberContainingIgnoreCase(
            String fullName,
            String licenseNumber,
            Pageable pageable
    );
}
