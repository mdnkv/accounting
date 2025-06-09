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

CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    description TEXT NOT NULL,
    transaction_date DATE NOT NULL,
    currency VARCHAR(3) NOT NULL CHECK(currency IN ('EUR')),
    CONSTRAINT fk_transaction_organization FOREIGN KEY (organization_id)
        REFERENCES organizations(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transaction_lines (
    id BIGINT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    transaction_id BIGINT NOT NULL,
    debit_amount DECIMAL(12,2) NOT NULL CHECK (debit_amount >= 0.0),
    credit_amount DECIMAL(12,2) NOT NULL CHECK (credit_amount >= 0.0),
    CONSTRAINT fk_transaction_line_account FOREIGN KEY (account_id)
        REFERENCES accounts(id) ON DELETE CASCADE,
    CONSTRAINT fk_transaction_line_transaction FOREIGN KEY (transaction_id)
        REFERENCES transactions(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_accounts (
    id BIGINT PRIMARY KEY,
    keycloak_id VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);

CREATE TYPE ROLE_TYPE AS ENUM ('OWNER', 'ADMINISTRATOR', 'ACCOUNTANT', 'USER');

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    organization_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL,
    role_type ROLE_TYPE NOT NULL,
    UNIQUE (user_id, organization_id),
    CONSTRAINT fk_role_organization FOREIGN KEY (organization_id)
        REFERENCES organizations(id) ON DELETE CASCADE,
    CONSTRAINT fk_role_user FOREIGN KEY (user_id)
        REFERENCES user_accounts(id) ON DELETE CASCADE
);
