package com.maplewood.model;

import jakarta.persistence.*;

@Entity
@Table(name = "current_enrollments")
@IdClass(CurrentEnrollmentId.class)
public class CurrentEnrollment {

    @Id
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Id
    @Column(name = "section_id", nullable = false)
    private Long sectionId;

    /** Required by JPA - do not remove */
    public CurrentEnrollment() {}

    public CurrentEnrollment(Long studentId, Long sectionId) {
        this.studentId = studentId;
        this.sectionId = sectionId;
    }

    public Long getStudentId() { return studentId; }
    public Long getSectionId() { return sectionId; }
}