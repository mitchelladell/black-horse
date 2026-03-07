package com.maplewood.controller;

import com.maplewood.dto.CourseDto;
import com.maplewood.dto.CourseSectionDto;
import com.maplewood.dto.PagedResponse;
import com.maplewood.service.CourseService;
import com.maplewood.service.CourseSectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/courses")
@Tag(name = "Courses", description = "Courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseSectionService courseSectionService;

    public CourseController(CourseService courseService, CourseSectionService courseSectionService) {
        this.courseService = courseService;
        this.courseSectionService = courseSectionService;
    }

    @GetMapping
    public ResponseEntity<PagedResponse<CourseDto>> getCourses(
            @RequestParam(required = false) @Min(9) @Max(12)  Integer grade,
            @RequestParam(required = false)  @Min(1) @Max(2) Integer semester,
            @RequestParam(defaultValue = "0" ) @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        return ResponseEntity.ok(courseService.getCourses(grade, semester, page, size));
    }

    @Operation(summary = "Get Sections for a Course")
    @GetMapping("/{courseId}/sections")
    public List<CourseSectionDto> getSections(@PathVariable @Min(0) @Max(999) Long courseId) {
        return courseSectionService.getSectionsByCourse(courseId);
    }



    
}