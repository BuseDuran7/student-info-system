package com.ege.repository;

import com.ege.entities.Student;
import com.ege.entities.User;
import com.ege.entities.Program;
import com.ege.entities.AcademicStaff;
import com.ege.entities.enums.ProgramType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // User ile öğrenci bul
    Optional<Student> findByUser(User user);

    // User ID ile öğrenci bul
    @Query("SELECT s FROM Student s WHERE s.user.id = :userId")
    Optional<Student> findByUserId(@Param("userId") Long userId);

    // Username ile öğrenci bul
    @Query("SELECT s FROM Student s WHERE s.user.username = :username")
    Optional<Student> findByUsername(@Param("username") String username);

    // Student ID ile öğrenci bul
    @Query("SELECT s FROM Student s WHERE s.user.studentId = :studentId")
    Optional<Student> findByStudentId(@Param("studentId") String studentId);

    // Program'a göre öğrenciler
    List<Student> findByProgram(Program program);

    // Program ID'ye göre öğrenciler
    @Query("SELECT s FROM Student s WHERE s.program.id = :programId")
    List<Student> findByProgramId(@Param("programId") Long programId);

    // Program türüne göre öğrenciler
    @Query("SELECT s FROM Student s WHERE s.program.type = :programType")
    List<Student> findByProgramType(@Param("programType") ProgramType programType);

    // Danışmana göre öğrenciler
    List<Student> findByAdvisor(AcademicStaff advisor);

    // Danışman ID'ye göre öğrenciler
    @Query("SELECT s FROM Student s WHERE s.advisor.id = :advisorId")
    List<Student> findByAdvisorId(@Param("advisorId") Long advisorId);

    // Aktif öğrenciler (User aktif olan)
    @Query("SELECT s FROM Student s WHERE s.user.isActive = true")
    List<Student> findActiveStudents();

    // Mezun olan öğrenciler
    List<Student> findByIsGraduatedTrue();

    // Mezun olmayan öğrenciler
    @Query("SELECT s FROM Student s WHERE s.isGraduated = false AND s.user.isActive = true")
    List<Student> findActiveNonGraduatedStudents();

    // Tezi tamamlamış öğrenciler
    List<Student> findByThesisCompletedTrue();

    // Tezi tamamlamamış öğrenciler
    @Query("SELECT s FROM Student s WHERE s.thesisCompleted = false AND s.user.isActive = true")
    List<Student> findActiveStudentsWithIncompleteThesis();

    // GPA aralığında öğrenciler
    List<Student> findByGpaBetween(BigDecimal minGpa, BigDecimal maxGpa);

    // Minimum GPA'dan yüksek öğrenciler
    List<Student> findByGpaGreaterThanEqual(BigDecimal minGpa);

    // Belirli kredi sayısından fazla alan öğrenciler
    List<Student> findByTotalCreditsGreaterThanEqual(Integer minCredits);

    // Kayıt tarihine göre öğrenciler
    List<Student> findByEnrollmentDateBetween(LocalDate startDate, LocalDate endDate);

    // İsim ve soyisimde arama
    @Query("SELECT s FROM Student s WHERE LOWER(CONCAT(s.user.firstName, ' ', s.user.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Student> findByFullNameContainingIgnoreCase(@Param("name") String name);

    // Aktif öğrencileri ada göre sırala
    @Query("SELECT s FROM Student s WHERE s.user.isActive = true ORDER BY s.user.firstName, s.user.lastName")
    List<Student> findActiveStudentsOrderByName();

    // Program bazında öğrenci sayısı
    @Query("SELECT s.program.name, COUNT(s) FROM Student s WHERE s.user.isActive = true GROUP BY s.program.name")
    List<Object[]> getStudentCountByProgram();

    // Danışman bazında öğrenci sayısı
    @Query("SELECT CONCAT(s.advisor.user.firstName, ' ', s.advisor.user.lastName), COUNT(s) FROM Student s WHERE s.user.isActive = true AND s.advisor IS NOT NULL GROUP BY s.advisor.id")
    List<Object[]> getStudentCountByAdvisor();

    // GPA ortalaması program bazında
    @Query("SELECT s.program.name, AVG(s.gpa) FROM Student s WHERE s.user.isActive = true GROUP BY s.program.name")
    List<Object[]> getAverageGpaByProgram();

    // Mezuniyet şartlarını karşılayan öğrenciler
    @Query("SELECT s FROM Student s WHERE s.user.isActive = true AND s.completedCourses >= s.program.requiredCourses AND s.totalCredits >= s.program.requiredCredits AND s.gpa >= s.program.minimumGpa")
    List<Student> findStudentsEligibleForGraduation();

    // Danışmanı olmayan öğrenciler
    @Query("SELECT s FROM Student s WHERE s.advisor IS NULL AND s.user.isActive = true")
    List<Student> findStudentsWithoutAdvisor();
}
