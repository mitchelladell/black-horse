PRAGMA foreign_keys = ON;

-- 1) Concrete course offering in a semester
CREATE TABLE IF NOT EXISTS course_sections (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  course_id INTEGER NOT NULL,
  semester_id INTEGER NOT NULL,
  capacity INTEGER NOT NULL DEFAULT 10 CHECK (capacity > 0 AND capacity <= 10),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (course_id) REFERENCES courses(id),
  FOREIGN KEY (semester_id) REFERENCES semesters(id)
);

CREATE INDEX IF NOT EXISTS idx_course_sections_semester ON course_sections(semester_id);
CREATE INDEX IF NOT EXISTS idx_course_sections_course ON course_sections(course_id);

-- 2) Meeting times for a section
-- day_of_week: 1=Mon ... 5=Fri
-- time stored as 'HH:MM' (TEXT). Lexicographic comparisons work for HH:MM.
CREATE TABLE IF NOT EXISTS time_slots (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  section_id INTEGER NOT NULL,
  day_of_week INTEGER NOT NULL CHECK (day_of_week BETWEEN 1 AND 5),
  start_time TEXT NOT NULL,
  end_time TEXT NOT NULL,
  FOREIGN KEY (section_id) REFERENCES course_sections(id),
  CHECK (start_time < end_time)
);

CREATE INDEX IF NOT EXISTS idx_time_slots_section ON time_slots(section_id);
CREATE INDEX IF NOT EXISTS idx_time_slots_day ON time_slots(day_of_week);

-- 3) Current enrollments (student ↔ section)
CREATE TABLE IF NOT EXISTS current_enrollments (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  student_id INTEGER NOT NULL,
  section_id INTEGER NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (student_id) REFERENCES students(id),
  FOREIGN KEY (section_id) REFERENCES course_sections(id),
  UNIQUE(student_id, section_id)
);

CREATE INDEX IF NOT EXISTS idx_current_enrollments_student ON current_enrollments(student_id);
CREATE INDEX IF NOT EXISTS idx_current_enrollments_section ON current_enrollments(section_id);