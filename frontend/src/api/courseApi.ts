interface CourseFilters {
  page?: number;
  size?: number;
  grade?: number;
  semester?: number;
}

export const fetchCoursesPaginated = async (filters: CourseFilters = {}) => {
  const { page = 0, size = 10, grade, semester } = filters;

  const params = new URLSearchParams({
    page: page.toString(),
    size: size.toString(),
  });

  if (grade !== undefined) params.append("grade", grade.toString());
  if (semester !== undefined) params.append("semester", semester.toString());

  const response = await fetch(`/api/courses?${params.toString()}`);
  if (!response.ok) throw new Error("Failed to fetch courses");
  return response.json();
};

export const fetchCourseSections = async (courseId: number) => {
  const response = await fetch(`/api/courses/${courseId}/sections`);

  if (!response.ok) throw new Error("Failed to fetch course sections");
  return response.json();
};
