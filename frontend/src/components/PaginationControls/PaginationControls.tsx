import { setPage } from "../../store/filterSlice";
import { useAppDispatch, useAppSelector } from "../../store/hooks";
import styles from "./PaginationControls.module.css";

interface PaginationProps {
  isLastPage: boolean;
}
export const PaginationControls = ({ isLastPage }: PaginationProps) => {
  const dispatch = useAppDispatch();
  const page = useAppSelector((state) => state.filters.page);

  return (
    <div className={styles.paginationContainer}>
      <button
        className={styles.pageButton}
        disabled={page === 0}
        onClick={() => dispatch(setPage(page - 1))}
      >
        &laquo; Prev
      </button>

      <span className={styles.pageIndicator}>Page {page + 1}</span>

      <button
        className={styles.pageButton}
        disabled={isLastPage}
        onClick={() => dispatch(setPage(page + 1))}
      >
        Next &raquo;
      </button>
    </div>
  );
};
