package com.maplewood.model;

import jakarta.persistence.*;

@Entity
@Table(name = "student_course_history")
public class StudentCourseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "status")
    private CourseResultStatus status;

    public StudentCourseHistory() {}

    public Long getId() { return id; }
    public Long getStudentId() { return studentId; }
    public Course getCourse() { return course; }
    public CourseResultStatus getStatus() { return status; }
}