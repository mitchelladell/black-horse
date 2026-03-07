package com.maplewood.model;

import jakarta.persistence.*;

@Entity
@Table(name = "course_sections")
public class CourseSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "semester_id", nullable = false)
    private Long semesterId;

    @Column(nullable = false)
    private Integer capacity;

    public CourseSection() {}

    public Long getId() { return id; }
    public Course getCourse() { return course; }
    public Long getSemesterId() { return semesterId; }
    public Integer getCapacity() { return capacity; }
}