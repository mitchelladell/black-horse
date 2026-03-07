import { Link } from "react-router-dom";
import styles from "./NavBar.module.css";
import { useAppDispatch, useAppSelector } from "../../store/hooks";
import { setStudentId } from "../../store/studentSlice";

export function Navbar() {
  const dispatch = useAppDispatch();
  const studentId = useAppSelector((state) => state.student.studentId);

  return (
    <nav className={styles.navbar}>
      <div className={styles.navLinks}>
        <Link to="/" className={styles.navLink}>
          Dashboard
        </Link>
        <Link to="/courses" className={styles.navLink}>
          Courses
        </Link>
        <Link to="/schedule" className={styles.navLink}>
          Schedule
        </Link>
        <select
          value={studentId}
          onChange={(e) => dispatch(setStudentId(Number(e.target.value)))}
        >
          <option value={1}>Student 1</option>
          <option value={17}>Student 17</option>
          <option value={20}>Student 20</option>
          <option value={100}>Student 100</option>
          <option value={101}>Student 101</option>
        </select>
      </div>
    </nav>
  );
}
