# API Samples

## Create Vehicle

```http
POST http://localhost:8080/api/vehicles
Content-Type: application/json
```

```json
{
  "licensePlate": "DL03GH1111",
  "model": "Eicher Pro 2049",
  "capacityKg": 2500,
  "status": "AVAILABLE",
  "lastMaintenanceDate": "2026-06-01",
  "nextMaintenanceDate": "2026-09-01",
  "currentLatitude": 28.6139,
  "currentLongitude": 77.209
}
```

## Create Driver

```http
POST http://localhost:8080/api/drivers
Content-Type: application/json
```

```json
{
  "fullName": "Nitin Kumar",
  "licenseNumber": "DL-DRV-2001",
  "licenseValidUntil": "2029-01-01",
  "shiftHours": 8,
  "status": "AVAILABLE",
  "phone": "9876500000"
}
```

## Create Delivery Task

```http
POST http://localhost:8080/api/deliveries
Content-Type: application/json
```

```json
{
  "customerName": "City Medical Store",
  "address": "Lajpat Nagar",
  "latitude": 28.5677,
  "longitude": 77.2433,
  "packageWeightKg": 25,
  "timeWindowStart": "2026-06-26T10:00:00",
  "timeWindowEnd": "2026-06-26T13:00:00",
  "status": "UNASSIGNED"
}
```

## Optimize Route

```http
POST http://localhost:8080/api/routes/optimize
Content-Type: application/json
```

```json
{
  "depot": {
    "address": "Delhi Depot",
    "latitude": 28.6139,
    "longitude": 77.209
  },
  "stops": [
    {
      "address": "Noida Sector 62",
      "latitude": 28.627,
      "longitude": 77.375
    },
    {
      "address": "Gurugram Cyber City",
      "latitude": 28.495,
      "longitude": 77.089
    },
    {
      "address": "Karol Bagh",
      "latitude": 28.6517,
      "longitude": 77.1907
    }
  ]
}
```

## Dispatch Route

```http
POST http://localhost:8080/api/routes/dispatch
Content-Type: application/json
```

```json
{
  "vehicleId": 1,
  "driverId": 1,
  "depot": {
    "address": "Delhi Depot",
    "latitude": 28.6139,
    "longitude": 77.209
  },
  "deliveries": [
    {
      "customerName": "ABC Retail",
      "address": "Karol Bagh",
      "latitude": 28.6517,
      "longitude": 77.1907,
      "packageWeightKg": 120
    },
    {
      "customerName": "Metro Foods",
      "address": "Noida Sector 62",
      "latitude": 28.627,
      "longitude": 77.375,
      "packageWeightKg": 200
    }
  ]
}
```

## Update Status

```http
PATCH http://localhost:8080/api/vehicles/1/status/AVAILABLE
PATCH http://localhost:8080/api/drivers/1/status/AVAILABLE
PATCH http://localhost:8080/api/deliveries/1/status/DELIVERED
```
