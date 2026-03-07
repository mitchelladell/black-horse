import { DashboardPage } from "./pages/DashboardPage.tsx";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Navbar } from "./components/NavBar/NavBar.tsx";
import { SchedulePage } from "./pages/SchedulePage.tsx";
import { CoursesPage } from "./pages/CoursesPage.tsx";
import { useAppSelector } from "./store/hooks.ts";

import "./App.css";

function App() {
  const studentId = useAppSelector((state) => state.student.studentId);

  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        <Route
          path="/"
          element={<DashboardPage studentId={studentId} />}
        ></Route>
        <Route path="/courses" element={<CoursesPage />}></Route>
        <Route
          path="/schedule"
          element={<SchedulePage studentId={studentId} />}
        ></Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
