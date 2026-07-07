package com.feelt.fleet.repository;

import com.feelt.fleet.model.Trip;
import com.feelt.fleet.model.TripStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface TripRepository extends JpaRepository<Trip, Long> {
    Page<Trip> findByStatus(TripStatus status, Pageable pageable);
    Page<Trip> findByTripCodeContainingIgnoreCaseOrOriginContainingIgnoreCaseOrDestinationContainingIgnoreCase(
            String tripCode,
            String origin,
            String destination,
            Pageable pageable
    );

    long countByStatus(TripStatus status);

    @Query("select coalesce(sum(t.revenue), 0) from Trip t where t.status = com.feelt.fleet.model.TripStatus.COMPLETED")
    BigDecimal totalCompletedRevenue();

    @Query("select coalesce(sum(t.expense), 0) from Trip t")
    BigDecimal totalTripExpense();
}
