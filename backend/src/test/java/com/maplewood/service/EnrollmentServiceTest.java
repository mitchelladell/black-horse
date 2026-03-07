package com.maplewood.service;

import com.maplewood.dto.EnrollmentRequestDto;
import com.maplewood.exception.ConflictException;
import com.maplewood.exception.NotFoundException;
import com.maplewood.model.*;
import com.maplewood.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class EnrollmentServiceTest {

    private StudentRepository studentRepository;
    private SemesterRepository semesterRepository;
    private CourseSectionRepository courseSectionRepository;
    private TimeSlotRepository timeSlotRepository;
    private CurrentEnrollmentRepository currentEnrollmentRepository;
    private StudentCourseHistoryRepository historyRepository;

    private EnrollmentService service;

    @BeforeEach
    void setup() {
        studentRepository = mock(StudentRepository.class);
        semesterRepository = mock(SemesterRepository.class);
        courseSectionRepository = mock(CourseSectionRepository.class);
        timeSlotRepository = mock(TimeSlotRepository.class);
        currentEnrollmentRepository = mock(CurrentEnrollmentRepository.class);
        historyRepository = mock(StudentCourseHistoryRepository.class);

        service = new EnrollmentService(
                studentRepository,
                semesterRepository,
                courseSectionRepository,
                timeSlotRepository,
                currentEnrollmentRepository,
                historyRepository
        );
    }

    @Test
    void enroll_studentNotFound_throws404() {
        
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        EnrollmentRequestDto req = new EnrollmentRequestDto(1L, 10L);
        assertThrows(NotFoundException.class, () -> service.enroll(req));
    }

    @Test
    void enroll_maxFiveCourses_throws409() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student()));
        when(semesterRepository.findActiveSemesterId()).thenReturn(Optional.of(7L));
        
        CourseSection section = mock(CourseSection.class);
        when(courseSectionRepository.findById(10L)).thenReturn(Optional.of(section));
        
        // Mock that student already has 5 courses
        when(currentEnrollmentRepository.countEnrollmentsInSemester(1L, 7L)).thenReturn(5L);

        EnrollmentRequestDto req = new EnrollmentRequestDto(1L, 10L);
        assertThrows(ConflictException.class, () -> service.enroll(req));
    }

    @Test
    void enroll_wrongSemester_throws409() {
    when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student()));
    when(semesterRepository.findActiveSemesterId()).thenReturn(Optional.of(7L));

    CourseSection section = mock(CourseSection.class);
    when(section.getSemesterId()).thenReturn(99L);
    when(courseSectionRepository.findById(10L)).thenReturn(Optional.of(section));

    EnrollmentRequestDto req = new EnrollmentRequestDto(1L, 10L);
    ConflictException ex = assertThrows(ConflictException.class, () -> service.enroll(req));
    assertEquals("WRONG_SEMESTER", ex.getCode());
    }



    @Test
    void enroll_timeConflict_throws409() {
    when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student()));
    when(semesterRepository.findActiveSemesterId()).thenReturn(Optional.of(7L));

    Course course = mock(Course.class);
    when(course.getId()).thenReturn(5L);

    CourseSection newSection = mock(CourseSection.class);
    when(newSection.getSemesterId()).thenReturn(7L);
    when(newSection.getCourse()).thenReturn(course);
    when(newSection.getCapacity()).thenReturn(10);
    when(courseSectionRepository.findById(10L)).thenReturn(Optional.of(newSection));

    TimeSlot newSlot = mock(TimeSlot.class);
    when(newSlot.getDayOfWeek()).thenReturn(1);
    when(newSlot.getStartTime()).thenReturn("09:00");
    when(newSlot.getEndTime()).thenReturn("10:00");
    when(timeSlotRepository.findBySectionId(10L)).thenReturn(List.of(newSlot));

    // Fix: use findActiveSectionsByStudentAndSemester
    CourseSection existingSection = mock(CourseSection.class);
    when(existingSection.getId()).thenReturn(20L);
    when(courseSectionRepository.findActiveSectionsByStudentAndSemester(1L, 7L))
            .thenReturn(List.of(existingSection));

    TimeSlot existingSlot = mock(TimeSlot.class);
    when(existingSlot.getDayOfWeek()).thenReturn(1);
    when(existingSlot.getStartTime()).thenReturn("09:30");
    when(existingSlot.getEndTime()).thenReturn("10:30");
    when(timeSlotRepository.findBySectionIdIn(anyList())).thenReturn(List.of(existingSlot));

    EnrollmentRequestDto req = new EnrollmentRequestDto(1L, 10L);
    assertThrows(ConflictException.class, () -> service.enroll(req));
}

@Test
void enroll_valid_savesEnrollment() {
    when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student()));
    when(semesterRepository.findActiveSemesterId()).thenReturn(Optional.of(7L));

    Course course = mock(Course.class);
    when(course.getId()).thenReturn(5L);
    when(course.getPrerequisiteId()).thenReturn(null);

    CourseSection section = mock(CourseSection.class);
    when(section.getSemesterId()).thenReturn(7L);
    when(section.getCourse()).thenReturn(course);
    when(section.getCapacity()).thenReturn(10);
    when(courseSectionRepository.findById(10L)).thenReturn(Optional.of(section));

    when(currentEnrollmentRepository.countEnrollmentsInSemester(1L, 7L)).thenReturn(0L);
    when(currentEnrollmentRepository.countBySectionId(10L)).thenReturn(0L);
    when(historyRepository.findByStudentIdWithCourse(1L)).thenReturn(List.of());

    // Fix: use findActiveSectionsByStudentAndSemester
    when(courseSectionRepository.findActiveSectionsByStudentAndSemester(1L, 7L))
            .thenReturn(List.of());

    TimeSlot validSlot = mock(TimeSlot.class);
    when(validSlot.getStartTime()).thenReturn("08:00");
    when(validSlot.getEndTime()).thenReturn("09:00");
    when(timeSlotRepository.findBySectionId(10L)).thenReturn(List.of(validSlot));

    assertDoesNotThrow(() -> service.enroll(new EnrollmentRequestDto(1L, 10L)));
    verify(currentEnrollmentRepository, times(1)).save(any(CurrentEnrollment.class));
}



@Test
void unenroll_validEnrollment_deletesIt() {
    CurrentEnrollmentId id = new CurrentEnrollmentId(1L, 10L);
    when(currentEnrollmentRepository.existsById(id)).thenReturn(true);

    assertDoesNotThrow(() -> service.unenroll(1L, 10L));

    verify(currentEnrollmentRepository, times(1)).deleteById(id);
}

    @Test
    void unenroll_notEnrolled_throws404() {
        CurrentEnrollmentId id = new CurrentEnrollmentId(1L, 10L);
        when(currentEnrollmentRepository.existsById(id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.unenroll(1L, 10L));
    }

    @Test
void enroll_sectionFull_throws409() {
    when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student()));
    when(semesterRepository.findActiveSemesterId()).thenReturn(Optional.of(7L));

    CourseSection section = mock(CourseSection.class);
    when(section.getSemesterId()).thenReturn(7L);
    when(section.getCapacity()).thenReturn(2);
    when(courseSectionRepository.findById(10L)).thenReturn(Optional.of(section));
    when(currentEnrollmentRepository.countEnrollmentsInSemester(1L, 7L)).thenReturn(0L);
    when(currentEnrollmentRepository.countBySectionId(10L)).thenReturn(2L);

    EnrollmentRequestDto req = new EnrollmentRequestDto(1L, 10L);
    ConflictException ex = assertThrows(ConflictException.class, () -> service.enroll(req));
    assertEquals("SECTION_FULL", ex.getCode());
}

@Test
void enroll_duplicateCourse_throws409() {
    when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student()));
    when(semesterRepository.findActiveSemesterId()).thenReturn(Optional.of(7L));

    Course course = mock(Course.class);
    when(course.getId()).thenReturn(5L);

    CourseSection section = mock(CourseSection.class);
    when(section.getSemesterId()).thenReturn(7L);
    when(section.getCourse()).thenReturn(course);
    when(section.getCapacity()).thenReturn(10);
    when(courseSectionRepository.findById(10L)).thenReturn(Optional.of(section));

    when(currentEnrollmentRepository.countEnrollmentsInSemester(1L, 7L)).thenReturn(0L);
    when(currentEnrollmentRepository.countBySectionId(10L)).thenReturn(0L);

    TimeSlot slot = mock(TimeSlot.class);
    when(slot.getStartTime()).thenReturn("08:00");
    when(slot.getEndTime()).thenReturn("09:00");
    when(timeSlotRepository.findBySectionId(10L)).thenReturn(List.of(slot));

    // Mock existing enrollment in another section of the same course
    when(currentEnrollmentRepository.findSectionIdsByStudentId(1L)).thenReturn(List.of(20L));
    CourseSection existingSection = mock(CourseSection.class);
    when(existingSection.getCourse()).thenReturn(course);
    when(courseSectionRepository.findAllById(List.of(20L))).thenReturn(List.of(existingSection));

    EnrollmentRequestDto req = new EnrollmentRequestDto(1L, 10L);
    ConflictException ex = assertThrows(ConflictException.class, () -> service.enroll(req));
    assertEquals("DUPLICATE_COURSE", ex.getCode());
}

@Test
void enroll_prerequisiteNotSatisfied_throws409() {
    when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student()));
    when(semesterRepository.findActiveSemesterId()).thenReturn(Optional.of(7L));

    Course course = mock(Course.class);
    when(course.getId()).thenReturn(5L);
    when(course.getPrerequisiteId()).thenReturn(99L);

    CourseSection section = mock(CourseSection.class);
    when(section.getSemesterId()).thenReturn(7L);
    when(section.getCourse()).thenReturn(course);
    when(section.getCapacity()).thenReturn(10);
    when(courseSectionRepository.findById(10L)).thenReturn(Optional.of(section));

    when(currentEnrollmentRepository.countEnrollmentsInSemester(1L, 7L)).thenReturn(0L);
    when(currentEnrollmentRepository.countBySectionId(10L)).thenReturn(0L);
    when(currentEnrollmentRepository.findSectionIdsByStudentId(1L)).thenReturn(List.of());
    when(courseSectionRepository.findAllById(List.of())).thenReturn(List.of());

    TimeSlot slot = mock(TimeSlot.class);
    when(slot.getStartTime()).thenReturn("08:00");
    when(slot.getEndTime()).thenReturn("09:00");
    when(timeSlotRepository.findBySectionId(10L)).thenReturn(List.of(slot));

    // History has no PASSED record for prereq course 99
    when(historyRepository.findByStudentIdWithCourse(1L)).thenReturn(List.of());

    EnrollmentRequestDto req = new EnrollmentRequestDto(1L, 10L);
    ConflictException ex = assertThrows(ConflictException.class, () -> service.enroll(req));
    assertEquals("PREREQUISITE_MISSING", ex.getCode());
}

@Test
void enroll_alreadyPassed_throws409() {
    when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student()));
    when(semesterRepository.findActiveSemesterId()).thenReturn(Optional.of(7L));

    Course course = mock(Course.class);
    when(course.getId()).thenReturn(5L);
    when(course.getPrerequisiteId()).thenReturn(null);

    CourseSection section = mock(CourseSection.class);
    when(section.getSemesterId()).thenReturn(7L);
    when(section.getCourse()).thenReturn(course);
    when(section.getCapacity()).thenReturn(10);
    when(courseSectionRepository.findById(10L)).thenReturn(Optional.of(section));

    when(currentEnrollmentRepository.countEnrollmentsInSemester(1L, 7L)).thenReturn(0L);
    when(currentEnrollmentRepository.countBySectionId(10L)).thenReturn(0L);
    when(currentEnrollmentRepository.findSectionIdsByStudentId(1L)).thenReturn(List.of());
    when(courseSectionRepository.findAllById(List.of())).thenReturn(List.of());

    TimeSlot slot = mock(TimeSlot.class);
    when(slot.getStartTime()).thenReturn("08:00");
    when(slot.getEndTime()).thenReturn("09:00");
    when(timeSlotRepository.findBySectionId(10L)).thenReturn(List.of(slot));

    // Mock history with PASSED record for this course
    StudentCourseHistory history = mock(StudentCourseHistory.class);
    when(history.getCourse()).thenReturn(course);
    when(history.getStatus()).thenReturn(CourseResultStatus.PASSED);
    when(historyRepository.findByStudentIdWithCourse(1L)).thenReturn(List.of(history));

    EnrollmentRequestDto req = new EnrollmentRequestDto(1L, 10L);
    ConflictException ex = assertThrows(ConflictException.class, () -> service.enroll(req));
    assertEquals("ALREADY_PASSED", ex.getCode());
}
}