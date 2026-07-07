package com.feelt.fleet.controller;

import com.feelt.fleet.exception.ResourceNotFoundException;
import com.feelt.fleet.model.DeliveryStatus;
import com.feelt.fleet.model.DeliveryTask;
import com.feelt.fleet.repository.DeliveryTaskRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryTaskController {

    private final DeliveryTaskRepository deliveryTaskRepository;

    public DeliveryTaskController(DeliveryTaskRepository deliveryTaskRepository) {
        this.deliveryTaskRepository = deliveryTaskRepository;
    }

    @GetMapping
    public List<DeliveryTask> getDeliveries() {
        return deliveryTaskRepository.findAll();
    }

    @GetMapping("/page")
    public Page<DeliveryTask> getDeliveriesPage(
            @RequestParam(required = false) DeliveryStatus status,
            @RequestParam(defaultValue = "") String search,
            Pageable pageable
    ) {
        if (status != null) {
            return deliveryTaskRepository.findByStatus(status, pageable);
        }
        if (!search.isBlank()) {
            return deliveryTaskRepository.findByCustomerNameContainingIgnoreCaseOrAddressContainingIgnoreCase(
                    search,
                    search,
                    pageable
            );
        }
        return deliveryTaskRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public DeliveryTask getDelivery(@PathVariable Long id) {
        return deliveryTaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery task not found: " + id));
    }

    @GetMapping("/status/{status}")
    public List<DeliveryTask> getDeliveriesByStatus(@PathVariable DeliveryStatus status) {
        return deliveryTaskRepository.findByStatus(status);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryTask createDelivery(@Valid @RequestBody DeliveryTask deliveryTask) {
        if (deliveryTask.getStatus() == null) {
            deliveryTask.setStatus(DeliveryStatus.UNASSIGNED);
        }
        return deliveryTaskRepository.save(deliveryTask);
    }

    @PutMapping("/{id}")
    public DeliveryTask updateDelivery(@PathVariable Long id, @Valid @RequestBody DeliveryTask updatedTask) {
        DeliveryTask task = deliveryTaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery task not found: " + id));
        task.setCustomerName(updatedTask.getCustomerName());
        task.setAddress(updatedTask.getAddress());
        task.setLatitude(updatedTask.getLatitude());
        task.setLongitude(updatedTask.getLongitude());
        task.setPackageWeightKg(updatedTask.getPackageWeightKg());
        task.setTimeWindowStart(updatedTask.getTimeWindowStart());
        task.setTimeWindowEnd(updatedTask.getTimeWindowEnd());
        task.setSequenceNumber(updatedTask.getSequenceNumber());
        task.setStatus(updatedTask.getStatus() == null ? DeliveryStatus.UNASSIGNED : updatedTask.getStatus());
        return deliveryTaskRepository.save(task);
    }

    @PatchMapping("/{id}/status/{status}")
    public DeliveryTask updateStatus(@PathVariable Long id, @PathVariable DeliveryStatus status) {
        DeliveryTask task = deliveryTaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery task not found: " + id));
        task.setStatus(status);
        return deliveryTaskRepository.save(task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDelivery(@PathVariable Long id) {
        if (!deliveryTaskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Delivery task not found: " + id);
        }
        deliveryTaskRepository.deleteById(id);
    }
}
