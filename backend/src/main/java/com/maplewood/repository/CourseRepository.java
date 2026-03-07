package com.maplewood.repository;

import com.maplewood.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("""
        SELECT c FROM Course c
        WHERE (:semester IS NULL OR c.semesterOrder = :semester)
          AND (:grade IS NULL OR 
               ((c.gradeLevelMin IS NULL OR c.gradeLevelMin <= :grade)
                AND (c.gradeLevelMax IS NULL OR c.gradeLevelMax >= :grade)))
        ORDER BY c.code
    """)
    Page<Course> findFilteredPage(
        @Param("grade") Integer grade, 
        @Param("semester") Integer semester, 
        Pageable pageable
    );
}