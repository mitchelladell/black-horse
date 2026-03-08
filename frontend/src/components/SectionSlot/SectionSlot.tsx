import type { CourseSection } from "../../types/Course";
import styles from "./SectionSlot.module.css";

export const SectionSlot = ({
  section,
  isPending,
  enrolledSectionId,
  isAlreadyEnrolled,
  onEnroll,
}: {
  readonly section: CourseSection;
  readonly isPending: boolean;
  readonly enrolledSectionId: number | null;
  readonly isAlreadyEnrolled: boolean;
  readonly onEnroll: (sectionId: number) => void;
}) => (
  <div className={styles.slotRow}>
    <button
      disabled={
        isPending || enrolledSectionId === section.id || isAlreadyEnrolled
      }
      onClick={() => onEnroll(section.id)}
    >
      Select
    </button>
  </div>
);
