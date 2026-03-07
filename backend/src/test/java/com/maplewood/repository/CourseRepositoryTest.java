package com.maplewood.repository;

import com.maplewood.model.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseRepositoryTest {

    @Autowired
    CourseRepository courseRepository;

    // Helper to keep the tests clean since we always need a PageRequest
    private static final PageRequest DEFAULT_PAGE = PageRequest.of(0, 20);

    @Test
    void findFiltered_noFilters_returnsCourses() {
        // Use findFilteredPage and PageRequest
        Page<Course> resultPage = courseRepository.findFilteredPage(null, null, DEFAULT_PAGE);
        List<Course> result = resultPage.getContent();

        assertNotNull(result);
        assertFalse(result.isEmpty(), "Should return courses from the test database");
    }

    @Test
    void findFiltered_semesterFilter_returnsOnlyThatSemester() {
        Integer semester = 1;
        Page<Course> resultPage = courseRepository.findFilteredPage(null, semester, DEFAULT_PAGE);
        List<Course> result = resultPage.getContent();

        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(c -> 
            c.getSemesterOrder() != null && c.getSemesterOrder().equals(semester)
        ));
    }

    @Test
    void findFiltered_gradeFilter_respectsGradeBounds() {
        Integer grade = 10;
        Page<Course> resultPage = courseRepository.findFilteredPage(grade, null, DEFAULT_PAGE);
        List<Course> result = resultPage.getContent();

        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(c -> {
            Integer min = c.getGradeLevelMin();
            Integer max = c.getGradeLevelMax();
            boolean minOk = (min == null) || (min <= grade);
            boolean maxOk = (max == null) || (max >= grade);
            return minOk && maxOk;
        }));
    }
}