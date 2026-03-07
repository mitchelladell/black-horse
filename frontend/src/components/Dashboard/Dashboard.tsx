import type { StudentProfile } from "../../types/StudentProfile";
import styles from "./Dashboard.module.css";

export function Dashboard({
  dashboardData,
}: {
  readonly dashboardData: StudentProfile;
}) {
  return (
    <div className={styles.card}>
      <div className={styles.headerText}>Welcome, {dashboardData.name}</div>
      <div className={styles.grid}>
        <div className={styles.stat}>GPA: {dashboardData.gpa}</div>
        <div className={styles.stat}>
          Progress: {dashboardData.graduationProgress}%
        </div>
        <div className={styles.stat}>
          credits Earned: {dashboardData.creditsEarned}
        </div>
      </div>
    </div>
  );
}
