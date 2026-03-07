package com.maplewood.repository;

import com.maplewood.model.CurrentEnrollment;
import com.maplewood.model.CurrentEnrollmentId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CurrentEnrollmentRepository extends JpaRepository<CurrentEnrollment, CurrentEnrollmentId> {


    long countBySectionId(Long sectionId);
    
    @Query("""
      SELECT COUNT(e)
      FROM CurrentEnrollment e
      JOIN CourseSection cs ON e.sectionId = cs.id
      WHERE e.studentId = :studentId
        AND cs.semesterId = :semesterId
    """)
    long countEnrollmentsInSemester(@Param("studentId") Long studentId, @Param("semesterId") Long semesterId);  
    
    @Query("""
      SELECT e.sectionId
      FROM CurrentEnrollment e
      WHERE e.studentId = :studentId
    """)
    List<Long> findSectionIdsByStudentId(Long studentId);
}