package com.feelt.fleet.repository;

import com.feelt.fleet.model.GpsTracking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GpsTrackingRepository extends JpaRepository<GpsTracking, Long> {
    List<GpsTracking> findByVehicleIdOrderByRecordedAtDesc(Long vehicleId, Pageable pageable);
}
