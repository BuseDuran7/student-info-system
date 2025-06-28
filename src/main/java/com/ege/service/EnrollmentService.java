package com.ege.service;

import com.ege.dto.EnrollmentDto;
import com.ege.dto.CourseEnrollmentRequestDto;
import com.ege.entities.Enrollment;
import com.ege.entities.Student;
import com.ege.entities.Course;
import com.ege.entities.enums.EnrollmentStatus;
import com.ege.entities.enums.CourseLevel;
import com.ege.entities.enums.ProgramType;
import com.ege.repository.EnrollmentRepository;
import com.ege.repository.StudentRepository;
import com.ege.repository.CourseRepository;
import com.ege.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    // Tüm enrollments'ları getir
    public List<EnrollmentDto> getAllEnrollments() {
        return enrollmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Aktif enrollments'ları getir
    public List<EnrollmentDto> getActiveEnrollments() {
        return enrollmentRepository.findActiveEnrollments().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ID ile enrollment getir
    public Optional<EnrollmentDto> getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .map(this::convertToDto);
    }

    // Student'a göre enrollments
    public List<EnrollmentDto> getEnrollmentsByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Student'ın aktif enrollments
    public List<EnrollmentDto> getActiveEnrollmentsByStudentId(Long studentId) {
        return enrollmentRepository.findActiveEnrollmentsByStudentId(studentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Course'a göre enrollments
    public List<EnrollmentDto> getEnrollmentsByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Course'un aktif enrollments
    public List<EnrollmentDto> getActiveEnrollmentsByCourseId(Long courseId) {
        return enrollmentRepository.findActiveEnrollmentsByCourseId(courseId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Status'a göre enrollments
    public List<EnrollmentDto> getEnrollmentsByStatus(EnrollmentStatus status) {
        return enrollmentRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Student ve status'a göre enrollments
    public List<EnrollmentDto> getEnrollmentsByStudentIdAndStatus(Long studentId, EnrollmentStatus status) {
        return enrollmentRepository.findByStudentIdAndStatus(studentId, status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Öğretim üyesine göre enrollments
    public List<EnrollmentDto> getEnrollmentsByInstructorId(Long instructorId) {
        return enrollmentRepository.findByInstructorId(instructorId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Semester ve yıla göre enrollments
    public List<EnrollmentDto> getEnrollmentsBySemesterAndYear(String semester, Integer year) {
        return enrollmentRepository.findBySemesterAndYear(semester, year).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Student'ın semester ve yıla göre enrollments
    public List<EnrollmentDto> getEnrollmentsByStudentIdAndSemesterAndYear(Long studentId, String semester, Integer year) {
        return enrollmentRepository.findByStudentIdAndSemesterAndYear(studentId, semester, year).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Yeni enrollment oluştur
    public EnrollmentDto createEnrollment(CourseEnrollmentRequestDto requestDto) {
        // Student kontrolü
        Student student = studentRepository.findById(requestDto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + requestDto.getStudentId()));

        // Course kontrolü
        Course course = courseRepository.findById(requestDto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + requestDto.getCourseId()));

        // Validasyonlar
        validateEnrollment(student, course);

        // Enrollment oluştur
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus(EnrollmentStatus.KAYITLI);

        // Course enrollment sayısını artır
        courseService.incrementEnrollment(course.getId());

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return convertToDto(savedEnrollment);
    }

    // Enrollment status güncelle
    public EnrollmentDto updateEnrollmentStatus(Long enrollmentId, EnrollmentStatus newStatus) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + enrollmentId));

        EnrollmentStatus oldStatus = enrollment.getStatus();
        enrollment.setStatus(newStatus);

        // Status değişimlerine göre course enrollment sayısını güncelle
        if (oldStatus == EnrollmentStatus.KAYITLI && newStatus != EnrollmentStatus.KAYITLI) {
            // Aktif enrollment'tan çıktı (drop, tamamladı)
            courseService.decrementEnrollment(enrollment.getCourse().getId());
        } else if (oldStatus != EnrollmentStatus.KAYITLI && newStatus == EnrollmentStatus.KAYITLI) {
            // Aktif enrollment'a döndü (çok nadir)
            courseService.incrementEnrollment(enrollment.getCourse().getId());
        }

        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return convertToDto(updatedEnrollment);
    }

    // Enrollment drop et
    public EnrollmentDto dropEnrollment(Long enrollmentId) {
        return updateEnrollmentStatus(enrollmentId, EnrollmentStatus.BIRAKTI);
    }

    // Enrollment tamamla
    public EnrollmentDto completeEnrollment(Long enrollmentId) {
        return updateEnrollmentStatus(enrollmentId, EnrollmentStatus.TAMAMLADI);
    }

    // Enrollment sil
    public void deleteEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + enrollmentId));

        // Eğer aktif enrollment ise course sayısını azalt
        if (enrollment.getStatus() == EnrollmentStatus.KAYITLI) {
            courseService.decrementEnrollment(enrollment.getCourse().getId());
        }

        enrollmentRepository.delete(enrollment);
    }

    // Student'ın course'a kayıt olup olmadığını kontrol et
    public boolean isStudentEnrolledInCourse(Long studentId, Long courseId) {
        Optional<Enrollment> enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
        return enrollment.isPresent() && enrollment.get().getStatus() == EnrollmentStatus.KAYITLI;
    }

    // Student'ın toplam enrollment sayısı
    public Long getStudentTotalEnrollmentCount(Long studentId) {
        return enrollmentRepository.countByStudentId(studentId);
    }

    // Student'ın aktif enrollment sayısı
    public Long getStudentActiveEnrollmentCount(Long studentId) {
        return enrollmentRepository.countActiveEnrollmentsByStudentId(studentId);
    }

    // Course'un aktif enrollment sayısı
    public Long getCourseActiveEnrollmentCount(Long courseId) {
        return enrollmentRepository.countActiveEnrollmentsByCourseId(courseId);
    }

    // Duplicate enrollment'ları bul
    public List<EnrollmentDto> findDuplicateEnrollments() {
        return enrollmentRepository.findDuplicateEnrollments().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // İstatistikler
    public List<Object[]> getEnrollmentStatisticsByStatus() {
        return enrollmentRepository.getEnrollmentCountByStatus();
    }

    public List<Object[]> getEnrollmentStatisticsByCourse() {
        return enrollmentRepository.getEnrollmentCountByCourse();
    }

    public List<Object[]> getEnrollmentStatisticsByProgram() {
        return enrollmentRepository.getEnrollmentCountByProgram();
    }

    public List<Object[]> getEnrollmentStatisticsBySemester() {
        return enrollmentRepository.getEnrollmentCountBySemester();
    }

    public List<Object[]> getEnrollmentStatisticsByInstructor() {
        return enrollmentRepository.getEnrollmentCountByInstructor();
    }

    public List<Object[]> getDropRateByCourse() {
        return enrollmentRepository.getDropRateByCourse();
    }

    public List<Object[]> getStudentCourseLoad() {
        return enrollmentRepository.getStudentCourseLoad();
    }

    // Enrollment validasyonu
    private void validateEnrollment(Student student, Course course) {
        // 1. Aynı course'a zaten kayıtlı mı kontrol et
        Optional<Enrollment> existingEnrollment = enrollmentRepository.findByStudentAndCourse(student, course);
        if (existingEnrollment.isPresent() && existingEnrollment.get().getStatus() == EnrollmentStatus.KAYITLI) {
            throw new RuntimeException("Student is already enrolled in this course");
        }

        // 2. Course aktif mi kontrol et
        if (!course.getIsActive()) {
            throw new RuntimeException("Course is not active");
        }

        // 3. Course kapasitesi dolu mu kontrol et
        if (course.getCurrentEnrollment() >= course.getMaxCapacity()) {
            throw new RuntimeException("Course is full. Max capacity: " + course.getMaxCapacity());
        }

        // 4. Student aktif mi kontrol et
        if (!student.getUser().getIsActive()) {
            throw new RuntimeException("Student is not active");
        }

        // 5. Course level ve student program uyumluluğu
        validateCourseLevelCompatibility(student, course);

        // 6. Student'ın aynı semester'da çok fazla ders alıp almadığını kontrol et (isteğe bağlı)
        validateStudentCourseLoad(student, course);
    }

    // Course level ve student program uyumluluğu kontrolü
    private void validateCourseLevelCompatibility(Student student, Course course) {
        ProgramType studentProgramType = student.getProgram().getType();
        CourseLevel courseLevel = course.getLevel();

        switch (courseLevel) {
            case BILIMSEL_HAZIRLIK:
                if (studentProgramType != ProgramType.BILIMSEL_HAZIRLIK) {
                    throw new RuntimeException("This course is only for scientific preparation students");
                }
                break;
            case SADECE_YL:
                if (studentProgramType != ProgramType.TEZLI_YL && studentProgramType != ProgramType.TEZSIZ_YL) {
                    throw new RuntimeException("This course is only for master's students");
                }
                break;
            case SADECE_DOKTORA:
                if (studentProgramType != ProgramType.DOKTORA) {
                    throw new RuntimeException("This course is only for PhD students");
                }
                break;
            case HER_IKISI:
                if (studentProgramType == ProgramType.BILIMSEL_HAZIRLIK) {
                    throw new RuntimeException("Scientific preparation students cannot take this course");
                }
                break;
        }
    }

    // Student'ın ders yükü kontrolü
    private void validateStudentCourseLoad(Student student, Course course) {
        // Aynı semester'da maksimum ders sayısı kontrolü
        Long currentSemesterEnrollments = enrollmentRepository.findByStudentIdAndSemesterAndYear(
                        student.getId(), course.getSemester(), course.getYear()).stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.KAYITLI)
                .count();

        // Program tipine göre maksimum ders sayısı
        int maxCoursesPerSemester = getMaxCoursesPerSemester(student.getProgram().getType());

        if (currentSemesterEnrollments >= maxCoursesPerSemester) {
            throw new RuntimeException("Student has reached maximum course limit for this semester: " + maxCoursesPerSemester);
        }
    }

    // Program tipine göre maksimum ders sayısı
    private int getMaxCoursesPerSemester(ProgramType programType) {
        return switch (programType) {
            case BILIMSEL_HAZIRLIK -> 4;
            case TEZLI_YL, TEZSIZ_YL -> 5;
            case DOKTORA -> 4;
        };
    }

    // Entity'yi DTO'ya çevir
    private EnrollmentDto convertToDto(Enrollment enrollment) {
        EnrollmentDto dto = new EnrollmentDto();
        dto.setId(enrollment.getId());
        dto.setCourseCode(enrollment.getCourse().getCode());
        dto.setCourseName(enrollment.getCourse().getName());
        dto.setCredits(enrollment.getCourse().getCredits());
        dto.setSemester(enrollment.getCourse().getSemester());
        dto.setYear(enrollment.getCourse().getYear());
        dto.setInstructorName(enrollment.getCourse().getInstructor().getUser().getFirstName() + " " +
                enrollment.getCourse().getInstructor().getUser().getLastName());
        dto.setEnrollmentDate(enrollment.getEnrollmentDate());
        dto.setStatus(enrollment.getStatus());
        return dto;
    }
}
