package com.maplewood.repository;

import com.maplewood.model.CourseSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseSectionRepository extends JpaRepository<CourseSection, Long> {
    List<CourseSection> findByCourse_Id(Long courseId);

    @Query("SELECT s FROM CourseSection s " +
           "JOIN CurrentEnrollment ce ON ce.sectionId = s.id " +
           "WHERE ce.studentId = :studentId " +
           "AND s.semesterId = :semesterId")
    List<CourseSection> findActiveSectionsByStudentAndSemester(
            @Param("studentId") Long studentId, 
            @Param("semesterId") Long semesterId
    );
}