package com.maplewood.controller;

import com.maplewood.dto.EnrollmentRequestDto;
import com.maplewood.service.EnrollmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@Validated
@Tag(name = "Enrollments", description = "Enroll a student into a course section")
@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService service;

    public EnrollmentController(EnrollmentService service) {
        this.service = service;
    }
    @Operation(summary = "Enroll in a section", description = "Validates prerequisites, conflicts, capacity, and max 5 courses")
    @PostMapping
    public ResponseEntity<Void> enroll(@Valid @RequestBody EnrollmentRequestDto req) {
        service.enroll(req);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Unenroll from a section")
    @DeleteMapping("/{studentId}/sections/{sectionId}")
    public ResponseEntity<Void> unenroll(
            @PathVariable  @Min(1) @Max(9999) Long studentId,
            @PathVariable  @Min(1) @Max(99) Long sectionId) {
        service.unenroll(studentId, sectionId);
        return ResponseEntity.noContent().build();
    }
}

