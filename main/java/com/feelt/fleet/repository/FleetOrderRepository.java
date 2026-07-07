package com.feelt.fleet.repository;

import com.feelt.fleet.model.FleetOrder;
import com.feelt.fleet.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface FleetOrderRepository extends JpaRepository<FleetOrder, Long> {
    Page<FleetOrder> findByStatus(OrderStatus status, Pageable pageable);
    Page<FleetOrder> findByOrderNumberContainingIgnoreCaseOrPickupAddressContainingIgnoreCaseOrDropAddressContainingIgnoreCase(
            String orderNumber,
            String pickupAddress,
            String dropAddress,
            Pageable pageable
    );

    @Query("select coalesce(sum(o.orderValue), 0) from FleetOrder o where o.status <> com.feelt.fleet.model.OrderStatus.CANCELLED")
    BigDecimal totalOrderValue();
}
