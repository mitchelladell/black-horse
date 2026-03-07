export interface Course {
  id: number;
  name: string;
  code: string;
  credits: number;
  grade: number;
  prerequisiteId: number | null;
  prerequisiteName: string | null;
}

export interface PagedResponse<T> {
  content: T[];
  page: number;
  totalPages: number;
  totalElements: number;
  last: boolean;
}

export interface TimeSlot {
  id: number;
  dayOfWeek: string;
  startTime: string;
  endTime: string;
}

export interface CourseSection {
  id: number;
  courseId: number;
  semesterId: number;
  capacity: number;
  timeSlots: TimeSlot[];
}
