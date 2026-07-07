package com.feelt.fleet.repository;

import com.feelt.fleet.model.FuelRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface FuelRecordRepository extends JpaRepository<FuelRecord, Long> {
    @Query("select coalesce(sum(f.cost), 0) from FuelRecord f")
    BigDecimal totalFuelCost();
}
