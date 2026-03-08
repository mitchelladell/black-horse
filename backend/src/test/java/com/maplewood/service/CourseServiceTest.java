package com.maplewood.service;

import com.maplewood.dto.CourseDto;
import com.maplewood.dto.PagedResponse;
import com.maplewood.model.Course;
import com.maplewood.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Test
    void getCourses_callsRepository_andReturnsDtos() {

        CourseRepository repo = mock(CourseRepository.class);
        CourseService service = new CourseService(repo);

        Course course = new Course();
        course.setId(1L);
        course.setCode("MATH101");
        course.setName("Algebra");

        // 2. Mock Pagination logic
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Course> page = new PageImpl<>(List.of(course), pageable,100L);

        when(repo.findFilteredPage(null, null, pageable)).thenReturn(page);

   
        PagedResponse<CourseDto> result = service.getCourses(null, null, 0, 10);

        assertEquals(1, result.content().size());
        assertEquals("MATH101", result.content().get(0).code());
        assertEquals("Algebra", result.content().get(0).name());
        assertEquals(0, result.page());
        assertFalse(result.last());
        
        verify(repo).findFilteredPage(null, null, pageable);
    }
}