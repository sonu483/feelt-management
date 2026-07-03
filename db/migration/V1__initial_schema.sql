CREATE TABLE vehicle (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    license_plate VARCHAR(50) NOT NULL UNIQUE,
    model VARCHAR(100) NOT NULL,
    capacity_kg INT NOT NULL,
    status VARCHAR(40) NOT NULL,
    last_maintenance_date DATE,
    next_maintenance_date DATE,
    current_latitude DOUBLE,
    current_longitude DOUBLE,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    INDEX idx_vehicle_status (status)
);

CREATE TABLE driver (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(120) NOT NULL,
    license_number VARCHAR(80) NOT NULL UNIQUE,
    license_valid_until DATE NOT NULL,
    shift_hours INT NOT NULL,
    status VARCHAR(40) NOT NULL,
    phone VARCHAR(30),
    created_at DATETIME(6),
    updated_at DATETIME(6),
    INDEX idx_driver_status (status)
);

CREATE TABLE dispatch_route (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    vehicle_id BIGINT NOT NULL,
    driver_id BIGINT NOT NULL,
    dispatched_at DATETIME(6) NOT NULL,
    total_distance_km DOUBLE,
    estimated_fuel_litres DOUBLE,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    CONSTRAINT fk_route_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(id),
    CONSTRAINT fk_route_driver FOREIGN KEY (driver_id) REFERENCES driver(id),
    INDEX idx_route_dispatched_at (dispatched_at)
);

CREATE TABLE delivery_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(120) NOT NULL,
    address VARCHAR(255) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    package_weight_kg INT,
    time_window_start DATETIME(6),
    time_window_end DATETIME(6),
    sequence_number INT,
    status VARCHAR(40) NOT NULL,
    route_id BIGINT,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    CONSTRAINT fk_delivery_route FOREIGN KEY (route_id) REFERENCES dispatch_route(id),
    INDEX idx_delivery_status (status),
    INDEX idx_delivery_route (route_id)
);
