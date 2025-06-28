package com.ege.service;

import com.ege.dto.GradeDto;
import com.ege.dto.GradeEntryDto;
import com.ege.entities.Grade;
import com.ege.entities.Student;
import com.ege.entities.Course;
import com.ege.entities.AcademicStaff;
import com.ege.entities.enums.LetterGrade;
import com.ege.entities.enums.ProgramType;
import com.ege.repository.GradeRepository;
import com.ege.repository.StudentRepository;
import com.ege.repository.CourseRepository;
import com.ege.repository.AcademicStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AcademicStaffRepository academicStaffRepository;

    // Tüm notları getir
    public List<GradeDto> getAllGrades() {
        return gradeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ID ile not getir
    public Optional<GradeDto> getGradeById(Long id) {
        return gradeRepository.findById(id)
                .map(this::convertToDto);
    }

    // Student'a göre notları getir
    public List<GradeDto> getGradesByStudentId(Long studentId) {
        return gradeRepository.findByStudentId(studentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Student'ın geçtiği dersler
    public List<GradeDto> getPassedGradesByStudentId(Long studentId) {
        return gradeRepository.findPassedGradesByStudentId(studentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Student'ın kaldığı dersler
    public List<GradeDto> getFailedGradesByStudentId(Long studentId) {
        return gradeRepository.findFailedGradesByStudentId(studentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Course'a göre notları getir
    public List<GradeDto> getGradesByCourseId(Long courseId) {
        return gradeRepository.findByCourseId(courseId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Course'da geçen öğrenciler
    public List<GradeDto> getPassedGradesByCourseId(Long courseId) {
        return gradeRepository.findPassedGradesByCourseId(courseId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Course'da kalan öğrenciler
    public List<GradeDto> getFailedGradesByCourseId(Long courseId) {
        return gradeRepository.findFailedGradesByCourseId(courseId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Öğretim üyesinin verdiği notlar
    public List<GradeDto> getGradesByInstructorId(Long instructorId) {
        return gradeRepository.findByGradedById(instructorId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Letter grade'e göre notlar
    public List<GradeDto> getGradesByLetterGrade(LetterGrade letterGrade) {
        return gradeRepository.findByLetterGrade(letterGrade).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // En yüksek notlar
    public List<GradeDto> getTopGrades(int limit) {
        return gradeRepository.findTopGrades().stream()
                .limit(limit)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // En düşük notlar
    public List<GradeDto> getBottomGrades(int limit) {
        return gradeRepository.findBottomGrades().stream()
                .limit(limit)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Course'da en yüksek notlar
    public List<GradeDto> getTopGradesByCourseId(Long courseId, int limit) {
        return gradeRepository.findTopGradesByCourseId(courseId).stream()
                .limit(limit)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Yeni not oluştur/güncelle
    public GradeDto createOrUpdateGrade(GradeEntryDto gradeEntryDto) {
        // Student kontrolü
        Student student = studentRepository.findById(gradeEntryDto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + gradeEntryDto.getStudentId()));

        // Course kontrolü
        Course course = courseRepository.findById(gradeEntryDto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + gradeEntryDto.getCourseId()));

        // Mevcut not var mı kontrol et
        Optional<Grade> existingGrade = gradeRepository.findByStudentAndCourse(student, course);

        Grade grade;
        if (existingGrade.isPresent()) {
            grade = existingGrade.get();
        } else {
            grade = new Grade();
            grade.setStudent(student);
            grade.setCourse(course);
            grade.setGradeDate(LocalDate.now());
        }

        // Not bilgilerini güncelle
        updateGradeFields(grade, gradeEntryDto, student);

        Grade savedGrade = gradeRepository.save(grade);

        // Student'ın GPA'sını güncelle
        updateStudentGpa(student);

        return convertToDto(savedGrade);
    }

    // Not güncelle
    public GradeDto updateGrade(Long gradeId, GradeEntryDto gradeEntryDto) {
        Grade existingGrade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new RuntimeException("Grade not found with id: " + gradeId));

        updateGradeFields(existingGrade, gradeEntryDto, existingGrade.getStudent());

        Grade updatedGrade = gradeRepository.save(existingGrade);

        // Student'ın GPA'sını güncelle
        updateStudentGpa(existingGrade.getStudent());

        return convertToDto(updatedGrade);
    }

    // Not sil
    public void deleteGrade(Long gradeId) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new RuntimeException("Grade not found with id: " + gradeId));

        Student student = grade.getStudent();
        gradeRepository.delete(grade);

        // Student'ın GPA'sını güncelle
        updateStudentGpa(student);
    }

    // Student'ın transcript'ini getir
    public List<GradeDto> getStudentTranscript(Long studentId) {
        return gradeRepository.findByStudentId(studentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Student'ın GPA'sını hesapla ve güncelle
    public void updateStudentGpa(Student student) {
        List<Grade> passedGrades = gradeRepository.findForGpaCalculation(student.getId());

        if (passedGrades.isEmpty()) {
            student.setGpa(BigDecimal.ZERO);
            studentRepository.save(student);
            return;
        }

        BigDecimal totalPoints = BigDecimal.ZERO;
        int totalCredits = 0;

        for (Grade grade : passedGrades) {
            BigDecimal gradePoints = getGradePoints(grade.getLetterGrade());
            int courseCredits = grade.getCourse().getCredits();

            totalPoints = totalPoints.add(gradePoints.multiply(BigDecimal.valueOf(courseCredits)));
            totalCredits += courseCredits;
        }

        BigDecimal gpa = totalCredits > 0 ?
                totalPoints.divide(BigDecimal.valueOf(totalCredits), 2, RoundingMode.HALF_UP) :
                BigDecimal.ZERO;

        student.setGpa(gpa);

        // Completed courses ve total credits'i de güncelle
        student.setCompletedCourses(passedGrades.size());
        student.setTotalCredits(totalCredits);

        studentRepository.save(student);
    }

    // Course ortalama notu
    public Double getCourseAverageGrade(Long courseId) {
        return gradeRepository.getAverageGradeByCourseId(courseId);
    }

    // Student ortalama notu
    public Double getStudentAverageGrade(Long studentId) {
        return gradeRepository.getAverageGradeByStudentId(studentId);
    }

    // Course geçme oranı
    public Double getCoursePassRate(Long courseId) {
        return gradeRepository.getPassRateByCourseId(courseId);
    }

    // Öğretim üyesi geçme oranı
    public Double getInstructorPassRate(Long instructorId) {
        return gradeRepository.getPassRateByInstructorId(instructorId);
    }

    // Letter grade dağılımı - Course bazında
    public List<Object[]> getLetterGradeDistributionByCourse(Long courseId) {
        return gradeRepository.getLetterGradeDistributionByCourseId(courseId);
    }

    // Letter grade dağılımı - Student bazında
    public List<Object[]> getLetterGradeDistributionByStudent(Long studentId) {
        return gradeRepository.getLetterGradeDistributionByStudentId(studentId);
    }

    // İstatistikler
    public List<Object[]> getCourseGradeStatistics() {
        return gradeRepository.getCourseGradeStatistics();
    }

    public List<Object[]> getStudentGradeStatistics() {
        return gradeRepository.getStudentGradeStatistics();
    }

    public List<Object[]> getInstructorGradeStatistics() {
        return gradeRepository.getInstructorGradeStatistics();
    }

    public List<Object[]> getProgramGradeStatistics() {
        return gradeRepository.getProgramGradeStatistics();
    }

    public List<Object[]> getSemesterGradeStatistics() {
        return gradeRepository.getSemesterGradeStatistics();
    }

    public List<Object[]> getGradeRangeDistribution() {
        return gradeRepository.getGradeRangeDistribution();
    }

    // Grade fields güncelleme
    private void updateGradeFields(Grade grade, GradeEntryDto gradeEntryDto, Student student) {
        // Numeric grade set et
        grade.setNumericGrade(gradeEntryDto.getNumericGrade());

        // Letter grade hesapla (eğer gönderilmemişse)
        LetterGrade letterGrade = gradeEntryDto.getLetterGrade() != null ?
                gradeEntryDto.getLetterGrade() :
                calculateLetterGrade(gradeEntryDto.getNumericGrade());
        grade.setLetterGrade(letterGrade);

        // Pass durumu hesapla
        boolean isPassed = gradeEntryDto.getIsPassed() != null ?
                gradeEntryDto.getIsPassed() :
                calculatePassStatus(gradeEntryDto.getNumericGrade(), student);
        grade.setIsPassed(isPassed);

        grade.setGradeDate(LocalDate.now());

        // Graded by set et (course instructor'ı default)
        grade.setGradedBy(grade.getCourse().getInstructor());
    }

    // Numeric grade'den letter grade hesapla
    private LetterGrade calculateLetterGrade(BigDecimal numericGrade) {
        double grade = numericGrade.doubleValue();

        if (grade >= 90) return LetterGrade.AA;
        else if (grade >= 85) return LetterGrade.BA;
        else if (grade >= 80) return LetterGrade.BB;
        else if (grade >= 75) return LetterGrade.CB;
        else if (grade >= 70) return LetterGrade.CC;
        else if (grade >= 65) return LetterGrade.DC;
        else if (grade >= 60) return LetterGrade.DD;
        else if (grade >= 50) return LetterGrade.FD;
        else return LetterGrade.FF;
    }

    // Program tipine göre geçme durumu hesapla
    private boolean calculatePassStatus(BigDecimal numericGrade, Student student) {
        ProgramType programType = student.getProgram().getType();
        double grade = numericGrade.doubleValue();

        return switch (programType) {
            case DOKTORA -> grade >= 75;
            case TEZLI_YL, TEZSIZ_YL, BILIMSEL_HAZIRLIK -> grade >= 70;
        };
    }

    // Letter grade'den grade points hesapla (4.0 scale)
    private BigDecimal getGradePoints(LetterGrade letterGrade) {
        return switch (letterGrade) {
            case AA -> BigDecimal.valueOf(4.0);
            case BA -> BigDecimal.valueOf(3.5);
            case BB -> BigDecimal.valueOf(3.0);
            case CB -> BigDecimal.valueOf(2.5);
            case CC -> BigDecimal.valueOf(2.0);
            case DC -> BigDecimal.valueOf(1.5);
            case DD -> BigDecimal.valueOf(1.0);
            case FD -> BigDecimal.valueOf(0.5);
            case FF -> BigDecimal.valueOf(0.0);
        };
    }

    // Entity'yi DTO'ya çevir
    private GradeDto convertToDto(Grade grade) {
        GradeDto dto = new GradeDto();
        dto.setId(grade.getId());
        dto.setCourseCode(grade.getCourse().getCode());
        dto.setCourseName(grade.getCourse().getName());
        dto.setCredits(grade.getCourse().getCredits());
        dto.setNumericGrade(grade.getNumericGrade());
        dto.setLetterGrade(grade.getLetterGrade());
        dto.setPassed(grade.getIsPassed());
        dto.setGradeDate(grade.getGradeDate());

        if (grade.getGradedBy() != null) {
            dto.setGradedBy(grade.getGradedBy().getUser().getFirstName() + " " +
                    grade.getGradedBy().getUser().getLastName());
        }

        return dto;
    }
}