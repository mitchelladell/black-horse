package com.maplewood.model;

import java.io.Serializable;
import java.util.Objects;

public class CurrentEnrollmentId implements Serializable {
    private Long studentId;
    private Long sectionId;

    public CurrentEnrollmentId() {}

    public CurrentEnrollmentId(Long studentId, Long sectionId) {
        this.studentId = studentId;
        this.sectionId = sectionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrentEnrollmentId that = (CurrentEnrollmentId) o;
        return Objects.equals(studentId, that.studentId) && 
               Objects.equals(sectionId, that.sectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, sectionId);
    }
}