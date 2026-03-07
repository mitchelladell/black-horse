package com.maplewood.repository;

import com.maplewood.model.StudentCourseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentCourseHistoryRepository extends JpaRepository<StudentCourseHistory, Long> {

    @Query("""
        SELECT h
        FROM StudentCourseHistory h
        JOIN FETCH h.course
        WHERE h.studentId = :studentId
    """)
    List<StudentCourseHistory> findByStudentIdWithCourse(@Param("studentId") Long studentId);
}