PRAGMA foreign_keys = ON;

-- Get active semester id
-- (Assumes exactly one active semester)
-- Create 1 section per course for active semester, if none exist.
INSERT INTO course_sections (course_id, semester_id, capacity)
SELECT c.id, s.id, 10
FROM courses c
JOIN semesters s ON s.is_active = 1
WHERE NOT EXISTS (
  SELECT 1 FROM course_sections cs WHERE cs.course_id = c.id AND cs.semester_id = s.id
);

-- Add 2 weekly time slots for each section (simple deterministic grid):
-- Mon/Wed 09:00-10:00 for first half, Tue/Thu 10:00-11:00 for second half
-- (This is just to have data; you can refine later.)

-- Clear existing time slots for active semester sections (optional, comment out if you want preserve)
DELETE FROM time_slots
WHERE section_id IN (
  SELECT cs.id FROM course_sections cs
  JOIN semesters s ON s.id = cs.semester_id
  WHERE s.is_active = 1
);

-- Assign times based on section id parity
INSERT INTO time_slots (section_id, day_of_week, start_time, end_time)
SELECT cs.id,
       CASE WHEN (cs.id % 2) = 0 THEN 1 ELSE 2 END,
       CASE WHEN (cs.id % 2) = 0 THEN '09:00' ELSE '10:00' END,
       CASE WHEN (cs.id % 2) = 0 THEN '10:00' ELSE '11:00' END
FROM course_sections cs
JOIN semesters s ON s.id = cs.semester_id
WHERE s.is_active = 1;

INSERT INTO time_slots (section_id, day_of_week, start_time, end_time)
SELECT cs.id,
       CASE WHEN (cs.id % 2) = 0 THEN 3 ELSE 4 END,
       CASE WHEN (cs.id % 2) = 0 THEN '09:00' ELSE '10:00' END,
       CASE WHEN (cs.id % 2) = 0 THEN '10:00' ELSE '11:00' END
FROM course_sections cs
JOIN semesters s ON s.id = cs.semester_id
WHERE s.is_active = 1;