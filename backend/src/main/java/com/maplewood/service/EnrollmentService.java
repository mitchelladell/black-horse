package com.maplewood.service;

import com.maplewood.dto.EnrollmentRequestDto;
import com.maplewood.exception.ConflictException;
import com.maplewood.exception.NotFoundException;
import com.maplewood.model.*;
import com.maplewood.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnrollmentService {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentService.class);

    private final StudentRepository studentRepository;
    private final SemesterRepository semesterRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final CurrentEnrollmentRepository currentEnrollmentRepository;
    private final StudentCourseHistoryRepository historyRepository;

    public EnrollmentService(
            StudentRepository studentRepository,
            SemesterRepository semesterRepository,
            CourseSectionRepository courseSectionRepository,
            TimeSlotRepository timeSlotRepository,
            CurrentEnrollmentRepository currentEnrollmentRepository,
            StudentCourseHistoryRepository historyRepository
    ) {
        this.studentRepository = studentRepository;
        this.semesterRepository = semesterRepository;
        this.courseSectionRepository = courseSectionRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.currentEnrollmentRepository = currentEnrollmentRepository;
        this.historyRepository = historyRepository;
    }

    @Transactional
    public void enroll(EnrollmentRequestDto req) {

        Long studentId = req.studentId();
        Long sectionId = req.sectionId();

        log.info("Enroll request studentId={}, sectionId={}", studentId, sectionId);

        studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student with id " + studentId + " not found"));

        CourseSection section = courseSectionRepository.findById(sectionId)
                .orElseThrow(() -> new NotFoundException("Section with id " + sectionId + " not found"));

        Long activeSemesterId = semesterRepository.findActiveSemesterId()
                .orElseThrow(() -> new NotFoundException("No active semester configured"));



        if (currentEnrollmentRepository.existsById(new CurrentEnrollmentId(studentId, sectionId))) {
            throw new ConflictException("ALREADY_ENROLLED", "You are already enrolled in this section.");
        }

        if (!activeSemesterId.equals(section.getSemesterId())) {
            throw new ConflictException("WRONG_SEMESTER", "Cannot enroll: section is not in the active semester");
        }


        if (currentEnrollmentRepository.countEnrollmentsInSemester(studentId, activeSemesterId) >= 5) {
            throw new ConflictException("MAX_COURSES", "Cannot enroll: max 5 courses per semester");
        }

        long enrolledInSection = currentEnrollmentRepository.countBySectionId(sectionId);
        if (enrolledInSection >= section.getCapacity()) {
            throw new ConflictException("SECTION_FULL", "Cannot enroll: section is full");
        }

   

        var newSlots = timeSlotRepository.findBySectionId(sectionId);

   


        List<Long> existingSectionIds = currentEnrollmentRepository.findSectionIdsByStudentId(studentId);

        boolean alreadyInCourse = courseSectionRepository.findAllById(existingSectionIds)
                .stream()
                .anyMatch(s -> s.getCourse().getId().equals(section.getCourse().getId()));

        if (alreadyInCourse) {
            throw new ConflictException(
                    "DUPLICATE_COURSE",
                    "Cannot enroll: already enrolled in another section of this course"
            );
        }



        List<StudentCourseHistory> history = historyRepository.findByStudentIdWithCourse(studentId);

        Long prereqId = section.getCourse().getPrerequisiteId();

        if (prereqId != null) {
            boolean passedPrereq = history.stream()
                    .anyMatch(h ->
                            h.getStatus() == CourseResultStatus.PASSED &&
                            h.getCourse() != null &&
                            prereqId.equals(h.getCourse().getId())
                    );

            if (!passedPrereq) {
                throw new ConflictException(
                        "PREREQUISITE_MISSING",
                        "Cannot enroll: prerequisite not satisfied"
                );
            }
        }

        boolean alreadyPassed = history.stream()
                .anyMatch(h ->
                        h.getCourse() != null &&
                        section.getCourse().getId().equals(h.getCourse().getId()) &&
                        h.getStatus() == CourseResultStatus.PASSED
                );

        if (alreadyPassed) {
            throw new ConflictException(
                    "ALREADY_PASSED",
                    "Cannot enroll: already passed this course"
            );
        }

  
        List<CourseSection> activeSections = courseSectionRepository
                .findActiveSectionsByStudentAndSemester(studentId, activeSemesterId);

        List<Long> activeSectionIds = activeSections.stream()
                .map(CourseSection::getId)
                .toList();

        if (!activeSectionIds.isEmpty()) {
            var existingSlots = timeSlotRepository.findBySectionIdIn(activeSectionIds);
            if (hasConflict(newSlots, existingSlots)) {
                throw new ConflictException("SCHEDULE_CONFLICT", "Time conflict with existing schedule");
            }
        }


        currentEnrollmentRepository.save(new CurrentEnrollment(studentId, sectionId));
        log.info("Enrolled studentId={} into sectionId={}", studentId, sectionId);

    }

    @Transactional
    public void unenroll(Long studentId, Long sectionId) {
    CurrentEnrollmentId id = new CurrentEnrollmentId(studentId, sectionId);
    if (!currentEnrollmentRepository.existsById(id)) {
        throw new NotFoundException("Enrollment not found for studentId=" + studentId + " sectionId=" + sectionId);
    }
    currentEnrollmentRepository.deleteById(id);
    log.info("Unenrolled studentId={} from sectionId={}", studentId, sectionId);

    }

    private boolean hasConflict(List<TimeSlot> newSlots, List<TimeSlot> existingSlots) {

        for (var n : newSlots) {
            for (var e : existingSlots) {

                if (!n.getDayOfWeek().equals(e.getDayOfWeek())) continue;

                boolean overlap =
                        n.getStartTime().compareTo(e.getEndTime()) < 0 &&
                        n.getEndTime().compareTo(e.getStartTime()) > 0;

                if (overlap) return true;
            }
        }

        return false;
    }
}