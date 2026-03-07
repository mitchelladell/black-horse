import { Dashboard } from "../components/Dashboard/Dashboard";
import { useStudent } from "../hooks/useStudent";
import styles from "./DashboardPage.module.css";

interface DashboardPageProps {
  readonly studentId: number;
}

export const DashboardPage = ({ studentId }: DashboardPageProps) => {
  const {
    data: dashboardData,
    isLoading,
    isFetching,
    isError,
  } = useStudent(studentId);

  return (
    <div className={styles.content}>
      {isLoading && <div>Loading...</div>}
      {isError && <div>Error loading dashboard.</div>}
      {!isLoading && !isError && dashboardData && (
        <div className={isFetching ? styles.fetching : styles.ready}>
          <Dashboard dashboardData={dashboardData} />
        </div>
      )}
    </div>
  );
};
