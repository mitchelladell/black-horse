package com.maplewood.dto;

import com.maplewood.model.Student;

public record StudentProfileDto(
        Long id,
        String name,
        Integer gradeLevel,
        Integer coursesTaken,
        Integer coursesPassed,
        Double creditsEarned,
        Double gpa,
        Double graduationProgress
) {

    public static StudentProfileDto from(
            Student student,
            int taken,
            int passed,
            double credits,
            double gpa,
            double progress
    ) {
        return new StudentProfileDto(
                student.getId(),
                student.getFirstName() + " " + student.getLastName(),
                student.getGradeLevel(),
                taken,
                passed,
                credits,
                gpa,
                progress
        );
    }
}