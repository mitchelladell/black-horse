package com.maplewood.dto;

import com.maplewood.model.Course;

public record CourseDto(
        Long id,
        String code,
        String name,
        String description,
        Double credits,
        Integer hoursPerWeek,
        Long prerequisiteId,
        String prerequisiteName,

        String courseType,
        Integer gradeLevelMin,
        Integer gradeLevelMax,
        Integer semesterOrder
) {
    public static CourseDto fromEntity(Course c, String prerequisiteName) {
        return new CourseDto(
            c.getId(), 
            c.getCode(), 
            c.getName(), 
            c.getDescription(),
            c.getCredits(), 
            c.getHoursPerWeek(), 
            c.getPrerequisiteId(),
            prerequisiteName,
            c.getCourseType() != null ? c.getCourseType().name().toLowerCase() : null,            
            c.getGradeLevelMin(), 
            c.getGradeLevelMax(),
            c.getSemesterOrder()
        );
    }
}