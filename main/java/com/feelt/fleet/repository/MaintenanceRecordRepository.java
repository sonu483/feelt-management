package com.feelt.fleet.repository;

import com.feelt.fleet.model.MaintenanceRecord;
import com.feelt.fleet.model.MaintenanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {
    Page<MaintenanceRecord> findByStatus(MaintenanceStatus status, Pageable pageable);

    @Query("select coalesce(sum(m.cost), 0) from MaintenanceRecord m")
    BigDecimal totalMaintenanceCost();
}
