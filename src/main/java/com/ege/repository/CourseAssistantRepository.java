package com.ege.repository;

import com.ege.entities.CourseAssistant;
import com.ege.entities.CourseAssistantId;
import com.ege.entities.Course;
import com.ege.entities.ResearchAssistant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseAssistantRepository extends JpaRepository<CourseAssistant, CourseAssistantId> {

    // Course'a göre tüm assistant'ları getir
    @Query("SELECT ca FROM CourseAssistant ca " +
            "JOIN FETCH ca.assistant a " +
            "JOIN FETCH a.user u " +
            "WHERE ca.course.id = :courseId " +
            "ORDER BY u.firstName, u.lastName")
    List<CourseAssistant> findByCourseId(@Param("courseId") Long courseId);

    // Assistant'a göre tüm course'ları getir
    @Query("SELECT ca FROM CourseAssistant ca " +
            "JOIN FETCH ca.course c " +
            "WHERE ca.assistant.id = :assistantId " +
            "ORDER BY c.code")
    List<CourseAssistant> findByAssistantId(@Param("assistantId") Long assistantId);

    // Belirli bir course ve assistant kombinasyonu var mı kontrol et
    boolean existsByIdCourseIdAndIdAssistantId(Long courseId, Long assistantId);

    // Course ve Assistant entity'leri ile bul
    Optional<CourseAssistant> findByCourseAndAssistant(Course course, ResearchAssistant assistant);

    // Departmana göre course assistant'ları getir
    @Query("SELECT ca FROM CourseAssistant ca " +
            "JOIN FETCH ca.course c " +
            "JOIN FETCH c.instructor i " +
            "JOIN FETCH ca.assistant a " +
            "JOIN FETCH a.user u " +
            "WHERE i.department = :department " +
            "ORDER BY c.code, u.firstName")
    List<CourseAssistant> findByCourseDepartment(@Param("department") String department);

    // Aktif assistant'ların course'larını getir
    @Query("SELECT ca FROM CourseAssistant ca " +
            "JOIN FETCH ca.course c " +
            "JOIN FETCH ca.assistant a " +
            "JOIN FETCH a.user u " +
            "WHERE u.isActive = true " +
            "ORDER BY c.code")
    List<CourseAssistant> findByActiveAssistants();

    // Semester'a göre course assistant'ları getir
    @Query("SELECT ca FROM CourseAssistant ca " +
            "JOIN FETCH ca.course c " +
            "JOIN FETCH ca.assistant a " +
            "JOIN FETCH a.user u " +
            "WHERE c.semester = :semester " +
            "ORDER BY c.code, u.firstName")
    List<CourseAssistant> findByCourseSemester(@Param("semester") String semester);

    // Course code'a göre assistant'ları getir
    @Query("SELECT ca FROM CourseAssistant ca " +
            "JOIN FETCH ca.assistant a " +
            "JOIN FETCH a.user u " +
            "WHERE ca.course.code = :courseCode " +
            "ORDER BY u.firstName, u.lastName")
    List<CourseAssistant> findByCourseCode(@Param("courseCode") String courseCode);

    // Assistant'ın employee ID'sine göre course'ları getir
    @Query("SELECT ca FROM CourseAssistant ca " +
            "JOIN FETCH ca.course c " +
            "WHERE ca.assistant.user.employeeId = :employeeId " +
            "ORDER BY c.code")
    List<CourseAssistant> findByAssistantEmployeeId(@Param("employeeId") String employeeId);

    // Dual role (öğrenci olan) assistant'ların course'larını getir
    @Query("SELECT ca FROM CourseAssistant ca " +
            "JOIN FETCH ca.course c " +
            "JOIN FETCH ca.assistant a " +
            "JOIN FETCH a.user u " +
            "WHERE a.student IS NOT NULL " +
            "ORDER BY c.code, u.firstName")
    List<CourseAssistant> findByStudentAssistants();

    // Sadece çalışan (öğrenci olmayan) assistant'ların course'larını getir
    @Query("SELECT ca FROM CourseAssistant ca " +
            "JOIN FETCH ca.course c " +
            "JOIN FETCH ca.assistant a " +
            "JOIN FETCH a.user u " +
            "WHERE a.student IS NULL " +
            "ORDER BY c.code, u.firstName")
    List<CourseAssistant> findByNonStudentAssistants();

    // İstatistikler ve analizler

    // Departmana göre assistant sayısı
    @Query("SELECT i.department, COUNT(DISTINCT ca.assistant.id) " +
            "FROM CourseAssistant ca " +
            "JOIN ca.course c " +
            "JOIN c.instructor i " +
            "GROUP BY i.department " +
            "ORDER BY i.department")
    List<Object[]> getAssistantCountByDepartment();

    // Course'a göre assistant sayısı
    @Query("SELECT c.code, c.name, COUNT(ca.assistant.id) " +
            "FROM CourseAssistant ca " +
            "JOIN ca.course c " +
            "GROUP BY c.id, c.code, c.name " +
            "ORDER BY COUNT(ca.assistant.id) DESC")
    List<Object[]> getAssistantCountByCourse();

    // Assistant'a göre course sayısı
    @Query("SELECT u.firstName, u.lastName, u.employeeId, COUNT(ca.course.id) " +
            "FROM CourseAssistant ca " +
            "JOIN ca.assistant a " +
            "JOIN a.user u " +
            "GROUP BY a.id, u.firstName, u.lastName, u.employeeId " +
            "ORDER BY COUNT(ca.course.id) DESC")
    List<Object[]> getCourseCountByAssistant();

    // Semester'a göre course assistant istatistikleri
    @Query("SELECT c.semester, COUNT(ca) " +
            "FROM CourseAssistant ca " +
            "JOIN ca.course c " +
            "GROUP BY c.semester " +
            "ORDER BY c.semester")
    List<Object[]> getCourseAssistantCountBySemester();

    // Dual role vs non-dual role analizi
    @Query("SELECT " +
            "CASE WHEN a.student IS NOT NULL THEN 'Dual Role' ELSE 'Staff Only' END, " +
            "COUNT(ca) " +
            "FROM CourseAssistant ca " +
            "JOIN ca.assistant a " +
            "GROUP BY CASE WHEN a.student IS NOT NULL THEN 'Dual Role' ELSE 'Staff Only' END")
    List<Object[]> getDualRoleAnalysis();

    // En fazla assistant'a sahip course'lar
    @Query("SELECT c.code, c.name, i.department, COUNT(ca.assistant.id) " +
            "FROM CourseAssistant ca " +
            "JOIN ca.course c " +
            "JOIN c.instructor i " +
            "GROUP BY c.id, c.code, c.name, i.department " +
            "HAVING COUNT(ca.assistant.id) >= :minAssistants " +
            "ORDER BY COUNT(ca.assistant.id) DESC")
    List<Object[]> getCoursesWithMostAssistants(@Param("minAssistants") int minAssistants);

    // En fazla course'a yardım eden assistant'lar
    @Query("SELECT u.firstName, u.lastName, u.employeeId, a.department, COUNT(ca.course.id) " +
            "FROM CourseAssistant ca " +
            "JOIN ca.assistant a " +
            "JOIN a.user u " +
            "GROUP BY a.id, u.firstName, u.lastName, u.employeeId, a.department " +
            "HAVING COUNT(ca.course.id) >= :minCourses " +
            "ORDER BY COUNT(ca.course.id) DESC")
    List<Object[]> getAssistantsWithMostCourses(@Param("minCourses") int minCourses);
}