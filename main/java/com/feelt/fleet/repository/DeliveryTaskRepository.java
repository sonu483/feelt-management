package com.feelt.fleet.repository;

import com.feelt.fleet.model.DeliveryStatus;
import com.feelt.fleet.model.DeliveryTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryTaskRepository extends JpaRepository<DeliveryTask, Long> {
    List<DeliveryTask> findByStatus(DeliveryStatus status);

    Page<DeliveryTask> findByStatus(DeliveryStatus status, Pageable pageable);

    Page<DeliveryTask> findByCustomerNameContainingIgnoreCaseOrAddressContainingIgnoreCase(
            String customerName,
            String address,
            Pageable pageable
    );
}
