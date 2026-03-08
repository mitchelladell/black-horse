import { useQuery } from "@tanstack/react-query";
import { fetchScheduleApi, fetchStudentApi } from "../api/studentAPi";

export function useStudent(studentId: number) {
  return useQuery({
    queryKey: ["student", studentId],
    queryFn: () => fetchStudentApi(studentId),
  });
}

export const useSchedule = (studentId: number) => {
  return useQuery({
    queryKey: ["schedule", studentId],
    queryFn: () => fetchScheduleApi(studentId),
  });
};
