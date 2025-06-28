package com.ege.service;

import com.ege.dto.CourseAssistantDto;
import com.ege.entities.Course;
import com.ege.entities.CourseAssistant;
import com.ege.entities.CourseAssistantId;
import com.ege.entities.ResearchAssistant;
import com.ege.repository.CourseAssistantRepository;
import com.ege.repository.CourseRepository;
import com.ege.repository.ResearchAssistantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseAssistantService {

    @Autowired
    private CourseAssistantRepository courseAssistantRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ResearchAssistantRepository researchAssistantRepository;

    // Tüm course assistant'ları getir
    public List<CourseAssistantDto> getAllCourseAssistants() {
        return courseAssistantRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Course ID'ye göre assistant'ları getir
    public List<CourseAssistantDto> getAssistantsByCourseId(Long courseId) {
        return courseAssistantRepository.findByCourseId(courseId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Assistant ID'ye göre course'ları getir
    public List<CourseAssistantDto> getCoursesByAssistantId(Long assistantId) {
        return courseAssistantRepository.findByAssistantId(assistantId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Course code'a göre assistant'ları getir
    public List<CourseAssistantDto> getAssistantsByCourseCode(String courseCode) {
        return courseAssistantRepository.findByCourseCode(courseCode).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Employee ID'ye göre course'ları getir
    public List<CourseAssistantDto> getCoursesByEmployeeId(String employeeId) {
        return courseAssistantRepository.findByAssistantEmployeeId(employeeId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Departmana göre course assistant'ları getir
    public List<CourseAssistantDto> getCourseAssistantsByDepartment(String department) {
        return courseAssistantRepository.findByCourseDepartment(department).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Semester'a göre course assistant'ları getir
    public List<CourseAssistantDto> getCourseAssistantsBySemester(String semester) {
        return courseAssistantRepository.findByCourseSemester(semester).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Aktif assistant'ların course'larını getir
    public List<CourseAssistantDto> getActiveAssistantCourses() {
        return courseAssistantRepository.findByActiveAssistants().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Dual role assistant'ların course'larını getir
    public List<CourseAssistantDto> getStudentAssistantCourses() {
        return courseAssistantRepository.findByStudentAssistants().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Sadece çalışan assistant'ların course'larını getir
    public List<CourseAssistantDto> getNonStudentAssistantCourses() {
        return courseAssistantRepository.findByNonStudentAssistants().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Belirli bir course assistant ilişkisini getir
    public Optional<CourseAssistantDto> getCourseAssistant(Long courseId, Long assistantId) {
        CourseAssistantId id = new CourseAssistantId();
        id.setCourseId(courseId);
        id.setAssistantId(assistantId);

        return courseAssistantRepository.findById(id)
                .map(this::convertToDto);
    }

    // Course assistant ataması yap
    public CourseAssistantDto assignAssistantToCourse(Long courseId, Long assistantId) {
        // Course ve Assistant'ın var olup olmadığını kontrol et
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        ResearchAssistant assistant = researchAssistantRepository.findById(assistantId)
                .orElseThrow(() -> new RuntimeException("Research Assistant not found with id: " + assistantId));

        // Assistant'ın aktif olup olmadığını kontrol et
        if (!assistant.getUser().getIsActive()) {
            throw new RuntimeException("Cannot assign inactive research assistant to course");
        }

        // Bu atama zaten var mı kontrol et
        if (courseAssistantRepository.existsByIdCourseIdAndIdAssistantId(courseId, assistantId)) {
            throw new RuntimeException("Assistant is already assigned to this course");
        }

        // Yeni CourseAssistant oluştur
        CourseAssistant courseAssistant = new CourseAssistant();
        CourseAssistantId id = new CourseAssistantId();
        id.setCourseId(courseId);
        id.setAssistantId(assistantId);

        courseAssistant.setId(id);
        courseAssistant.setCourse(course);
        courseAssistant.setAssistant(assistant);

        CourseAssistant savedCourseAssistant = courseAssistantRepository.save(courseAssistant);
        return convertToDto(savedCourseAssistant);
    }

    // Course assistant atamasını kaldır
    public void removeAssistantFromCourse(Long courseId, Long assistantId) {
        CourseAssistantId id = new CourseAssistantId();
        id.setCourseId(courseId);
        id.setAssistantId(assistantId);

        CourseAssistant courseAssistant = courseAssistantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course Assistant assignment not found"));

        courseAssistantRepository.delete(courseAssistant);
    }

    // Assistant'ın tüm course atamalarını kaldır
    public void removeAllCourseAssignments(Long assistantId) {
        List<CourseAssistant> assignments = courseAssistantRepository.findByAssistantId(assistantId);
        courseAssistantRepository.deleteAll(assignments);
    }

    // Course'un tüm assistant atamalarını kaldır
    public void removeAllAssistantAssignments(Long courseId) {
        List<CourseAssistant> assignments = courseAssistantRepository.findByCourseId(courseId);
        courseAssistantRepository.deleteAll(assignments);
    }

    // İstatistikler
    public List<Object[]> getAssistantCountByDepartment() {
        return courseAssistantRepository.getAssistantCountByDepartment();
    }

    public List<Object[]> getAssistantCountByCourse() {
        return courseAssistantRepository.getAssistantCountByCourse();
    }

    public List<Object[]> getCourseCountByAssistant() {
        return courseAssistantRepository.getCourseCountByAssistant();
    }

    public List<Object[]> getCourseAssistantCountBySemester() {
        return courseAssistantRepository.getCourseAssistantCountBySemester();
    }

    public List<Object[]> getDualRoleAnalysis() {
        return courseAssistantRepository.getDualRoleAnalysis();
    }

    public List<Object[]> getCoursesWithMostAssistants(int minAssistants) {
        return courseAssistantRepository.getCoursesWithMostAssistants(minAssistants);
    }

    public List<Object[]> getAssistantsWithMostCourses(int minCourses) {
        return courseAssistantRepository.getAssistantsWithMostCourses(minCourses);
    }

    // Validasyon metodları
    private void validateCourseAssistantAssignment(Long courseId, Long assistantId) {
        if (courseId == null) {
            throw new RuntimeException("Course ID is required");
        }
        if (assistantId == null) {
            throw new RuntimeException("Assistant ID is required");
        }
    }

    // Entity'yi DTO'ya çevir
    private CourseAssistantDto convertToDto(CourseAssistant courseAssistant) {
        CourseAssistantDto dto = new CourseAssistantDto();
        dto.setCourseId(courseAssistant.getId().getCourseId());
        dto.setAssistantId(courseAssistant.getId().getAssistantId());

        // Course summary
        if (courseAssistant.getCourse() != null) {
            CourseAssistantDto.CourseSummary courseSummary = new CourseAssistantDto.CourseSummary();
            courseSummary.setId(courseAssistant.getCourse().getId());
            courseSummary.setCourseCode(courseAssistant.getCourse().getCode());
            courseSummary.setCourseName(courseAssistant.getCourse().getName());
            // Department bilgisi instructor'dan geliyor
            if (courseAssistant.getCourse().getInstructor() != null) {
                courseSummary.setDepartment(courseAssistant.getCourse().getInstructor().getDepartment());
            }
            courseSummary.setCredits(courseAssistant.getCourse().getCredits());
            courseSummary.setSemester(courseAssistant.getCourse().getSemester());
            dto.setCourse(courseSummary);
        }

        // Assistant summary
        if (courseAssistant.getAssistant() != null) {
            CourseAssistantDto.AssistantSummary assistantSummary = new CourseAssistantDto.AssistantSummary();
            assistantSummary.setId(courseAssistant.getAssistant().getId());
            assistantSummary.setFirstName(courseAssistant.getAssistant().getUser().getFirstName());
            assistantSummary.setLastName(courseAssistant.getAssistant().getUser().getLastName());
            assistantSummary.setEmail(courseAssistant.getAssistant().getUser().getEmail());
            assistantSummary.setEmployeeId(courseAssistant.getAssistant().getUser().getEmployeeId());
            assistantSummary.setDepartment(courseAssistant.getAssistant().getDepartment());
            dto.setAssistant(assistantSummary);
        }

        return dto;
    }
}