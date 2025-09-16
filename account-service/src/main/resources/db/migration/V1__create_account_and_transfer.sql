CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS accounts (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  balance NUMERIC(19,2) DEFAULT 0,
  created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE IF NOT EXISTS transfers (
  id BIGSERIAL PRIMARY KEY,
  transfer_id VARCHAR(100),
  from_account_id BIGINT NOT NULL,
  to_account_id BIGINT NOT NULL,
  amount NUMERIC(19,2) NOT NULL,
  status VARCHAR(50) NOT NULL,
  created_at TIMESTAMP DEFAULT now()
);
