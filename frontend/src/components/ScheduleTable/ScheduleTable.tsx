import { useUnenroll } from "../../hooks/useEnrollment";
import type { ApiResponseError } from "../../types/ApiError";
import type { ScheduleItem, ScheduleTimeSlot } from "../../types/Schedule";
import styles from "./ScheduleTable.module.css";

export const ScheduleTable = ({
  schedule,
  studentId,
}: {
  readonly schedule: ScheduleItem[];
  readonly studentId: number;
}) => {
  const { mutate: unenroll, isError, error } = useUnenroll(studentId);

  if (!schedule || schedule.length === 0) {
    return <p>No courses enrolled for this semester.</p>;
  }

  return (
    <div>
      {isError && (
        <p className={styles.error}>{(error as ApiResponseError).message}</p>
      )}
      <table className={styles.tableContainer}>
        <thead>
          <tr>
            <th className={styles.tableHeader}>Course</th>
            <th className={styles.tableHeader}>Day</th>
            <th className={styles.tableHeader}>Time</th>
            <th className={styles.tableHeader}>Action</th>
          </tr>
        </thead>
        <tbody>
          {schedule.map((item: ScheduleItem) => {
            const slotCount = item.timeSlots?.length || 1;
            return (item.timeSlots ?? []).map(
              (slot: ScheduleTimeSlot, index: number) => (
                <tr key={`${item.sectionId}-${index}`} className={styles.row}>
                  {index === 0 && (
                    <td className={styles.cell} rowSpan={slotCount}>
                      <strong>{item.name}</strong>
                    </td>
                  )}
                  <td className={styles.cell}>{slot.dayOfWeek}</td>
                  <td className={styles.cell}>
                    {slot.startTime} - {slot.endTime}
                  </td>
                  {index === 0 && (
                    <td className={styles.cell} rowSpan={slotCount}>
                      <button onClick={() => unenroll(item.sectionId, {})}>
                        Remove
                      </button>
                    </td>
                  )}
                </tr>
              ),
            );
          })}
        </tbody>
      </table>
    </div>
  );
};
