package com.ege.repository;

import com.ege.entities.Course;
import com.ege.entities.AcademicStaff;
import com.ege.entities.enums.CourseLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Course code ile ders bul
    Optional<Course> findByCode(String code);

    // Course code'un var olup olmadığını kontrol et
    boolean existsByCode(String code);

    // Course name ile ders bul
    List<Course> findByNameContainingIgnoreCase(String name);

    // Öğretim üyesine göre dersler
    List<Course> findByInstructor(AcademicStaff instructor);

    // Öğretim üyesi ID'ye göre dersler
    @Query("SELECT c FROM Course c WHERE c.instructor.id = :instructorId")
    List<Course> findByInstructorId(@Param("instructorId") Long instructorId);

    // Course level'a göre dersler
    List<Course> findByLevel(CourseLevel level);

    // Aktif dersler
    List<Course> findByIsActiveTrue();

    // Aktif dersleri code'a göre sırala
    @Query("SELECT c FROM Course c WHERE c.isActive = true ORDER BY c.code")
    List<Course> findActiveCoursesOrderByCode();

    // Course level ve aktiflik durumuna göre dersler
    List<Course> findByLevelAndIsActiveTrue(CourseLevel level);

    // Öğretim üyesi ve aktiflik durumuna göre dersler
    @Query("SELECT c FROM Course c WHERE c.instructor.id = :instructorId AND c.isActive = true")
    List<Course> findActiveByInstructorId(@Param("instructorId") Long instructorId);

    // Kredi sayısına göre dersler
    List<Course> findByCredits(Integer credits);

    // Minimum kredi sayısından fazla dersler
    List<Course> findByCreditsGreaterThanEqual(Integer minCredits);

    // Semester ve yıla göre dersler
    List<Course> findBySemesterAndYear(String semester, Integer year);

    // Sadece belirli bir yıldaki dersler
    List<Course> findByYear(Integer year);

    // Semester'e göre dersler
    List<Course> findBySemester(String semester);

    // Kapasitesi dolu olmayan dersler
    @Query("SELECT c FROM Course c WHERE c.currentEnrollment < c.maxCapacity AND c.isActive = true")
    List<Course> findCoursesWithAvailableCapacity();

    // Kapasitesi dolu olan dersler
    @Query("SELECT c FROM Course c WHERE c.currentEnrollment >= c.maxCapacity AND c.isActive = true")
    List<Course> findFullCourses();

    // Minimum kayıt sayısına ulaşmamış dersler
    @Query("SELECT c FROM Course c WHERE c.currentEnrollment < :minEnrollment AND c.isActive = true")
    List<Course> findCoursesWithLowEnrollment(@Param("minEnrollment") Integer minEnrollment);

    // Öğretim üyesi departmanına göre dersler
    @Query("SELECT c FROM Course c WHERE c.instructor.department = :department AND c.isActive = true")
    List<Course> findByInstructorDepartment(@Param("department") String department);

    // Course level bazında istatistik
    @Query("SELECT c.level, COUNT(c) FROM Course c WHERE c.isActive = true GROUP BY c.level")
    List<Object[]> getCourseCountByLevel();

    // Öğretim üyesi bazında ders sayısı
    @Query("SELECT CONCAT(c.instructor.user.firstName, ' ', c.instructor.user.lastName), COUNT(c) FROM Course c WHERE c.isActive = true GROUP BY c.instructor.id")
    List<Object[]> getCourseCountByInstructor();

    // Semester bazında ders sayısı
    @Query("SELECT c.semester, COUNT(c) FROM Course c WHERE c.isActive = true GROUP BY c.semester")
    List<Object[]> getCourseCountBySemester();

    // Departman bazında ders sayısı
    @Query("SELECT c.instructor.department, COUNT(c) FROM Course c WHERE c.isActive = true GROUP BY c.instructor.department")
    List<Object[]> getCourseCountByDepartment();

    // Ortalama ders kapasitesi
    @Query("SELECT AVG(c.maxCapacity) FROM Course c WHERE c.isActive = true")
    Double getAverageMaxCapacity();

    // Ortalama kayıt oranı
    @Query("SELECT AVG(CAST(c.currentEnrollment AS DOUBLE) / c.maxCapacity) FROM Course c WHERE c.isActive = true AND c.maxCapacity > 0")
    Double getAverageEnrollmentRate();

    // En çok kayıtlı dersler (top N)
    @Query("SELECT c FROM Course c WHERE c.isActive = true ORDER BY c.currentEnrollment DESC")
    List<Course> findMostEnrolledCourses();

    // En az kayıtlı dersler (top N)
    @Query("SELECT c FROM Course c WHERE c.isActive = true ORDER BY c.currentEnrollment ASC")
    List<Course> findLeastEnrolledCourses();
}