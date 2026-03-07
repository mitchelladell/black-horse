import { keepPreviousData, useQuery } from "@tanstack/react-query";
import { useAppSelector } from "../store/hooks";
import { fetchCourseSections, fetchCoursesPaginated } from "../api/courseApi";

import type { Course, PagedResponse } from "../types/Course";

export const useAvailableCourses = () => {
  const { grade, semester, page } = useAppSelector((state) => state.filters);

  return useQuery<PagedResponse<Course>>({
    queryKey: ["courses", grade, semester, page],
    queryFn: () => fetchCoursesPaginated({ page, grade, semester }),
    placeholderData: keepPreviousData,
  });
};

export const useCourseSections = (courseId: number) => {
  return useQuery({
    queryKey: ["sections", courseId],
    queryFn: () => fetchCourseSections(courseId),
  });
};
