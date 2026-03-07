package com.maplewood.service;

import com.maplewood.dto.CourseSectionDto;
import com.maplewood.model.CourseSection;
import com.maplewood.model.TimeSlot;
import com.maplewood.repository.CourseSectionRepository;
import com.maplewood.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class CourseSectionService {
    private final CourseSectionRepository courseSectionRepository;
    private final TimeSlotRepository timeSlotRepository;

    public CourseSectionService(CourseSectionRepository courseSectionRepository,
                                TimeSlotRepository timeSlotRepository) {
        this.courseSectionRepository = courseSectionRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    public List<CourseSectionDto> getSectionsByCourse(Long courseId) {
        List<CourseSection> sections = courseSectionRepository.findByCourse_Id(courseId);
        List<Long> sectionIds = sections.stream().map(CourseSection::getId).toList();
        List<TimeSlot> allTimeSlots = timeSlotRepository.findBySectionIdIn(sectionIds);

        Map<Long, List<TimeSlot>> slotsBySectionId = allTimeSlots.stream()
                .collect(Collectors.groupingBy(TimeSlot::getSectionId));

        return sections.stream()
                .map(s -> CourseSectionDto.from(s, slotsBySectionId.getOrDefault(s.getId(), List.of())))
                .toList();
    }
}