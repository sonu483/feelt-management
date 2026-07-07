package com.feelt.fleet.repository;

import com.feelt.fleet.model.NotificationMessage;
import com.feelt.fleet.model.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationMessageRepository extends JpaRepository<NotificationMessage, Long> {
    Page<NotificationMessage> findByStatus(NotificationStatus status, Pageable pageable);
}
