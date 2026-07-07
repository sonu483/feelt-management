package com.feelt.fleet.controller;

import com.feelt.fleet.exception.ResourceNotFoundException;
import com.feelt.fleet.model.NotificationMessage;
import com.feelt.fleet.model.NotificationStatus;
import com.feelt.fleet.repository.NotificationMessageRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationMessageRepository notificationRepository;

    public NotificationController(NotificationMessageRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping
    public List<NotificationMessage> all() { return notificationRepository.findAll(); }

    @GetMapping("/page")
    public Page<NotificationMessage> page(@RequestParam(required = false) NotificationStatus status, Pageable pageable) {
        return status == null ? notificationRepository.findAll(pageable) : notificationRepository.findByStatus(status, pageable);
    }

    @GetMapping("/{id}")
    public NotificationMessage one(@PathVariable Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationMessage create(@Valid @RequestBody NotificationMessage notification) {
        if (notification.getStatus() == null) {
            notification.setStatus(NotificationStatus.PENDING);
        }
        return notificationRepository.save(notification);
    }

    @PatchMapping("/{id}/status/{status}")
    public NotificationMessage status(@PathVariable Long id, @PathVariable NotificationStatus status) {
        NotificationMessage notification = one(id);
        notification.setStatus(status);
        return notificationRepository.save(notification);
    }
}
