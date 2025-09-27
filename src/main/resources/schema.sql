CREATE TABLE IF NOT EXISTS posts (
  id          BIGSERIAL PRIMARY KEY,
  title       VARCHAR(200)   NOT NULL,
  content     TEXT           NOT NULL,
  author      VARCHAR(50)    NOT NULL,
  view_count  INTEGER        NOT NULL DEFAULT 0,
  created_at  TIMESTAMP      NOT NULL DEFAULT NOW(),
  updated_at  TIMESTAMP      NOT NULL DEFAULT NOW()
);