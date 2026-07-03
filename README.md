# Fleet Management and Route Optimization Backend

## Eclipse Import

1. Open Eclipse.
2. Go to `File > Import > Maven > Existing Maven Projects`.
3. Select this folder: `backend`.
4. Finish import and wait for Maven dependencies.
5. Open `src/main/resources/application.properties`.
6. Update MySQL username/password if needed.
7. Run `FleetManagementApplication.java` as Java Application.

## MySQL

Default database is `fleet_management`. Spring Boot will create/update tables automatically.

```sql
CREATE DATABASE fleet_management;
```

## Swagger

After backend starts:

```text
http://localhost:8080/swagger-ui.html
```

## Production-style Configuration

The backend supports environment variables, so credentials do not need to be hard-coded:

```text
DB_URL=jdbc:mysql://localhost:3306/fleet_management
DB_USERNAME=root
DB_PASSWORD=root
DDL_AUTO=update
SHOW_SQL=false
CORS_ALLOWED_ORIGINS=http://localhost:5173,http://127.0.0.1:5173
```

Health and API metadata:

```text
GET /api
GET /actuator/health
GET /actuator/metrics
GET /api-docs
```

## Main APIs

- `POST /api/vehicles`
- `GET /api/vehicles`
- `GET /api/vehicles/page?page=0&size=10&sort=id,desc`
- `GET /api/vehicles/page?search=truck`
- `GET /api/vehicles/page?status=AVAILABLE`
- `GET /api/vehicles/{id}`
- `PATCH /api/vehicles/{id}/status/{status}`
- `DELETE /api/vehicles/{id}`
- `POST /api/drivers`
- `GET /api/drivers`
- `GET /api/drivers/page?page=0&size=10&sort=id,desc`
- `GET /api/drivers/page?search=rahul`
- `GET /api/drivers/page?status=AVAILABLE`
- `GET /api/drivers/{id}`
- `PATCH /api/drivers/{id}/status/{status}`
- `DELETE /api/drivers/{id}`
- `POST /api/deliveries`
- `GET /api/deliveries`
- `GET /api/deliveries/page?page=0&size=10&sort=id,desc`
- `GET /api/deliveries/page?search=delhi`
- `GET /api/deliveries/page?status=UNASSIGNED`
- `GET /api/deliveries/{id}`
- `POST /api/routes/optimize`
- `POST /api/routes/dispatch`
- `GET /api/routes`
- `GET /api/routes/page?page=0&size=10&sort=id,desc`
- `GET /api/routes/{id}`
- `PATCH /api/deliveries/{id}/status/{status}`

## Industry Features

- Spring Boot layered backend with controllers, services, repositories, DTOs, and exception handling.
- MySQL JPA persistence with validation and unique database constraints.
- Swagger/OpenAPI documentation.
- Actuator health, info, and metrics endpoints.
- Environment-based database and CORS configuration.
- Global request ID propagation through `X-Request-Id`.
- Structured API error responses with timestamp, path, and request ID.
- Paginated and searchable list endpoints for scalable data access.
- Entity audit timestamps through Hibernate `createdAt` and `updatedAt`.
- Route optimization and dispatch workflow with transactional consistency.

## Route Optimization Sample

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
    }
  ]
}
```
