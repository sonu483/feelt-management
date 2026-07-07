package com.feelt.fleet.controller;

import com.feelt.fleet.exception.ResourceNotFoundException;
import com.feelt.fleet.model.FleetOrder;
import com.feelt.fleet.model.OrderStatus;
import com.feelt.fleet.repository.FleetOrderRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final FleetOrderRepository orderRepository;

    public OrderController(FleetOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public List<FleetOrder> all() {
        return orderRepository.findAll();
    }

    @GetMapping("/page")
    public Page<FleetOrder> page(@RequestParam(required = false) OrderStatus status,
                                 @RequestParam(defaultValue = "") String search,
                                 Pageable pageable) {
        if (status != null) {
            return orderRepository.findByStatus(status, pageable);
        }
        if (!search.isBlank()) {
            return orderRepository.findByOrderNumberContainingIgnoreCaseOrPickupAddressContainingIgnoreCaseOrDropAddressContainingIgnoreCase(
                    search, search, search, pageable);
        }
        return orderRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public FleetOrder one(@PathVariable Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FleetOrder create(@Valid @RequestBody FleetOrder order) {
        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.CREATED);
        }
        return orderRepository.save(order);
    }

    @PutMapping("/{id}")
    public FleetOrder update(@PathVariable Long id, @Valid @RequestBody FleetOrder updated) {
        FleetOrder order = one(id);
        order.setOrderNumber(updated.getOrderNumber());
        order.setCustomer(updated.getCustomer());
        order.setPickupAddress(updated.getPickupAddress());
        order.setDropAddress(updated.getDropAddress());
        order.setPickupLatitude(updated.getPickupLatitude());
        order.setPickupLongitude(updated.getPickupLongitude());
        order.setDropLatitude(updated.getDropLatitude());
        order.setDropLongitude(updated.getDropLongitude());
        order.setWeightKg(updated.getWeightKg());
        order.setOrderValue(updated.getOrderValue());
        order.setStatus(updated.getStatus() == null ? OrderStatus.CREATED : updated.getStatus());
        order.setExpectedDeliveryAt(updated.getExpectedDeliveryAt());
        return orderRepository.save(order);
    }

    @PatchMapping("/{id}/status/{status}")
    public FleetOrder status(@PathVariable Long id, @PathVariable OrderStatus status) {
        FleetOrder order = one(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found: " + id);
        }
        orderRepository.deleteById(id);
    }
}
