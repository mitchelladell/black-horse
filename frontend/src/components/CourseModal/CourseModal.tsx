import { useEffect, useState } from "react";
import styles from "./CourseModal.module.css";
import { useCourseSections } from "../../hooks/useCourses";
import { useEnroll } from "../../hooks/useEnrollment";
import { useAppSelector } from "../../store/hooks";
import { useSchedule } from "../../hooks/useStudent";
import type { ApiResponseError } from "../../types/ApiError";

import type { Course, CourseSection, TimeSlot } from "../../types/Course";
import { SectionSlot } from "../SectionSlot/SectionSlot";
import type { ScheduleItem } from "../../types/Schedule";

export const CourseModal = ({
  course,
  onClose,
}: {
  course: Course;
  onClose: () => void;
}) => {
  const studentId = useAppSelector((state) => state.student.studentId);

  const { data: schedule } = useSchedule(studentId);

  const isAlreadyEnrolled =
    schedule?.some((item: ScheduleItem) => item.courseId === course.id) ??
    false;

  useEffect(() => {
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape") onClose();
    };
    globalThis.addEventListener("keydown", handleKeyDown);
    return () => globalThis.removeEventListener("keydown", handleKeyDown);
  }, [onClose]);

  const [enrolledSectionId, setEnrolledSectionId] = useState<number | null>(
    null,
  );

  const handleEnroll = (sectionId: number) => {
    enroll(
      { sectionId, studentId },
      {
        onSuccess: () => setEnrolledSectionId(sectionId),
      },
    );
  };

  const {
    data: courseSections,
    isLoading,
    isError: LoadingSectionsError,
  } = useCourseSections(course.id);
  const {
    mutate: enroll,
    isError: enrollError,
    error,
    isPending,
  } = useEnroll();

  if (isLoading) return <div>Loading available sections...</div>;

  return (
    <dialog
      className={styles.modalOverlay}
      onClick={(e) => {
        if (e.target === e.currentTarget) onClose();
      }}
      open
    >
      <div className={styles.modalContent}>
        <h2>{course.name}</h2>
        {enrolledSectionId && (
          <p className={styles.success}>Enrolled successfully!</p>
        )}
        {enrollError && (
          <p className={styles.error}>{(error as ApiResponseError).message}</p>
        )}
        {LoadingSectionsError && (
          <p className={styles.error}>Failed to load sections.</p>
        )}
        <ul>
          {courseSections.map((section: CourseSection) => (
            <li key={section.id}>
              <ul>
                {section.timeSlots?.map((slot: TimeSlot) => (
                  <div key={slot.id} className={styles.slotRow}>
                    <span>
                      {slot.dayOfWeek} : {slot.startTime} - {slot.endTime}
                    </span>
                  </div>
                ))}
              </ul>
              <SectionSlot
                section={section}
                isPending={isPending}
                enrolledSectionId={enrolledSectionId}
                isAlreadyEnrolled={isAlreadyEnrolled}
                onEnroll={handleEnroll}
              />
            </li>
          ))}
        </ul>
        <button className={styles.closeButton} onClick={onClose}>
          Close
        </button>
      </div>
    </dialog>
  );
};
