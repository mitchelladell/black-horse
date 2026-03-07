import { ScheduleTable } from "../components/ScheduleTable/ScheduleTable";
import { useSchedule } from "../hooks/useStudent";
import styles from "./SchedulePage.module.css";

interface SchedulePageProps {
  studentId: number;
}

export const SchedulePage = ({ studentId }: SchedulePageProps) => {
  const {
    data: schedule,
    isLoading,
    isFetching,
    isError,
  } = useSchedule(studentId);

  if (isLoading) return;
  if (isError) return;

  return (
    <div className={styles.content}>
      {isLoading && <div>Loading table...</div>}
      {isError && <div>Error loading schedule.</div>}
      <div className={isFetching ? styles.fetching : styles.ready}>
        <ScheduleTable schedule={schedule} studentId={studentId} />
      </div>
    </div>
  );
};
