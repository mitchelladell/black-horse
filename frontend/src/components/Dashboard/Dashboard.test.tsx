/// <reference types="vitest/globals" />
import { render, screen } from "@testing-library/react";
import { Dashboard } from "./Dashboard";

const mockData = {
  name: "Mitchell Adel",
  gpa: 3.5,
  id: 22,
  grade: 20,
  creditsEarned: 15,
  graduationProgress: 50,
};

describe("Dashboard", () => {
  it("renders student name", () => {
    render(<Dashboard dashboardData={mockData} />);
    expect(screen.getByText("Welcome, Mitchell Adel")).toBeInTheDocument();
  });

  it("renders GPA", () => {
    render(<Dashboard dashboardData={mockData} />);
    expect(screen.getByText("GPA: 3.5")).toBeInTheDocument();
  });

  it("renders graduation progress", () => {
    render(<Dashboard dashboardData={mockData} />);
    expect(screen.getByText("Progress: 50%")).toBeInTheDocument();
  });
});
