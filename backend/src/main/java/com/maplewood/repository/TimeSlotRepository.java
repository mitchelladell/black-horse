package com.maplewood.repository;

import com.maplewood.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findBySectionId(Long sectionId);
    List<TimeSlot> findBySectionIdIn(List<Long> sectionIds);
}