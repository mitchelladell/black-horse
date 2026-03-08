# 🏫 High School - Course Planning Challenge

Build a full-stack course planning application for students to browse courses, plan their semester schedule, and track graduation progress.

**Tech Stack**: Spring Boot + React + TypeScript + State Management Library

---

## 🎯 Requirements

### **Backend API (Spring Boot)**

**Required Endpoints:**

- List courses (with filters for grade/semester)
- Student profile with academic history, GPA, credits
- Enroll in a course (validate prerequisites, conflicts, max 5 courses)
- Current semester schedule

**Business Rules:**

- Prerequisites must be passed before enrollment
- Max 5 courses per semester
- 30 credits to graduate
- No time slot conflicts

### **Frontend (React + TypeScript)**

**Required Features:**

- **Course Browser** - List/filter courses with details (credits, prerequisites)
- **Schedule Builder** - Add/remove courses with real-time validation (prerequisites, conflicts, limits)
- **Student Dashboard** - GPA, credits, graduation progress

**State Management:**
Implement centralized state using Redux Toolkit, Zustand, Jotai, Context+useReducer, or your preferred solution. Handle loading states, errors, and optimistic updates.

---

## 🗄️ Database

Pre-populated SQLite database (`maplewood_school.sqlite`) with 400 students, 57 courses, and ~6,700 historical records.

**Key Tables:** `students`, `courses`, `student_course_history`, `semesters` (see [DATABASE.md](./DATABASE.md))

**Your Task:** Create additional tables for course sections, time slots, and current semester enrollments.

---

## 🚀 Setup

**Dev Container (Recommended):** Open in VS Code with Dev Containers extension - everything pre-configured.

**Manual Setup:** Java 17, Maven 3.8+, Node.js 20+, SQLite 3

## 📁 Project Structure

Create your project with the following structure:

```
fullstack-challenge/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/maplewood/
│   │   │   │       ├── controller/
│   │   │   │       ├── service/
│   │   │   │       ├── repository/
│   │   │   │       ├── model/
│   │   │   │       └── dto/
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── store/          # State management
│   │   ├── api/            # API client
│   │   ├── types/
│   │   └── App.tsx
│   ├── package.json
│   └── tsconfig.json
│
└── maplewood_school.sqlite
```

---

## 🛠️ Implementation

### How to Run

**Both (recommended)**

```bash

./scripts/dev.sh
```

Starts backend on `http://localhost:8080` and frontend on `http://localhost:3000`.

API endpoints are documented with Swagger please visit :

`http://localhost:8080/swagger-ui/index.html`

**Backend only**

```bash
cd backend
mvn spring-boot:run
```

**Frontend only**

```bash
cd frontend
npm install
npm run dev
```

**Tests**

```bash
# Backend
cd backend
mvn test

# Frontend
cd frontend
npm test
```

---

### Architecture

```
Follows a 5 tier Architecture
    ┌─────────────────────┐
    │     React Frontend  │
    │                     │
    │  Pages              │
    │  - Dashboard        │
    │  - Course Browser   │
    │  - Schedule         │
    │                     │
    │  React Query Hooks  │
    │  API Calls          │
    └──────────┬──────────┘
        │
        │ HTTP / JSON
        │
        ▼
    ┌─────────────────────────┐
    │  Spring Boot Backend    │
    │                         │
    │  Controllers            │
    │  - CourseController     │
    │  - StudentController    │
    │  - EnrollmentController │
    │                         │
    └──────────┬──────────────┘
        │
        ▼
    ┌─────────────────────────┐
    │       Services          │
    │                         │
    │  EnrollmentService      │
    │  CourseService          │
    │  StudentService         │
    │                         │
    │  Business Rules:        │
    │  - prerequisites        │
    │  - max 5 courses        │
    │  - capacity             │
    │  - schedule conflicts   │
    └──────────┬──────────────┘
        │
        ▼
    ┌─────────────────────────┐
    │      Repositories       │
    │                         │
    │  StudentRepository      │
    │  CourseRepository       │
    │  CourseSectionRepo      │
    │  EnrollmentRepository   │
    │  TimeSlotRepository     │
    │                         │
    │  (Spring Data JPA)      │
    └──────────┬──────────────┘
        │
        ▼
    ┌──────────────┐
    │   Database   │
    │              │
    │  Tables:     │
    │  students    │
    │  courses     │
    │  sections    │
    │  timeslots   │
    │  enrollments │
    │  history     │
    └──────────────┘
```

---

### Key Technical Decisions

**Validation ordering (cheap → expensive)**  
Enrollment checks are ordered by computational cost: existence checks first, then fast-fail business rules, then DB counts, then schedule conflict detection. This minimizes database load on invalid requests.

**Optimistic updates on unenroll only**  
Unenroll uses optimistic updates because the data shape is known removing an existing `ScheduleItem` from cache is safe. Enroll does not use optimistic updates because only `studentId` and `sectionId` are available at mutation time, not the full `ScheduleItem` shape needed to render the schedule correctly.

**Pagination metadata**  
The API returns `PagedResponse<T>` with `content`, `page`, `totalPages`, `totalElements`, and `last`. The frontend uses `last` to determine if the Next button should be disabled. That is more accurate than assuming a fixed page size.

**React Query vs Redux**  
React Query manages all server state (courses, schedule, student profile) with caching, background refetching, and optimistic updates. Redux manages UI state only (active filters, current student, pagination page).

---

### Features Implemented

- Course browser with grade and semester filters
- Pagination with server-side metadata
- Enrollment with full validation (prerequisites, capacity, conflicts, max 5 courses)
- Unenroll with optimistic updates
- Student dashboard (GPA, credits earned, graduation progress)
- Current semester schedule
- Prerequisite names resolved server-side and included in course response
