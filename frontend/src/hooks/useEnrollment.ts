import { useMutation, useQueryClient } from "@tanstack/react-query";

import { enrollStudentApi, unenrollStudentApi } from "../api/enrollmentApi";
import type { ScheduleItem } from "../types/Schedule";

export const useEnroll = () => {
  return useMutation({
    mutationFn: enrollStudentApi,
  });
};

export const useUnenroll = (studentId: number) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (sectionId: number) =>
      unenrollStudentApi({ studentId, sectionId }),
    onMutate: async (sectionId) => {
      await queryClient.cancelQueries({ queryKey: ["schedule", studentId] });
      const previous = queryClient.getQueryData<ScheduleItem[]>([
        "schedule",
        studentId,
      ]);
      queryClient.setQueryData<ScheduleItem[]>(["schedule", studentId], (old) =>
        (old ?? []).filter((item) => item.sectionId !== sectionId),
      );
      return { previous };
    },
    onError: (_err, _vars, context) => {
      queryClient.setQueryData(["schedule", studentId], context?.previous);
    },
    onSuccess: () =>
      queryClient.invalidateQueries({ queryKey: ["schedule", studentId] }),
  });
};
