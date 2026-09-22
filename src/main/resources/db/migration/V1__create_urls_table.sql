CREATE SEQUENCE urls_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE urls (
    id BIGINT PRIMARY KEY DEFAULT nextval('urls_id_seq'),
    short_code VARCHAR(16) NOT NULL,
    long_url TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE,
    click_count BIGINT NOT NULL DEFAULT 0 CHECK (click_count >= 0)
);

ALTER SEQUENCE urls_id_seq OWNED BY urls.id;

CREATE UNIQUE INDEX urls_short_code_key ON urls (short_code);
