package com.maplewood.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CourseTypeConverter implements AttributeConverter<CourseType, String> {

    @Override
    public String convertToDatabaseColumn(CourseType attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public CourseType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CourseType.valueOf(dbData.toUpperCase());
    }
}