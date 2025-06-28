package com.ege.repository;

import com.ege.entities.Enrollment;
import com.ege.entities.Student;
import com.ege.entities.Course;
import com.ege.entities.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // Student'a göre enrollments
    List<Enrollment> findByStudent(Student student);

    // Student ID'ye göre enrollments
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId")
    List<Enrollment> findByStudentId(@Param("studentId") Long studentId);

    // Course'a göre enrollments
    List<Enrollment> findByCourse(Course course);

    // Course ID'ye göre enrollments
    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId")
    List<Enrollment> findByCourseId(@Param("courseId") Long courseId);

    // Student ve Course'a göre enrollment bul
    Optional<Enrollment> findByStudentAndCourse(Student student, Course course);

    // Student ID ve Course ID'ye göre enrollment bul
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.course.id = :courseId")
    Optional<Enrollment> findByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    // Status'a göre enrollments
    List<Enrollment> findByStatus(EnrollmentStatus status);

    // Student ve status'a göre enrollments
    List<Enrollment> findByStudentAndStatus(Student student, EnrollmentStatus status);

    // Student ID ve status'a göre enrollments
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.status = :status")
    List<Enrollment> findByStudentIdAndStatus(@Param("studentId") Long studentId, @Param("status") EnrollmentStatus status);

    // Course ve status'a göre enrollments
    List<Enrollment> findByCourseAndStatus(Course course, EnrollmentStatus status);

    // Course ID ve status'a göre enrollments
    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId AND e.status = :status")
    List<Enrollment> findByCourseIdAndStatus(@Param("courseId") Long courseId, @Param("status") EnrollmentStatus status);

    // Aktif enrollments (KAYITLI status)
    @Query("SELECT e FROM Enrollment e WHERE e.status = 'KAYITLI'")
    List<Enrollment> findActiveEnrollments();

    // Student'ın aktif enrollments
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.status = 'KAYITLI'")
    List<Enrollment> findActiveEnrollmentsByStudentId(@Param("studentId") Long studentId);

    // Course'un aktif enrollments
    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId AND e.status = 'KAYITLI'")
    List<Enrollment> findActiveEnrollmentsByCourseId(@Param("courseId") Long courseId);

    // Enrollment tarihine göre enrollments
    List<Enrollment> findByEnrollmentDateBetween(LocalDate startDate, LocalDate endDate);

    // Belirli bir tarihten sonraki enrollments
    List<Enrollment> findByEnrollmentDateAfter(LocalDate date);

    // Program'a göre enrollments
    @Query("SELECT e FROM Enrollment e WHERE e.student.program.id = :programId")
    List<Enrollment> findByStudentProgramId(@Param("programId") Long programId);

    // Öğretim üyesine göre enrollments (course instructor)
    @Query("SELECT e FROM Enrollment e WHERE e.course.instructor.id = :instructorId")
    List<Enrollment> findByInstructorId(@Param("instructorId") Long instructorId);

    // Course level'a göre enrollments
    @Query("SELECT e FROM Enrollment e WHERE e.course.level = :courseLevel")
    List<Enrollment> findByCourseLevel(@Param("courseLevel") String courseLevel);

    // Student'ın semester ve yıla göre enrollments
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.course.semester = :semester AND e.course.year = :year")
    List<Enrollment> findByStudentIdAndSemesterAndYear(@Param("studentId") Long studentId, @Param("semester") String semester, @Param("year") Integer year);

    // Semester ve yıla göre tüm enrollments
    @Query("SELECT e FROM Enrollment e WHERE e.course.semester = :semester AND e.course.year = :year")
    List<Enrollment> findBySemesterAndYear(@Param("semester") String semester, @Param("year") Integer year);

    // Student'ın toplam enrollment sayısı
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.student.id = :studentId")
    Long countByStudentId(@Param("studentId") Long studentId);

    // Student'ın aktif enrollment sayısı
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.student.id = :studentId AND e.status = 'KAYITLI'")
    Long countActiveEnrollmentsByStudentId(@Param("studentId") Long studentId);

    // Course'un toplam enrollment sayısı
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId")
    Long countByCourseId(@Param("courseId") Long courseId);

    // Course'un aktif enrollment sayısı
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId AND e.status = 'KAYITLI'")
    Long countActiveEnrollmentsByCourseId(@Param("courseId") Long courseId);

    // Student'ın belirli bir durumda kaç dersi var
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.student.id = :studentId AND e.status = :status")
    Long countByStudentIdAndStatus(@Param("studentId") Long studentId, @Param("status") EnrollmentStatus status);

    // İstatistikler - Status bazında enrollment sayısı
    @Query("SELECT e.status, COUNT(e) FROM Enrollment e GROUP BY e.status")
    List<Object[]> getEnrollmentCountByStatus();

    // İstatistikler - Course bazında enrollment sayısı
    @Query("SELECT c.code, c.name, COUNT(e) FROM Enrollment e JOIN e.course c GROUP BY c.id ORDER BY COUNT(e) DESC")
    List<Object[]> getEnrollmentCountByCourse();

    // İstatistikler - Program bazında enrollment sayısı
    @Query("SELECT p.name, COUNT(e) FROM Enrollment e JOIN e.student s JOIN s.program p GROUP BY p.id")
    List<Object[]> getEnrollmentCountByProgram();

    // İstatistikler - Semester bazında enrollment sayısı
    @Query("SELECT c.semester, COUNT(e) FROM Enrollment e JOIN e.course c WHERE c.semester IS NOT NULL GROUP BY c.semester")
    List<Object[]> getEnrollmentCountBySemester();

    // İstatistikler - Öğretim üyesi bazında enrollment sayısı
    @Query("SELECT CONCAT(a.user.firstName, ' ', a.user.lastName), COUNT(e) FROM Enrollment e JOIN e.course c JOIN c.instructor a GROUP BY a.id")
    List<Object[]> getEnrollmentCountByInstructor();

    // Drop rate hesaplama - Course bazında
    @Query("SELECT c.code, c.name, " +
            "COUNT(CASE WHEN e.status = 'BIRAKTI' THEN 1 END) AS dropped, " +
            "COUNT(e) AS total, " +
            "(COUNT(CASE WHEN e.status = 'BIRAKTI' THEN 1 END) * 100.0 / COUNT(e)) AS dropRate " +
            "FROM Enrollment e JOIN e.course c GROUP BY c.id HAVING COUNT(e) > 0")
    List<Object[]> getDropRateByCourse();

    // Öğrenci bazında ders yükü analizi
    @Query("SELECT s.user.studentId, CONCAT(s.user.firstName, ' ', s.user.lastName), COUNT(e) " +
            "FROM Enrollment e JOIN e.student s WHERE e.status = 'KAYITLI' GROUP BY s.id ORDER BY COUNT(e) DESC")
    List<Object[]> getStudentCourseLoad();

    // Çakışma kontrolü - aynı student ve course için birden fazla enrollment
    @Query("SELECT e1 FROM Enrollment e1 WHERE EXISTS (SELECT e2 FROM Enrollment e2 WHERE e1.id != e2.id AND e1.student = e2.student AND e1.course = e2.course)")
    List<Enrollment> findDuplicateEnrollments();
}