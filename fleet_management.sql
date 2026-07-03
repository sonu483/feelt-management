CREATE DATABASE IF NOT EXISTS fleet_management;
USE fleet_management;

-- Import order for MySQL submissions:
-- 1) backend/src/main/resources/db/migration/V1__initial_schema.sql
-- 2) backend/src/main/resources/db/migration/V2__users_roles.sql
-- 3) backend/src/main/resources/db/migration/V3__fleet_business_modules.sql
--
-- Local development does not need manual SQL. The default app profile uses H2:
-- backend/data/fleet_management.mv.db
