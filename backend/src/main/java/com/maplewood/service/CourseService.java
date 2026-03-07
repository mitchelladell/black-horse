package com.maplewood.service;

import com.maplewood.dto.CourseDto;
import com.maplewood.dto.PagedResponse;
import com.maplewood.model.Course;
import com.maplewood.repository.CourseRepository;
import org.springframework.data.domain.PageRequest; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

   @Transactional(readOnly = true)
   public PagedResponse<CourseDto> getCourses(Integer grade, Integer semester, int page, int size) {
    Page<Course> result = courseRepository.findFilteredPage(grade, semester, PageRequest.of(page, size));
    List<Course> courses = result.getContent();

    List<Long> prereqIds = courses.stream()
            .map(Course::getPrerequisiteId)
            .filter(Objects::nonNull)
            .toList();

    Map<Long, String> prereqNames = courseRepository.findAllById(prereqIds)
            .stream()
            .collect(Collectors.toMap(Course::getId, Course::getName));

    List<CourseDto> dtos = courses.stream()
            .map(c -> CourseDto.fromEntity(c, prereqNames.get(c.getPrerequisiteId())))
            .toList();

    return PagedResponse.from(result, dtos);
 }
}