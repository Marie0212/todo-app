CREATE TABLE IF NOT EXISTS tasks (
  id        INTEGER PRIMARY KEY,
  title     TEXT NOT NULL,
  description TEXT,
  due_date  TEXT,
  status    TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
  id    INTEGER PRIMARY KEY,
  name  TEXT NOT NULL UNIQUE
);

-- optional: task -> category relation (falls US-05/US-06 das braucht)
CREATE TABLE IF NOT EXISTS task_categories (
  task_id     INTEGER NOT NULL,
  category_id INTEGER NOT NULL,
  PRIMARY KEY (task_id, category_id),
  FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
  FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);
