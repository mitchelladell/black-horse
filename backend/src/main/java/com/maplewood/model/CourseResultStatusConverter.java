package com.maplewood.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CourseResultStatusConverter implements AttributeConverter<CourseResultStatus, String> {

    @Override
    public String convertToDatabaseColumn(CourseResultStatus status) {
        return status == null ? null : status.name().toLowerCase();
    }

    @Override
    public CourseResultStatus convertToEntityAttribute(String value) {
        if (value == null) return null;
        return CourseResultStatus.valueOf(value.toUpperCase());
    }
}