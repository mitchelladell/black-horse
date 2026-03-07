package com.maplewood.dto;

import com.maplewood.model.CourseSection;
import com.maplewood.model.TimeSlot;


import java.util.List;

public record CourseSectionDto(
        Long id,
        Long courseId,
        Long semesterId,
        Integer capacity,
        List<TimeSlotDto> timeSlots
) {
    public static CourseSectionDto from(CourseSection section, List<TimeSlot> slots) {
        return new CourseSectionDto(
                section.getId(),
                section.getCourse() != null ? section.getCourse().getId() : null,
                section.getSemesterId(),
                section.getCapacity(),
                slots.stream().map(TimeSlotDto::from).toList()
        );
    }
}