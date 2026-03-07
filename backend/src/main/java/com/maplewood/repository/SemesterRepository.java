package com.maplewood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.maplewood.model.Semester;

import java.util.Optional;

public interface SemesterRepository extends JpaRepository<Semester, Long> {

    @Query("SELECT s.id FROM Semester s WHERE s.isActive = true")
    Optional<Long> findActiveSemesterId();
}