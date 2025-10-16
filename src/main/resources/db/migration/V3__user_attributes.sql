ALTER TABLE user_accounts ADD COLUMN is_superuser BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE user_accounts ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT true;
