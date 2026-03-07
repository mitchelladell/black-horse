import { useState } from "react";
import { PaginationControls } from "../PaginationControls/PaginationControls";
import styles from "./CourseCatalog.module.css";
import { CourseModal } from "../CourseModal/CourseModal";
import type { Course, PagedResponse } from "../../types/Course";

export const CourseCatalog = ({
  coursesData,
}: {
  coursesData: PagedResponse<Course>;
}) => {
  const [selectedCourse, setSelectedCourse] = useState<Course | null>(null);

  return (
    <div className={styles.catalogContainer}>
      <ul className={styles.courseList}>
        {coursesData.content.map((course: Course) => (
          <li key={course.id} className={styles.courseItem}>
            <div>Name: {course.name}</div>
            <div>Credits: {course.credits}</div>
            <div>Prerequisite: {course.prerequisiteName ?? "None"}</div>
            <button
              onClick={() => {
                setSelectedCourse(course);
              }}
            >
              Enroll
            </button>
          </li>
        ))}
      </ul>
      {selectedCourse && (
        <CourseModal
          course={selectedCourse}
          onClose={() => {
            setSelectedCourse(null);
          }}
        />
      )}
      <PaginationControls isLastPage={coursesData.last} />
    </div>
  );
};
