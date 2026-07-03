CREATE TABLE customer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(160) UNIQUE,
    phone VARCHAR(30),
    company_name VARCHAR(160),
    billing_address VARCHAR(255),
    status VARCHAR(40) NOT NULL,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    INDEX idx_customer_status (status)
);

CREATE TABLE fleet_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_number VARCHAR(80) NOT NULL UNIQUE,
    customer_id BIGINT,
    pickup_address VARCHAR(255) NOT NULL,
    drop_address VARCHAR(255) NOT NULL,
    pickup_latitude DOUBLE,
    pickup_longitude DOUBLE,
    drop_latitude DOUBLE,
    drop_longitude DOUBLE,
    weight_kg DOUBLE,
    order_value DECIMAL(19, 2),
    status VARCHAR(40) NOT NULL,
    expected_delivery_at DATETIME(6),
    created_at DATETIME(6),
    updated_at DATETIME(6),
    CONSTRAINT fk_fleet_order_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    INDEX idx_fleet_order_status (status)
);

CREATE TABLE trip (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    trip_code VARCHAR(80) NOT NULL UNIQUE,
    vehicle_id BIGINT,
    driver_id BIGINT,
    order_id BIGINT,
    origin VARCHAR(255),
    destination VARCHAR(255),
    distance_km DOUBLE,
    revenue DECIMAL(19, 2),
    expense DECIMAL(19, 2),
    start_time DATETIME(6),
    end_time DATETIME(6),
    status VARCHAR(40) NOT NULL,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    CONSTRAINT fk_trip_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(id),
    CONSTRAINT fk_trip_driver FOREIGN KEY (driver_id) REFERENCES driver(id),
    CONSTRAINT fk_trip_order FOREIGN KEY (order_id) REFERENCES fleet_order(id),
    INDEX idx_trip_status (status)
);

CREATE TABLE fuel_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    vehicle_id BIGINT,
    litres DOUBLE NOT NULL,
    cost DECIMAL(19, 2) NOT NULL,
    odometer_km INT,
    fuel_station VARCHAR(160),
    fuel_date DATE,
    created_at DATETIME(6),
    CONSTRAINT fk_fuel_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(id)
);

CREATE TABLE maintenance_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    vehicle_id BIGINT,
    service_type VARCHAR(120) NOT NULL,
    description VARCHAR(255),
    spare_parts VARCHAR(255),
    cost DECIMAL(19, 2),
    service_date DATE,
    next_service_date DATE,
    status VARCHAR(40) NOT NULL,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    CONSTRAINT fk_maintenance_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(id),
    INDEX idx_maintenance_status (status)
);

CREATE TABLE gps_tracking (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    vehicle_id BIGINT,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    speed_kmph DOUBLE,
    geofence_alert BOOLEAN,
    recorded_at DATETIME(6),
    CONSTRAINT fk_gps_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(id),
    INDEX idx_gps_vehicle_time (vehicle_id, recorded_at)
);

CREATE TABLE notification_message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    recipient VARCHAR(160) NOT NULL,
    subject VARCHAR(200) NOT NULL,
    message VARCHAR(2000) NOT NULL,
    channel VARCHAR(40) NOT NULL,
    status VARCHAR(40) NOT NULL,
    created_at DATETIME(6),
    INDEX idx_notification_status (status)
);

CREATE TABLE audit_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    actor VARCHAR(160),
    action VARCHAR(120),
    module_name VARCHAR(120),
    record_id BIGINT,
    details VARCHAR(2000),
    created_at DATETIME(6),
    INDEX idx_audit_module_record (module_name, record_id)
);
