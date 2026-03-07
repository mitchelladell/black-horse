package com.maplewood.dto;

import com.maplewood.model.CourseSection;
import com.maplewood.model.TimeSlot;
import java.util.List;

public record CourseScheduleDto(
        Long sectionId,
        Long courseId,
        String code,
        String name,
        Double credits,
        List<TimeSlotDto> timeSlots
) {
    public static CourseScheduleDto from(CourseSection s, List<TimeSlot> slots) {
        return new CourseScheduleDto(
                s.getId(),
                s.getCourse().getId(),
                s.getCourse().getCode(),
                s.getCourse().getName(),
                s.getCourse().getCredits(),
                slots.stream().map(TimeSlotDto::from).toList()
        );
    }
}