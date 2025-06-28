package com.ege.service;

import com.ege.dto.CourseDto;
import com.ege.entities.Course;
import com.ege.entities.AcademicStaff;
import com.ege.entities.enums.CourseLevel;
import com.ege.repository.CourseRepository;
import com.ege.repository.AcademicStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AcademicStaffRepository academicStaffRepository;

    // Tüm dersleri getir
    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Aktif dersleri getir
    public List<CourseDto> getActiveCourses() {
        return courseRepository.findActiveCoursesOrderByCode().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ID ile ders getir
    public Optional<CourseDto> getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(this::convertToDto);
    }

    // Course code ile ders getir
    public Optional<CourseDto> getCourseByCode(String code) {
        return courseRepository.findByCode(code)
                .map(this::convertToDto);
    }

    // Ders adında arama yap
    public List<CourseDto> searchCoursesByName(String name) {
        return courseRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Öğretim üyesine göre dersleri getir
    public List<CourseDto> getCoursesByInstructorId(Long instructorId) {
        return courseRepository.findActiveByInstructorId(instructorId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Course level'a göre dersleri getir
    public List<CourseDto> getCoursesByLevel(CourseLevel level) {
        return courseRepository.findByLevelAndIsActiveTrue(level).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Semester ve yıla göre dersleri getir
    public List<CourseDto> getCoursesBySemesterAndYear(String semester, Integer year) {
        return courseRepository.findBySemesterAndYear(semester, year).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Kapasitesi dolu olmayan dersleri getir
    public List<CourseDto> getCoursesWithAvailableCapacity() {
        return courseRepository.findCoursesWithAvailableCapacity().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Kapasitesi dolu olan dersleri getir
    public List<CourseDto> getFullCourses() {
        return courseRepository.findFullCourses().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Düşük kayıtlı dersleri getir
    public List<CourseDto> getCoursesWithLowEnrollment(Integer minEnrollment) {
        return courseRepository.findCoursesWithLowEnrollment(minEnrollment).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Departmana göre dersleri getir
    public List<CourseDto> getCoursesByDepartment(String department) {
        return courseRepository.findByInstructorDepartment(department).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // En çok kayıtlı dersleri getir
    public List<CourseDto> getMostEnrolledCourses(int limit) {
        return courseRepository.findMostEnrolledCourses().stream()
                .limit(limit)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // En az kayıtlı dersleri getir
    public List<CourseDto> getLeastEnrolledCourses(int limit) {
        return courseRepository.findLeastEnrolledCourses().stream()
                .limit(limit)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Yeni ders oluştur
    public CourseDto createCourse(CourseDto courseDto) {
        // Validasyon
        validateCourseDto(courseDto);

        // Course code kontrolü
        if (courseRepository.existsByCode(courseDto.getCode())) {
            throw new RuntimeException("Course code already exists: " + courseDto.getCode());
        }

        // Öğretim üyesi kontrolü
        AcademicStaff instructor = academicStaffRepository.findById(courseDto.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + courseDto.getInstructorId()));

        Course course = convertToEntity(courseDto);
        course.setInstructor(instructor);
        course.setIsActive(true);
        course.setCurrentEnrollment(0); // Yeni ders, kayıt yok

        Course savedCourse = courseRepository.save(course);
        return convertToDto(savedCourse);
    }

    // Ders güncelle
    public CourseDto updateCourse(Long id, CourseDto courseDto) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        // Validasyon
        validateCourseDto(courseDto);

        // Course code kontrolü (değişiyorsa)
        if (!existingCourse.getCode().equals(courseDto.getCode()) &&
                courseRepository.existsByCode(courseDto.getCode())) {
            throw new RuntimeException("Course code already exists: " + courseDto.getCode());
        }

        // Öğretim üyesi kontrolü
        AcademicStaff instructor = academicStaffRepository.findById(courseDto.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + courseDto.getInstructorId()));

        updateCourseFields(existingCourse, courseDto, instructor);

        Course updatedCourse = courseRepository.save(existingCourse);
        return convertToDto(updatedCourse);
    }

    // Ders kapasitesini güncelle
    public CourseDto updateCourseCapacity(Long courseId, Integer newCapacity) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        if (newCapacity < course.getCurrentEnrollment()) {
            throw new RuntimeException("New capacity cannot be less than current enrollment: " + course.getCurrentEnrollment());
        }

        course.setMaxCapacity(newCapacity);
        Course updatedCourse = courseRepository.save(course);
        return convertToDto(updatedCourse);
    }

    // Ders kayıt sayısını artır (Enrollment oluşturulduğunda çağrılacak)
    public void incrementEnrollment(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        if (course.getCurrentEnrollment() >= course.getMaxCapacity()) {
            throw new RuntimeException("Course is full. Cannot enroll more students.");
        }

        course.setCurrentEnrollment(course.getCurrentEnrollment() + 1);
        courseRepository.save(course);
    }

    // Ders kayıt sayısını azalt (Enrollment silindiğinde çağrılacak)
    public void decrementEnrollment(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        if (course.getCurrentEnrollment() > 0) {
            course.setCurrentEnrollment(course.getCurrentEnrollment() - 1);
            courseRepository.save(course);
        }
    }

    // Ders sil (soft delete)
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        // Eğer derste kayıtlı öğrenci varsa silinmesine izin verme
        if (course.getCurrentEnrollment() > 0) {
            throw new RuntimeException("Cannot delete course with enrolled students. Current enrollment: " + course.getCurrentEnrollment());
        }

        course.setIsActive(false);
        courseRepository.save(course);
    }

    // Ders aktifleştir
    public CourseDto activateCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        course.setIsActive(true);
        Course updatedCourse = courseRepository.save(course);
        return convertToDto(updatedCourse);
    }

    // İstatistikler
    public List<Object[]> getCourseStatisticsByLevel() {
        return courseRepository.getCourseCountByLevel();
    }

    public List<Object[]> getCourseStatisticsByInstructor() {
        return courseRepository.getCourseCountByInstructor();
    }

    public List<Object[]> getCourseStatisticsBySemester() {
        return courseRepository.getCourseCountBySemester();
    }

    public List<Object[]> getCourseStatisticsByDepartment() {
        return courseRepository.getCourseCountByDepartment();
    }

    public Double getAverageMaxCapacity() {
        return courseRepository.getAverageMaxCapacity();
    }

    public Double getAverageEnrollmentRate() {
        return courseRepository.getAverageEnrollmentRate();
    }

    // Ders DTO validasyonu
    private void validateCourseDto(CourseDto dto) {
        if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
            throw new RuntimeException("Course code is required");
        }
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new RuntimeException("Course name is required");
        }
        if (dto.getCredits() == null || dto.getCredits() <= 0) {
            throw new RuntimeException("Credits must be greater than 0");
        }
        if (dto.getLevel() == null) {
            throw new RuntimeException("Course level is required");
        }
        if (dto.getMaxCapacity() == null || dto.getMaxCapacity() <= 0) {
            throw new RuntimeException("Max capacity must be greater than 0");
        }
        if (dto.getInstructorId() == null) {
            throw new RuntimeException("Instructor is required");
        }
        if (dto.getYear() != null && dto.getYear() < 2000) {
            throw new RuntimeException("Invalid year");
        }

        // Course code format kontrolü (örnek: BMU501)
        if (!dto.getCode().matches("[A-Z]{3}\\d{3}")) {
            throw new RuntimeException("Course code must be in format ABC123 (e.g., BMU501)");
        }
    }

    // Mevcut ders alanlarını güncelle
    private void updateCourseFields(Course course, CourseDto dto, AcademicStaff instructor) {
        course.setCode(dto.getCode());
        course.setName(dto.getName());
        course.setCredits(dto.getCredits());
        course.setLevel(dto.getLevel());
        course.setSemester(dto.getSemester());
        course.setYear(dto.getYear());
        course.setMaxCapacity(dto.getMaxCapacity());
        course.setInstructor(instructor);

        if (dto.getIsActive() != null) {
            course.setIsActive(dto.getIsActive());
        }
    }

    // Entity'yi DTO'ya çevir
    private CourseDto convertToDto(Course course) {
        CourseDto dto = new CourseDto();
        dto.setId(course.getId());
        dto.setCode(course.getCode());
        dto.setName(course.getName());
        dto.setCredits(course.getCredits());
        dto.setLevel(course.getLevel());
        dto.setSemester(course.getSemester());
        dto.setYear(course.getYear());
        dto.setMaxCapacity(course.getMaxCapacity());
        dto.setCurrentEnrollment(course.getCurrentEnrollment());
        dto.setInstructorName(course.getInstructor().getUser().getFirstName() + " " +
                course.getInstructor().getUser().getLastName());
        dto.setInstructorId(course.getInstructor().getId());
        dto.setIsActive(course.getIsActive());

        // Assistant names (ileride CourseAssistant entity'si hazır olunca eklenecek)
        dto.setAssistantNames(new ArrayList<>());

        return dto;
    }

    // DTO'yu Entity'ye çevir
    private Course convertToEntity(CourseDto dto) {
        Course course = new Course();
        course.setCode(dto.getCode());
        course.setName(dto.getName());
        course.setCredits(dto.getCredits());
        course.setLevel(dto.getLevel());
        course.setSemester(dto.getSemester());
        course.setYear(dto.getYear());
        course.setMaxCapacity(dto.getMaxCapacity());
        return course;
    }
}
