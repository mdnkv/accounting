CREATE TABLE IF NOT EXISTS account_categories (
    id BIGINT PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    account_type ACCOUNT_TYPE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (organization_id, name),
    CONSTRAINT fk_account_category_organization FOREIGN KEY (organization_id)
        REFERENCES organizations(id) ON DELETE CASCADE
);

ALTER TABLE accounts ADD COLUMN account_category_id BIGINT;
ALTER TABLE accounts ADD CONSTRAINT fk_account_account_category
    FOREIGN KEY (account_category_id) REFERENCES account_categories ON DELETE SET NULL;