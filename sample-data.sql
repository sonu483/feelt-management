USE fleet_management;

INSERT INTO vehicle (
    license_plate,
    model,
    capacity_kg,
    status,
    last_maintenance_date,
    next_maintenance_date,
    current_latitude,
    current_longitude
) VALUES
('DL01AB1234', 'Tata Ace EV', 750, 'AVAILABLE', '2026-05-10', '2026-08-10', 28.6139, 77.2090),
('DL02CD5678', 'Mahindra Bolero Pickup', 1500, 'AVAILABLE', '2026-04-20', '2026-07-20', 28.6200, 77.2300),
('HR26EF9012', 'Ashok Leyland Dost', 1250, 'IN_MAINTENANCE', '2026-03-15', '2026-06-28', 28.4595, 77.0266);

INSERT INTO driver (
    full_name,
    license_number,
    license_valid_until,
    shift_hours,
    status,
    phone
) VALUES
('Rahul Sharma', 'DL-DRV-1001', '2028-12-31', 8, 'AVAILABLE', '9876543210'),
('Amit Verma', 'DL-DRV-1002', '2027-09-30', 9, 'AVAILABLE', '9876543211'),
('Suresh Yadav', 'HR-DRV-1003', '2026-11-15', 8, 'OFF_DUTY', '9876543212');

INSERT INTO delivery_task (
    customer_name,
    address,
    latitude,
    longitude,
    package_weight_kg,
    time_window_start,
    time_window_end,
    sequence_number,
    status,
    route_id
) VALUES
('Nisha Retail Store', 'Karol Bagh', 28.6517, 77.1907, 35, '2026-06-26 09:00:00', '2026-06-26 12:00:00', NULL, 'UNASSIGNED', NULL),
('Om Electronics', 'Noida Sector 62', 28.6270, 77.3750, 50, '2026-06-26 11:00:00', '2026-06-26 15:00:00', NULL, 'UNASSIGNED', NULL),
('Cyber Hub Cafe', 'Gurugram Cyber City', 28.4950, 77.0890, 42, '2026-06-26 14:00:00', '2026-06-26 18:00:00', NULL, 'UNASSIGNED', NULL);
