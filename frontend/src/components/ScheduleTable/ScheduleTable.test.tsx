/// <reference types="vitest/globals" />
import { render, screen } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ScheduleTable } from "./ScheduleTable";

const wrapper = ({ children }: { children: React.ReactNode }) => (
  <QueryClientProvider client={new QueryClient()}>
    {children}
  </QueryClientProvider>
);

describe("ScheduleTable", () => {
  it("shows empty state when no schedule", () => {
    render(<ScheduleTable schedule={[]} studentId={101} />, { wrapper });
    expect(
      screen.getByText("No courses enrolled for this semester."),
    ).toBeInTheDocument();
  });

  it("renders course name when schedule has items", () => {
    const schedule = [
      {
        sectionId: 1,
        courseId: 1,
        name: "Algebra",
        timeSlots: [
          { id: 1, dayOfWeek: "Monday", startTime: "09:00", endTime: "10:00" },
        ],
      },
    ];
    render(<ScheduleTable schedule={schedule} studentId={101} />, { wrapper });
    expect(screen.getByText("Algebra")).toBeInTheDocument();
  });
});
