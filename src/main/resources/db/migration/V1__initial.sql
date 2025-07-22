CREATE TABLE IF NOT EXISTS organizations (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TYPE ACCOUNT_TYPE AS ENUM ('ASSET', 'LIABILITY', 'EQUITY', 'INCOME', 'EXPENSE');

CREATE TABLE IF NOT EXISTS accounts (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(20) NOT NULL,
    account_type ACCOUNT_TYPE NOT NULL,
    organization_id BIGINT NOT NULL,
    is_deprecated BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE (organization_id, code),
    CONSTRAINT fk_account_organization FOREIGN KEY (organization_id)
        REFERENCES organizations(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS user_accounts (
    id BIGINT PRIMARY KEY,
    keycloak_id VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS currencies (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(3) NOT NULL,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    organization_id BIGINT NOT NULL,
    UNIQUE (organization_id, code),
    CONSTRAINT fk_currency_organization FOREIGN KEY (organization_id)
        REFERENCES organizations(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    currency_id BIGINT NOT NULL,
    description TEXT NOT NULL,
    transaction_date DATE NOT NULL,
    is_draft BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_transaction_organization FOREIGN KEY (organization_id)
        REFERENCES organizations(id) ON DELETE CASCADE,
    CONSTRAINT fk_transaction_currency FOREIGN KEY (currency_id)
        REFERENCES currencies(id) ON DELETE SET NULL
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

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    UNIQUE (organization_id, name),
    CONSTRAINT fk_role_organization FOREIGN KEY (organization_id)
        REFERENCES organizations(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS authorities (
    id BIGINT PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    UNIQUE (organization_id, name),
    CONSTRAINT fk_authority_organization FOREIGN KEY (organization_id)
        REFERENCES organizations(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS roles_authorities (
    id BIGSERIAL PRIMARY KEY,
    authority_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_roles_authorities_authority FOREIGN KEY (authority_id) REFERENCES authorities(id),
    CONSTRAINT fk_roles_authorities_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS organization_users (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    organization_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE (user_id, organization_id),
    CONSTRAINT fk_organization_user_organization FOREIGN KEY (organization_id)
        REFERENCES organizations(id) ON DELETE CASCADE,
    CONSTRAINT fk_organization_user_user FOREIGN KEY (user_id)
        REFERENCES user_accounts(id) ON DELETE CASCADE,
    CONSTRAINT fk_organization_user_role FOREIGN KEY (role_id)
        REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS invitations (
    id BIGINT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    organization_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    UNIQUE (email, organization_id),
    CONSTRAINT fk_invitation_organization FOREIGN KEY (organization_id)
        REFERENCES organizations(id) ON DELETE CASCADE,
    CONSTRAINT fk_invitation_role FOREIGN KEY (role_id)
        REFERENCES roles(id) ON DELETE CASCADE
);