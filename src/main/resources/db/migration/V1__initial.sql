CREATE TABLE IF NOT EXISTS organizations (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    currency VARCHAR(3) NOT NULL CHECK(currency IN ('EUR'))
);

CREATE TYPE ACCOUNT_TYPE AS ENUM ('ASSET', 'LIABILITY', 'EQUITY', 'INCOME', 'EXPENSE');

CREATE TABLE IF NOT EXISTS accounts (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(20) NOT NULL,
    account_type ACCOUNT_TYPE NOT NULL,
    organization_id BIGINT NOT NULL,
    UNIQUE (organization_id, code),
    CONSTRAINT fk_account_organization FOREIGN KEY (organization_id)
        REFERENCES organizations(id) ON DELETE CASCADE
);