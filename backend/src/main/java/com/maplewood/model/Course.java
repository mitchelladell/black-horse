package com.maplewood.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course code is required")
    @Column(nullable = false, unique = true)
    private String code;

    @NotBlank(message = "Course name is required")
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @DecimalMin(value = "0.1")
    @Column(nullable = false)
    private Double credits;

    @Min(2)
    @Max(6)
    @Column(name = "hours_per_week")
    private Integer hoursPerWeek;

    @Column(name = "specialization_id")
    private Long specializationId;

    @Column(name = "prerequisite_id")
    private Long prerequisiteId;

    @jakarta.persistence.Convert(converter = CourseTypeConverter.class)
    @Column(name = "course_type", nullable = false)
    private CourseType courseType;

    @Min(9)
    @Max(12)
    @Column(name = "grade_level_min")
    private Integer gradeLevelMin;

    @Min(9)
    @Max(12)
    @Column(name = "grade_level_max")
    private Integer gradeLevelMax;

    @Column(name = "semester_order")
    private Integer semesterOrder;

    // JPA requires a no-args constructor.
    // It is public to allow simple instantiation in unit tests.
    public Course() {}

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Double getCredits() { return credits; }
    public Integer getHoursPerWeek() { return hoursPerWeek; }
    public Long getSpecializationId() { return specializationId; }
    public Long getPrerequisiteId() { return prerequisiteId; }
    public CourseType getCourseType() { return courseType; }
    public Integer getGradeLevelMin() { return gradeLevelMin; }
    public Integer getGradeLevelMax() { return gradeLevelMax; }
    public Integer getSemesterOrder() { return semesterOrder; }


    // Setters provided primarily for unit testing convenience.
    public void setId(Long id) { this.id = id; }   
    public void setCode(String code) { this.code = code; } 
    public void setName(String name) { this.name = name; }  
}

