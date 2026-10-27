CREATE TABLE book_log (
    id          UUID         PRIMARY KEY DEFAULT uuidv7(),
    book_id     UUID         NOT NULL,
    type        VARCHAR(32)  NOT NULL CHECK (type IN ('BOOK_CREATED', 'BOOK_UPDATED')),
    message     TEXT         NOT NULL,
    occurred_at TIMESTAMPTZ  NOT NULL
);

CREATE INDEX idx_book_log_book_id_occurred_at ON book_log (book_id, occurred_at);
