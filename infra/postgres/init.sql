CREATE DATABASE loan_approval;

\connect loan_application;
CREATE TABLE IF NOT EXISTS platform_schema_version (
  id integer primary key,
  description text not null,
  applied_at timestamptz not null default now()
);
INSERT INTO platform_schema_version(id, description)
VALUES (1, 'loan application database initialized')
ON CONFLICT DO NOTHING;

\connect loan_approval;
CREATE TABLE IF NOT EXISTS platform_schema_version (
  id integer primary key,
  description text not null,
  applied_at timestamptz not null default now()
);
INSERT INTO platform_schema_version(id, description)
VALUES (1, 'loan approval database initialized')
ON CONFLICT DO NOTHING;
