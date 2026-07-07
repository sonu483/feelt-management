package com.feelt.fleet.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dispatch_route")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Vehicle vehicle;

    @ManyToOne(optional = false)
    private Driver driver;

    @Column(nullable = false)
    private LocalDateTime dispatchedAt;

    private Double totalDistanceKm;
    private Double estimatedFuelLitres;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    @OrderBy("sequenceNumber ASC")
    private List<DeliveryTask> deliveryTasks = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public LocalDateTime getDispatchedAt() {
        return dispatchedAt;
    }

    public void setDispatchedAt(LocalDateTime dispatchedAt) {
        this.dispatchedAt = dispatchedAt;
    }

    public Double getTotalDistanceKm() {
        return totalDistanceKm;
    }

    public void setTotalDistanceKm(Double totalDistanceKm) {
        this.totalDistanceKm = totalDistanceKm;
    }

    public Double getEstimatedFuelLitres() {
        return estimatedFuelLitres;
    }

    public void setEstimatedFuelLitres(Double estimatedFuelLitres) {
        this.estimatedFuelLitres = estimatedFuelLitres;
    }

    public List<DeliveryTask> getDeliveryTasks() {
        return deliveryTasks;
    }

    public void setDeliveryTasks(List<DeliveryTask> deliveryTasks) {
        this.deliveryTasks = deliveryTasks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
