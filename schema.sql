CREATE DATABASE IF NOT EXISTS fleet_management;
USE fleet_management;

-- Spring Data JPA can create the tables automatically.
-- This file is provided for project submission and MySQL setup clarity.

CREATE TABLE IF NOT EXISTS vehicle (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    license_plate VARCHAR(50) NOT NULL UNIQUE,
    model VARCHAR(100) NOT NULL,
    capacity_kg INT NOT NULL,
    status VARCHAR(40) NOT NULL,
    last_maintenance_date DATE,
    next_maintenance_date DATE,
    current_latitude DOUBLE,
    current_longitude DOUBLE
);

CREATE TABLE IF NOT EXISTS driver (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(120) NOT NULL,
    license_number VARCHAR(80) NOT NULL UNIQUE,
    license_valid_until DATE NOT NULL,
    shift_hours INT NOT NULL,
    status VARCHAR(40) NOT NULL,
    phone VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS dispatch_route (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    vehicle_id BIGINT NOT NULL,
    driver_id BIGINT NOT NULL,
    dispatched_at DATETIME NOT NULL,
    total_distance_km DOUBLE,
    estimated_fuel_litres DOUBLE,
    CONSTRAINT fk_dispatch_route_vehicle
        FOREIGN KEY (vehicle_id) REFERENCES vehicle(id),
    CONSTRAINT fk_dispatch_route_driver
        FOREIGN KEY (driver_id) REFERENCES driver(id)
);

CREATE TABLE IF NOT EXISTS delivery_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(120) NOT NULL,
    address VARCHAR(255) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    package_weight_kg INT,
    time_window_start DATETIME,
    time_window_end DATETIME,
    sequence_number INT,
    status VARCHAR(40) NOT NULL,
    route_id BIGINT,
    CONSTRAINT fk_delivery_task_route
        FOREIGN KEY (route_id) REFERENCES dispatch_route(id)
);
