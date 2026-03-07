export const fetchScheduleApi = async (studentId: number) => {
  const response = await fetch(`/api/students/${studentId}/schedule`);
  if (!response.ok) throw new Error("Failed to fetch schedule");
  return response.json();
};

export const fetchStudentApi = async (id: number) => {
  const response = await fetch(`/api/students/${id}`);
  if (!response.ok) throw new Error("Failed to fetch schedule");
  return response.json();
};
