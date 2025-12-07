\c product_management_db

GRANT CREATE ON SCHEMA public TO product_manager_user;
GRANT USAGE  ON SCHEMA public TO product_manager_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO product_manager_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO product_manager_user;
