package com.maplewood.service;

import com.maplewood.dto.CourseScheduleDto;
import com.maplewood.dto.StudentProfileDto;
import com.maplewood.exception.NotFoundException;
import com.maplewood.model.Student;
import com.maplewood.model.StudentCourseHistory;
import com.maplewood.model.CourseResultStatus; // Added this import
import com.maplewood.model.CourseSection;
import com.maplewood.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.maplewood.model.TimeSlot;
import java.util.stream.Collectors;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentCourseHistoryRepository historyRepository;
    private final SemesterRepository semesterRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final TimeSlotRepository timeSlotRepository;
    
    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    public StudentService(
            StudentRepository studentRepository, 
            StudentCourseHistoryRepository historyRepository,
            SemesterRepository semesterRepository,
            CourseSectionRepository courseSectionRepository,
            TimeSlotRepository timeSlotRepository) {
        this.studentRepository = studentRepository;
        this.historyRepository = historyRepository;
        this.semesterRepository = semesterRepository;
        this.courseSectionRepository = courseSectionRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

@Transactional(readOnly = true)
public List<CourseScheduleDto> getStudentSchedule(Long studentId) {

    log.info("Fetching schedule for studentId={}", studentId);

    studentRepository.findById(studentId)
            .orElseThrow(() -> new NotFoundException("Student with id " + studentId + " not found"));

    Long activeSemesterId = semesterRepository.findActiveSemesterId()
            .orElseThrow(() -> new NotFoundException("No active semester configured"));

    List<CourseSection> sections = courseSectionRepository
            .findActiveSectionsByStudentAndSemester(studentId, activeSemesterId);

    if (sections.isEmpty()) return List.of();

    List<Long> sectionIds = sections.stream().map(CourseSection::getId).toList();
    var slots = timeSlotRepository.findBySectionIdIn(sectionIds);

    var slotsBySection = slots.stream()
            .collect(Collectors.groupingBy(TimeSlot::getSectionId));

   return sections.stream()
        .map(s -> CourseScheduleDto.from(s, slotsBySection.getOrDefault(s.getId(), List.of())))
        .toList();
}

    @Transactional(readOnly = true)
    public StudentProfileDto getStudentProfile(Long id) {

        log.info("Fetching student profile for id={}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student with id " + id + " not found"));

        List<StudentCourseHistory> history = historyRepository.findByStudentIdWithCourse(id);

        int taken = history.size();

        int passed = (int) history.stream()
                .filter(h -> h.getStatus() == CourseResultStatus.PASSED)
                .count();

        double creditsEarned = history.stream()
                .filter(h -> h.getStatus() == CourseResultStatus.PASSED)
                .mapToDouble(h -> h.getCourse().getCredits())
                .sum();

        double totalCreditsAttempted = history.stream()
                .mapToDouble(h -> h.getCourse().getCredits())
                .sum();

        double gpa = totalCreditsAttempted == 0
                ? 0.0
                : (creditsEarned / totalCreditsAttempted) * 4.0;

        double roundedGpa = Math.round(gpa * 100.0) / 100.0;
        double graduationProgress = Math.min(100.0, (creditsEarned / 30.0) * 100.0);

        return StudentProfileDto.from(student, taken, passed, creditsEarned, roundedGpa, graduationProgress);
    }
}