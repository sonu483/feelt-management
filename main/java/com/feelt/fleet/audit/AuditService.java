package com.feelt.fleet.audit;

import com.feelt.fleet.model.AuditLog;
import com.feelt.fleet.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void record(String actor, String moduleName, String action, Long recordId, String details) {
        AuditLog log = new AuditLog();
        log.setActor(actor);
        log.setModuleName(moduleName);
        log.setAction(action);
        log.setRecordId(recordId);
        log.setDetails(details);
        auditLogRepository.save(log);
    }
}
