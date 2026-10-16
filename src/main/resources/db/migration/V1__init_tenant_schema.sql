-- V1__init_tenant_schema.sql
-- Tenant service initial schema per PMS-DB-001 §6

CREATE SCHEMA IF NOT EXISTS tenant;

CREATE TABLE tenant.tenants (
    id                                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_profile_id                     UUID,
    full_name                           VARCHAR(255) NOT NULL,
    phone_encrypted                     BYTEA,
    phone_hash                          VARCHAR(64),
    mpesa_phone_encrypted               BYTEA,
    mpesa_phone_hash                    VARCHAR(64),
    email                               VARCHAR(255),
    national_id_encrypted               BYTEA,
    national_id_hash                    VARCHAR(64) NOT NULL,
    date_of_birth                       DATE,
    emergency_contact_name              VARCHAR(255),
    emergency_contact_phone_encrypted   BYTEA,
    status                              VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at                          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by                          UUID,
    updated_by                          UUID,
    deleted_at                          TIMESTAMPTZ
);

CREATE INDEX ix_tenants_phone_hash ON tenant.tenants (phone_hash) WHERE deleted_at IS NULL;
CREATE INDEX ix_tenants_mpesa_hash ON tenant.tenants (mpesa_phone_hash) WHERE deleted_at IS NULL;
CREATE UNIQUE INDEX ux_tenants_national_id ON tenant.tenants (national_id_hash) WHERE deleted_at IS NULL;

CREATE TABLE tenant.leases (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id           UUID NOT NULL REFERENCES tenant.tenants(id) ON DELETE RESTRICT,
    unit_id             UUID NOT NULL,
    building_id         UUID NOT NULL,
    lease_number        VARCHAR(50) UNIQUE NOT NULL,
    start_date          DATE NOT NULL,
    end_date            DATE NOT NULL,
    monthly_rent        BIGINT NOT NULL CHECK (monthly_rent > 0),
    currency            VARCHAR(3) NOT NULL DEFAULT 'KES',
    deposit_amount      BIGINT NOT NULL DEFAULT 0,
    payment_due_day     SMALLINT NOT NULL DEFAULT 5 CHECK (payment_due_day BETWEEN 1 AND 28),
    payment_frequency   VARCHAR(20) NOT NULL DEFAULT 'MONTHLY',
    late_fee_policy     JSONB,
    status              VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    signed_at           TIMESTAMPTZ,
    move_in_date        DATE,
    move_out_date       DATE,
    move_out_notes      TEXT,
    terminated_reason   TEXT,
    previous_lease_id   UUID,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by          UUID,
    updated_by          UUID,
    deleted_at          TIMESTAMPTZ,
    CONSTRAINT ck_lease_dates CHECK (end_date > start_date)
);

CREATE INDEX ix_leases_tenant ON tenant.leases (tenant_id) WHERE deleted_at IS NULL;
CREATE INDEX ix_leases_unit ON tenant.leases (unit_id) WHERE deleted_at IS NULL;
CREATE INDEX ix_leases_status ON tenant.leases (status);

CREATE UNIQUE INDEX ux_leases_one_active_per_unit
    ON tenant.leases (unit_id)
    WHERE status = 'ACTIVE' AND deleted_at IS NULL;

CREATE TABLE tenant.deposits (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    lease_id            UUID NOT NULL REFERENCES tenant.leases(id) ON DELETE RESTRICT,
    tenant_id           UUID NOT NULL,
    amount              BIGINT NOT NULL CHECK (amount >= 0),
    currency            VARCHAR(3) NOT NULL DEFAULT 'KES',
    status              VARCHAR(20) NOT NULL DEFAULT 'HELD',
    held_since          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    refund_amount       BIGINT,
    refund_due_date     DATE,
    refund_paid_at      TIMESTAMPTZ,
    deductions          JSONB,
    notes               TEXT,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX ix_deposits_lease ON tenant.deposits (lease_id);
CREATE INDEX ix_deposits_status ON tenant.deposits (status);
CREATE INDEX ix_deposits_refund_due ON tenant.deposits (refund_due_date)
    WHERE status IN ('HELD', 'REFUND_PENDING');
