package com.ege.service;

import com.ege.dto.StudentProfileDto;
import com.ege.dto.AdvisorDto;
import com.ege.dto.GraduationStatusDto;
import com.ege.entities.Student;
import com.ege.entities.User;
import com.ege.entities.Program;
import com.ege.entities.AcademicStaff;
import com.ege.entities.enums.ProgramType;
import com.ege.repository.StudentRepository;
import com.ege.repository.UserRepository;
import com.ege.repository.ProgramRepository;
import com.ege.repository.AcademicStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private AcademicStaffRepository academicStaffRepository;

    // Tüm öğrencileri getir
    public List<StudentProfileDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Aktif öğrencileri getir
    public List<StudentProfileDto> getActiveStudents() {
        return studentRepository.findActiveStudentsOrderByName().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ID ile öğrenci getir
    public Optional<StudentProfileDto> getStudentById(Long id) {
        return studentRepository.findById(id)
                .map(this::convertToDto);
    }

    // User ID ile öğrenci getir
    public Optional<StudentProfileDto> getStudentByUserId(Long userId) {
        return studentRepository.findByUserId(userId)
                .map(this::convertToDto);
    }

    // Username ile öğrenci getir
    public Optional<StudentProfileDto> getStudentByUsername(String username) {
        return studentRepository.findByUsername(username)
                .map(this::convertToDto);
    }

    // Student ID ile öğrenci getir
    public Optional<StudentProfileDto> getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId)
                .map(this::convertToDto);
    }

    // Program'a göre öğrencileri getir
    public List<StudentProfileDto> getStudentsByProgramId(Long programId) {
        return studentRepository.findByProgramId(programId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Program türüne göre öğrencileri getir
    public List<StudentProfileDto> getStudentsByProgramType(ProgramType programType) {
        return studentRepository.findByProgramType(programType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Danışmana göre öğrencileri getir
    public List<StudentProfileDto> getStudentsByAdvisorId(Long advisorId) {
        return studentRepository.findByAdvisorId(advisorId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // İsimde arama yap
    public List<StudentProfileDto> searchStudentsByName(String name) {
        return studentRepository.findByFullNameContainingIgnoreCase(name).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // GPA'ya göre öğrencileri getir
    public List<StudentProfileDto> getStudentsByMinGpa(BigDecimal minGpa) {
        return studentRepository.findByGpaGreaterThanEqual(minGpa).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Mezun olan öğrencileri getir
    public List<StudentProfileDto> getGraduatedStudents() {
        return studentRepository.findByIsGraduatedTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Mezuniyet şartlarını karşılayan öğrencileri getir
    public List<StudentProfileDto> getStudentsEligibleForGraduation() {
        return studentRepository.findStudentsEligibleForGraduation().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Danışmanı olmayan öğrencileri getir
    public List<StudentProfileDto> getStudentsWithoutAdvisor() {
        return studentRepository.findStudentsWithoutAdvisor().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Yeni öğrenci oluştur
    public StudentProfileDto createStudent(StudentProfileDto studentDto) {
        // Validasyon
        validateStudentDto(studentDto);

        // User kontrolü
        User user = userRepository.findById(studentDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + studentDto.getId()));

        // Bu user zaten öğrenci mi?
        if (studentRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("Student already exists for this user");
        }

        // User'ın rolü STUDENT mi kontrol et
        if (!"STUDENT".equals(user.getUserRole().getRoleName())) {
            throw new RuntimeException("User role must be STUDENT");
        }

        // Program kontrolü
        Program program = programRepository.findById(studentDto.getProgramType().ordinal() + 1L) // Basit mapping
                .orElseThrow(() -> new RuntimeException("Program not found"));

        Student student = convertToEntity(studentDto);
        student.setUser(user);
        student.setProgram(program);

        // Danışman atama (eğer belirtilmişse)
        if (studentDto.getAdvisor() != null && studentDto.getAdvisor().getId() != null) {
            AcademicStaff advisor = academicStaffRepository.findById(studentDto.getAdvisor().getId())
                    .orElseThrow(() -> new RuntimeException("Advisor not found"));
            student.setAdvisor(advisor);
        }

        Student savedStudent = studentRepository.save(student);
        return convertToDto(savedStudent);
    }

    // Öğrenci güncelle
    public StudentProfileDto updateStudent(Long id, StudentProfileDto studentDto) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        // Validasyon
        validateStudentDto(studentDto);

        updateStudentFields(existingStudent, studentDto);

        Student updatedStudent = studentRepository.save(existingStudent);
        return convertToDto(updatedStudent);
    }

    // Danışman ata
    public StudentProfileDto assignAdvisor(Long studentId, Long advisorId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        AcademicStaff advisor = academicStaffRepository.findById(advisorId)
                .orElseThrow(() -> new RuntimeException("Advisor not found with id: " + advisorId));

        student.setAdvisor(advisor);
        Student updatedStudent = studentRepository.save(student);
        return convertToDto(updatedStudent);
    }

    // Mezuniyet durumunu kontrol et
    public GraduationStatusDto checkGraduationStatus(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        Program program = student.getProgram();
        GraduationStatusDto graduationStatus = new GraduationStatusDto();

        // Temel bilgiler
        graduationStatus.setStudentId(student.getId());
        graduationStatus.setStudentName(student.getUser().getFirstName() + " " + student.getUser().getLastName());
        graduationStatus.setProgramName(program.getName());

        // Gereksinimler
        graduationStatus.setRequiredCourses(program.getRequiredCourses());
        graduationStatus.setCompletedCourses(student.getCompletedCourses());
        graduationStatus.setCourseRequirementMet(student.getCompletedCourses() >= program.getRequiredCourses());

        graduationStatus.setRequiredCredits(program.getRequiredCredits());
        graduationStatus.setTotalCredits(student.getTotalCredits());
        graduationStatus.setCreditRequirementMet(student.getTotalCredits() >= program.getRequiredCredits());

        graduationStatus.setMinimumGpa(program.getMinimumGpa());
        graduationStatus.setCurrentGpa(student.getGpa());
        graduationStatus.setGpaRequirementMet(student.getGpa().compareTo(program.getMinimumGpa()) >= 0);

        graduationStatus.setThesisRequired(program.getHasThesis());
        graduationStatus.setThesisCompleted(student.getThesisCompleted());
        graduationStatus.setThesisRequirementMet(!program.getHasThesis() || student.getThesisCompleted());

        // Genel mezuniyet durumu
        boolean canGraduate = graduationStatus.isCourseRequirementMet() &&
                graduationStatus.isCreditRequirementMet() &&
                graduationStatus.isGpaRequirementMet() &&
                graduationStatus.isThesisRequirementMet();

        graduationStatus.setCanGraduate(canGraduate);
        graduationStatus.setGraduationStatus(canGraduate ? "Mezuniyet şartlarını karşılıyor" : "Mezuniyet şartları eksik");

        // Eksik gereksinimler
        List<String> missingRequirements = new ArrayList<>();
        if (!graduationStatus.isCourseRequirementMet()) {
            missingRequirements.add((program.getRequiredCourses() - student.getCompletedCourses()) + " ders eksik");
        }
        if (!graduationStatus.isCreditRequirementMet()) {
            missingRequirements.add((program.getRequiredCredits() - student.getTotalCredits()) + " kredi eksik");
        }
        if (!graduationStatus.isGpaRequirementMet()) {
            missingRequirements.add("GPA " + program.getMinimumGpa() + " olmalı (şu anki: " + student.getGpa() + ")");
        }
        if (!graduationStatus.isThesisRequirementMet()) {
            missingRequirements.add("Tez tamamlanmalı");
        }

        graduationStatus.setMissingRequirements(missingRequirements);
        graduationStatus.setNextSteps(canGraduate ? "Mezuniyet işlemleri başlatılabilir" : "Eksik gereksinimler tamamlanmalı");

        return graduationStatus;
    }

    // Öğrenci sil (soft delete)
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        // User'ı da pasif yap
        student.getUser().setIsActive(false);
        userRepository.save(student.getUser());
    }

    // Öğrenci aktifleştir
    public StudentProfileDto activateStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        // User'ı da aktif yap
        student.getUser().setIsActive(true);
        userRepository.save(student.getUser());

        return convertToDto(student);
    }

    // İstatistikler
    public List<Object[]> getStudentStatisticsByProgram() {
        return studentRepository.getStudentCountByProgram();
    }

    public List<Object[]> getStudentStatisticsByAdvisor() {
        return studentRepository.getStudentCountByAdvisor();
    }

    public List<Object[]> getAverageGpaByProgram() {
        return studentRepository.getAverageGpaByProgram();
    }

    // Validasyon
    private void validateStudentDto(StudentProfileDto dto) {
        if (dto.getProgramType() == null) {
            throw new RuntimeException("Program type is required");
        }
        if (dto.getEnrollmentDate() == null) {
            throw new RuntimeException("Enrollment date is required");
        }
        if (dto.getGpa() != null && (dto.getGpa().compareTo(BigDecimal.ZERO) < 0 || dto.getGpa().compareTo(new BigDecimal("4.00")) > 0)) {
            throw new RuntimeException("GPA must be between 0.00 and 4.00");
        }
        if (dto.getTotalCredits() != null && dto.getTotalCredits() < 0) {
            throw new RuntimeException("Total credits cannot be negative");
        }
        if (dto.getCompletedCourses() != null && dto.getCompletedCourses() < 0) {
            throw new RuntimeException("Completed courses cannot be negative");
        }
    }

    // Öğrenci alanlarını güncelle
    private void updateStudentFields(Student student, StudentProfileDto dto) {
        student.setEnrollmentDate(dto.getEnrollmentDate());
        student.setExpectedGraduationDate(dto.getExpectedGraduationDate());
        student.setGpa(dto.getGpa() != null ? dto.getGpa() : BigDecimal.ZERO);
        student.setTotalCredits(dto.getTotalCredits() != null ? dto.getTotalCredits() : 0);
        student.setCompletedCourses(dto.getCompletedCourses() != null ? dto.getCompletedCourses() : 0);
        student.setThesisCompleted(dto.getThesisCompleted() != null ? dto.getThesisCompleted() : false);
        student.setIsGraduated(dto.getGraduated() != null ? dto.getGraduated() : false);

        // Danışman güncelleme
        if (dto.getAdvisor() != null && dto.getAdvisor().getId() != null) {
            AcademicStaff advisor = academicStaffRepository.findById(dto.getAdvisor().getId())
                    .orElse(null);
            student.setAdvisor(advisor);
        }
    }

    // Entity'yi DTO'ya çevir
    private StudentProfileDto convertToDto(Student student) {
        StudentProfileDto dto = new StudentProfileDto();
        dto.setId(student.getId());
        dto.setFirstName(student.getUser().getFirstName());
        dto.setLastName(student.getUser().getLastName());
        dto.setEmail(student.getUser().getEmail());
        dto.setStudentId(student.getUser().getStudentId());
        dto.setProgramName(student.getProgram().getName());
        dto.setProgramType(student.getProgram().getType());
        dto.setEnrollmentDate(student.getEnrollmentDate());
        dto.setExpectedGraduationDate(student.getExpectedGraduationDate());
        dto.setGpa(student.getGpa());
        dto.setTotalCredits(student.getTotalCredits());
        dto.setCompletedCourses(student.getCompletedCourses());
        dto.setThesisCompleted(student.getThesisCompleted());
        dto.setGraduated(student.getIsGraduated());

        // Danışman bilgisi
        if (student.getAdvisor() != null) {
            AdvisorDto advisorDto = new AdvisorDto();
            advisorDto.setId(student.getAdvisor().getId());
            advisorDto.setFirstName(student.getAdvisor().getUser().getFirstName());
            advisorDto.setLastName(student.getAdvisor().getUser().getLastName());
            advisorDto.setTitle(student.getAdvisor().getTitle());
            advisorDto.setDepartment(student.getAdvisor().getDepartment());
            advisorDto.setOfficeRoom(student.getAdvisor().getOfficeRoom());
            advisorDto.setEmail(student.getAdvisor().getUser().getEmail());
            dto.setAdvisor(advisorDto);
        }

        return dto;
    }

    // DTO'yu Entity'ye çevir
    private Student convertToEntity(StudentProfileDto dto) {
        Student student = new Student();
        student.setEnrollmentDate(dto.getEnrollmentDate());
        student.setExpectedGraduationDate(dto.getExpectedGraduationDate());
        student.setGpa(dto.getGpa() != null ? dto.getGpa() : BigDecimal.ZERO);
        student.setTotalCredits(dto.getTotalCredits() != null ? dto.getTotalCredits() : 0);
        student.setCompletedCourses(dto.getCompletedCourses() != null ? dto.getCompletedCourses() : 0);
        student.setThesisCompleted(dto.getThesisCompleted() != null ? dto.getThesisCompleted() : false);
        student.setIsGraduated(dto.getGraduated() != null ? dto.getGraduated() : false);
        return student;
    }
}