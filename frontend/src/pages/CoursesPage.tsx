import { CourseCatalog } from "../components/CourseCatalog/CourseCatalog";
import { useAvailableCourses } from "../hooks/useCourses";
import { setFilters } from "../store/filterSlice";
import { useAppDispatch, useAppSelector } from "../store/hooks";
import styles from "./CoursePage.module.css";

export const CoursesPage = () => {
  const dispatch = useAppDispatch();
  const { grade, semester } = useAppSelector((state) => state.filters);
  const {
    data: coursesData,
    isLoading,
    isFetching,
    isError,
  } = useAvailableCourses();

  return (
    <div className={styles.pageContainer}>
      <div className={styles.filters}>
        <label htmlFor="grade-filter">Grade</label>
        <select
          id="grade-filter"
          value={grade?.toString() ?? ""}
          onChange={(e) =>
            dispatch(
              setFilters({
                grade: Number(e.target.value) || undefined,
                page: 0,
              }),
            )
          }
        >
          <option value="">All Grades</option>
          <option value="9">Grade 9</option>
          <option value="10">Grade 10</option>
          <option value="11">Grade 11</option>
          <option value="12">Grade 12</option>
        </select>

        <label htmlFor="semester-filter">Semester</label>
        <select
          id="semester-filter"
          value={semester?.toString() ?? ""}
          onChange={(e) =>
            dispatch(
              setFilters({
                semester: Number(e.target.value) || undefined,
                page: 0,
              }),
            )
          }
        >
          <option value="">All Semesters</option>
          <option value="1">Semester 1</option>
          <option value="2">Semester 2</option>
        </select>

        <button
          onClick={() =>
            dispatch(
              setFilters({ grade: undefined, semester: undefined, page: 0 }),
            )
          }
        >
          Reset Filters
        </button>
      </div>

      <div className={styles.content}>
        {isLoading && <p>Loading...</p>}
        {isError && <p>Error loading courses.</p>}
        {!isLoading && !isError && coursesData?.content.length === 0 && (
          <p>No courses found for the selected filters.</p>
        )}
        {!isLoading &&
          !isError &&
          coursesData &&
          coursesData.content.length > 0 && (
            <div
              className={
                isFetching && !isLoading ? styles.fetching : styles.ready
              }
            >
              <CourseCatalog coursesData={coursesData} />
            </div>
          )}
      </div>
    </div>
  );
};
