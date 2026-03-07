package com.maplewood.controller;

import com.maplewood.dto.CourseScheduleDto;
import com.maplewood.dto.StudentProfileDto;
import com.maplewood.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/students")
@Tag(name = "Students", description = "Student dashboard and scheduling endpoints")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @Operation(
        summary = "Get student dashboard",
        description = "Returns GPA, credits earned, and graduation progress"
    )
    @GetMapping("/{id}")
    public StudentProfileDto getStudent(@PathVariable @Min(1) @Max(9999) Long id) {
        return service.getStudentProfile(id);
    }
    
    @Operation(summary = "Get current semester schedule", description = "Returns enrolled sections with meeting times for the active semester")
    @GetMapping("/{id}/schedule")
    public List<CourseScheduleDto> getSchedule(@PathVariable @Min(1) @Max(9999) Long id) {
    return service.getStudentSchedule(id);
}
    
    
}