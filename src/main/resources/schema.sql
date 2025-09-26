CREATE TABLE IF NOT EXISTS posts (
  id          BIGSERIAL PRIMARY KEY,
  title       VARCHAR(200)   NOT NULL,
  content     TEXT           NOT NULL,
  author      VARCHAR(50)    NOT NULL,
  view_count  INTEGER        NOT NULL DEFAULT 0,
  created_at  TIMESTAMP      NOT NULL DEFAULT NOW(),
  updated_at  TIMESTAMP      NOT NULL DEFAULT NOW()
);

DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'posts_updated_at_trigger') THEN
    CREATE OR REPLACE FUNCTION set_posts_updated_at() RETURNS trigger AS $f$
    BEGIN
      NEW.updated_at := NOW();
      RETURN NEW;
    END; $f$ LANGUAGE plpgsql;

    CREATE TRIGGER posts_updated_at_trigger
      BEFORE UPDATE ON posts
      FOR EACH ROW EXECUTE PROCEDURE set_posts_updated_at();
  END IF;
END$$;
