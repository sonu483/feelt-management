package com.feelt.fleet.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceReminderScheduler {

    @Scheduled(cron = "0 0 9 * * *")
    public void scanUpcomingMaintenance() {
        // Hook for future email/SMS reminders. Kept lightweight so local development remains fast.
    }
}
