import type { ApiResponseError } from "../types/ApiError";
import type { EnrollmentRequest } from "../types/Enrollment";

export const enrollStudentApi = async (data: EnrollmentRequest) => {
  const response = await fetch("/api/enrollments", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    const errorBody = await response.json();
    throw errorBody as ApiResponseError;
  }
};

export const unenrollStudentApi = async ({
  studentId,
  sectionId,
}: {
  studentId: number;
  sectionId: number;
}) => {
  const response = await fetch(
    `/api/enrollments/${studentId}/sections/${sectionId}`,
    { method: "DELETE" },
  );

  if (!response.ok) {
    const errorBody = await response
      .json()
      .catch(() => ({ message: "Failed to unenroll" }));
    throw errorBody as ApiResponseError;
  }
};
