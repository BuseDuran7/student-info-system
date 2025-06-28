package com.ege.repository;

import com.ege.entities.Grade;
import com.ege.entities.Student;
import com.ege.entities.Course;
import com.ege.entities.AcademicStaff;
import com.ege.entities.enums.LetterGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    // Student'a göre notlar
    List<Grade> findByStudent(Student student);

    // Student ID'ye göre notlar
    @Query("SELECT g FROM Grade g WHERE g.student.id = :studentId")
    List<Grade> findByStudentId(@Param("studentId") Long studentId);

    // Course'a göre notlar
    List<Grade> findByCourse(Course course);

    // Course ID'ye göre notlar
    @Query("SELECT g FROM Grade g WHERE g.course.id = :courseId")
    List<Grade> findByCourseId(@Param("courseId") Long courseId);

    // Student ve Course'a göre not bul
    Optional<Grade> findByStudentAndCourse(Student student, Course course);

    // Student ID ve Course ID'ye göre not bul
    @Query("SELECT g FROM Grade g WHERE g.student.id = :studentId AND g.course.id = :courseId")
    Optional<Grade> findByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    // Graded by (kim not verdi) göre notlar
    List<Grade> findByGradedBy(AcademicStaff gradedBy);

    // Graded by ID'ye göre notlar
    @Query("SELECT g FROM Grade g WHERE g.gradedBy.id = :gradedById")
    List<Grade> findByGradedById(@Param("gradedById") Long gradedById);

    // Letter grade'e göre notlar
    List<Grade> findByLetterGrade(LetterGrade letterGrade);

    // Geçen öğrenciler
    List<Grade> findByIsPassedTrue();

    // Kalan öğrenciler
    List<Grade> findByIsPassedFalse();

    // Student'ın geçtiği dersler
    @Query("SELECT g FROM Grade g WHERE g.student.id = :studentId AND g.isPassed = true")
    List<Grade> findPassedGradesByStudentId(@Param("studentId") Long studentId);

    // Student'ın kaldığı dersler
    @Query("SELECT g FROM Grade g WHERE g.student.id = :studentId AND g.isPassed = false")
    List<Grade> findFailedGradesByStudentId(@Param("studentId") Long studentId);

    // Course'un geçen öğrencileri
    @Query("SELECT g FROM Grade g WHERE g.course.id = :courseId AND g.isPassed = true")
    List<Grade> findPassedGradesByCourseId(@Param("courseId") Long courseId);

    // Course'un kalan öğrencileri
    @Query("SELECT g FROM Grade g WHERE g.course.id = :courseId AND g.isPassed = false")
    List<Grade> findFailedGradesByCourseId(@Param("courseId") Long courseId);

    // Numeric grade aralığında notlar
    List<Grade> findByNumericGradeBetween(BigDecimal minGrade, BigDecimal maxGrade);

    // Minimum numeric grade'den yüksek notlar
    List<Grade> findByNumericGradeGreaterThanEqual(BigDecimal minGrade);

    // Grade tarihine göre notlar
    List<Grade> findByGradeDateBetween(LocalDate startDate, LocalDate endDate);

    // Belirli bir tarihten sonraki notlar
    List<Grade> findByGradeDateAfter(LocalDate date);

    // Course semester ve yılına göre notlar
    @Query("SELECT g FROM Grade g WHERE g.course.semester = :semester AND g.course.year = :year")
    List<Grade> findByCourseSemedterAndYear(@Param("semester") String semester, @Param("year") Integer year);

    // Student'ın program'ına göre notlar
    @Query("SELECT g FROM Grade g WHERE g.student.program.id = :programId")
    List<Grade> findByStudentProgramId(@Param("programId") Long programId);

    // Öğretim üyesinin verdiği notlar (semester/yıl bazında)
    @Query("SELECT g FROM Grade g WHERE g.gradedBy.id = :instructorId AND g.course.semester = :semester AND g.course.year = :year")
    List<Grade> findByInstructorAndSemesterAndYear(@Param("instructorId") Long instructorId, @Param("semester") String semester, @Param("year") Integer year);

    // Student'ın GPA hesaplama için notları
    @Query("SELECT g FROM Grade g WHERE g.student.id = :studentId AND g.isPassed = true")
    List<Grade> findForGpaCalculation(@Param("studentId") Long studentId);

    // Course'un ortalama notu
    @Query("SELECT AVG(g.numericGrade) FROM Grade g WHERE g.course.id = :courseId")
    Double getAverageGradeByCourseId(@Param("courseId") Long courseId);

    // Student'ın ortalama notu
    @Query("SELECT AVG(g.numericGrade) FROM Grade g WHERE g.student.id = :studentId")
    Double getAverageGradeByStudentId(@Param("studentId") Long studentId);

    // Course'un geçme oranı
    @Query("SELECT " +
            "(COUNT(CASE WHEN g.isPassed = true THEN 1 END) * 100.0 / COUNT(g)) " +
            "FROM Grade g WHERE g.course.id = :courseId")
    Double getPassRateByCourseId(@Param("courseId") Long courseId);

    // Öğretim üyesinin genel geçme oranı
    @Query("SELECT " +
            "(COUNT(CASE WHEN g.isPassed = true THEN 1 END) * 100.0 / COUNT(g)) " +
            "FROM Grade g WHERE g.gradedBy.id = :instructorId")
    Double getPassRateByInstructorId(@Param("instructorId") Long instructorId);

    // Letter grade dağılımı - Course bazında
    @Query("SELECT g.letterGrade, COUNT(g) FROM Grade g WHERE g.course.id = :courseId GROUP BY g.letterGrade")
    List<Object[]> getLetterGradeDistributionByCourseId(@Param("courseId") Long courseId);

    // Letter grade dağılımı - Student bazında
    @Query("SELECT g.letterGrade, COUNT(g) FROM Grade g WHERE g.student.id = :studentId GROUP BY g.letterGrade")
    List<Object[]> getLetterGradeDistributionByStudentId(@Param("studentId") Long studentId);

    // İstatistikler - Course bazında not sayıları
    @Query("SELECT c.code, c.name, COUNT(g), AVG(g.numericGrade), " +
            "(COUNT(CASE WHEN g.isPassed = true THEN 1 END) * 100.0 / COUNT(g)) AS passRate " +
            "FROM Grade g JOIN g.course c GROUP BY c.id ORDER BY AVG(g.numericGrade) DESC")
    List<Object[]> getCourseGradeStatistics();

    // İstatistikler - Student bazında performans
    @Query("SELECT s.user.studentId, CONCAT(s.user.firstName, ' ', s.user.lastName), " +
            "COUNT(g), AVG(g.numericGrade), " +
            "(COUNT(CASE WHEN g.isPassed = true THEN 1 END) * 100.0 / COUNT(g)) AS passRate " +
            "FROM Grade g JOIN g.student s GROUP BY s.id ORDER BY AVG(g.numericGrade) DESC")
    List<Object[]> getStudentGradeStatistics();

    // İstatistikler - Öğretim üyesi bazında verdiği notlar
    @Query("SELECT CONCAT(a.user.firstName, ' ', a.user.lastName), " +
            "COUNT(g), AVG(g.numericGrade), " +
            "(COUNT(CASE WHEN g.isPassed = true THEN 1 END) * 100.0 / COUNT(g)) AS passRate " +
            "FROM Grade g JOIN g.gradedBy a GROUP BY a.id ORDER BY COUNT(g) DESC")
    List<Object[]> getInstructorGradeStatistics();

    // İstatistikler - Program bazında performans
    @Query("SELECT p.name, COUNT(g), AVG(g.numericGrade), " +
            "(COUNT(CASE WHEN g.isPassed = true THEN 1 END) * 100.0 / COUNT(g)) AS passRate " +
            "FROM Grade g JOIN g.student s JOIN s.program p GROUP BY p.id")
    List<Object[]> getProgramGradeStatistics();

    // İstatistikler - Semester bazında performans
    @Query("SELECT c.semester, c.year, COUNT(g), AVG(g.numericGrade), " +
            "(COUNT(CASE WHEN g.isPassed = true THEN 1 END) * 100.0 / COUNT(g)) AS passRate " +
            "FROM Grade g JOIN g.course c WHERE c.semester IS NOT NULL GROUP BY c.semester, c.year ORDER BY c.year DESC, c.semester")
    List<Object[]> getSemesterGradeStatistics();

    // En yüksek notlar (Top N)
    @Query("SELECT g FROM Grade g ORDER BY g.numericGrade DESC")
    List<Grade> findTopGrades();

    // En düşük notlar (Bottom N)
    @Query("SELECT g FROM Grade g ORDER BY g.numericGrade ASC")
    List<Grade> findBottomGrades();

    // Course'da en yüksek notu alan öğrenciler
    @Query("SELECT g FROM Grade g WHERE g.course.id = :courseId ORDER BY g.numericGrade DESC")
    List<Grade> findTopGradesByCourseId(@Param("courseId") Long courseId);

    // Grade count by range
    @Query("SELECT " +
            "CASE " +
            "WHEN g.numericGrade >= 90 THEN '90-100' " +
            "WHEN g.numericGrade >= 80 THEN '80-89' " +
            "WHEN g.numericGrade >= 70 THEN '70-79' " +
            "WHEN g.numericGrade >= 60 THEN '60-69' " +
            "ELSE '0-59' " +
            "END as gradeRange, COUNT(g) " +
            "FROM Grade g GROUP BY " +
            "CASE " +
            "WHEN g.numericGrade >= 90 THEN '90-100' " +
            "WHEN g.numericGrade >= 80 THEN '80-89' " +
            "WHEN g.numericGrade >= 70 THEN '70-79' " +
            "WHEN g.numericGrade >= 60 THEN '60-69' " +
            "ELSE '0-59' " +
            "END")
    List<Object[]> getGradeRangeDistribution();
}