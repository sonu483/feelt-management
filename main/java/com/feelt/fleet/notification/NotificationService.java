package com.feelt.fleet.notification;

import com.feelt.fleet.model.NotificationMessage;
import com.feelt.fleet.model.NotificationStatus;
import com.feelt.fleet.repository.NotificationMessageRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationMessageRepository notificationRepository;

    public NotificationService(NotificationMessageRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationMessage queue(NotificationMessage message) {
        message.setStatus(NotificationStatus.PENDING);
        return notificationRepository.save(message);
    }
}
