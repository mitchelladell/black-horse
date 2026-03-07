export interface ScheduleTimeSlot {
  dayOfWeek: string;
  startTime: string;
  endTime: string;
}

export interface ScheduleItem {
  sectionId: number;
  courseId: number;
  code: string;
  name: string;
  credits: number;
  timeSlots: ScheduleTimeSlot[];
}
