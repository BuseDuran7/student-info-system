package com.ege.repository;

import com.ege.entities.ResearchAssistant;
import com.ege.entities.User;
import com.ege.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResearchAssistantRepository extends JpaRepository<ResearchAssistant, Long> {

    // User ile araştırma görevlisi bul
    Optional<ResearchAssistant> findByUser(User user);

    // User ID ile araştırma görevlisi bul
    @Query("SELECT ra FROM ResearchAssistant ra WHERE ra.user.id = :userId")
    Optional<ResearchAssistant> findByUserId(@Param("userId") Long userId);

    // Username ile araştırma görevlisi bul
    @Query("SELECT ra FROM ResearchAssistant ra WHERE ra.user.username = :username")
    Optional<ResearchAssistant> findByUsername(@Param("username") String username);

    // Employee ID ile araştırma görevlisi bul
    @Query("SELECT ra FROM ResearchAssistant ra WHERE ra.user.employeeId = :employeeId")
    Optional<ResearchAssistant> findByEmployeeId(@Param("employeeId") String employeeId);

    // Student ile araştırma görevlisi bul (dual role)
    Optional<ResearchAssistant> findByStudent(Student student);

    // Student ID ile araştırma görevlisi bul
    @Query("SELECT ra FROM ResearchAssistant ra WHERE ra.student.id = :studentId")
    Optional<ResearchAssistant> findByStudentId(@Param("studentId") Long studentId);

    // Departmana göre araştırma görevlileri
    List<ResearchAssistant> findByDepartment(String department);

    // Aktif araştırma görevlileri (User aktif olan)
    @Query("SELECT ra FROM ResearchAssistant ra WHERE ra.user.isActive = true")
    List<ResearchAssistant> findActiveResearchAssistants();

    // Departmana göre aktif araştırma görevlileri
    @Query("SELECT ra FROM ResearchAssistant ra WHERE ra.department = :department AND ra.user.isActive = true")
    List<ResearchAssistant> findActiveByDepartment(@Param("department") String department);

    // Dual role araştırma görevlileri (hem öğrenci hem RA)
    @Query("SELECT ra FROM ResearchAssistant ra WHERE ra.student IS NOT NULL AND ra.user.isActive = true")
    List<ResearchAssistant> findStudentResearchAssistants();

    // Sadece çalışan araştırma görevlileri (öğrenci olmayanlar)
    @Query("SELECT ra FROM ResearchAssistant ra WHERE ra.student IS NULL AND ra.user.isActive = true")
    List<ResearchAssistant> findNonStudentResearchAssistants();

    // İşe alınma tarihine göre araştırma görevlileri
    List<ResearchAssistant> findByHireDateBetween(LocalDate startDate, LocalDate endDate);

    // Belirli bir tarihten sonra işe alınanlar
    List<ResearchAssistant> findByHireDateAfter(LocalDate date);

    // Maaş aralığında araştırma görevlileri
    List<ResearchAssistant> findBySalaryBetween(BigDecimal minSalary, BigDecimal maxSalary);

    // Minimum maaştan yüksek araştırma görevlileri
    List<ResearchAssistant> findBySalaryGreaterThanEqual(BigDecimal minSalary);

    // İsim ve soyisimde arama
    @Query("SELECT ra FROM ResearchAssistant ra WHERE LOWER(CONCAT(ra.user.firstName, ' ', ra.user.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ResearchAssistant> findByFullNameContainingIgnoreCase(@Param("name") String name);

    // Aktif araştırma görevlilerini ada göre sırala
    @Query("SELECT ra FROM ResearchAssistant ra WHERE ra.user.isActive = true ORDER BY ra.user.firstName, ra.user.lastName")
    List<ResearchAssistant> findActiveResearchAssistantsOrderByName();

    // Kıdem sırasına göre araştırma görevlileri
    @Query("SELECT ra FROM ResearchAssistant ra WHERE ra.user.isActive = true ORDER BY ra.hireDate ASC")
    List<ResearchAssistant> findActiveResearchAssistantsOrderBySeniority();

    // Maaş sırasına göre araştırma görevlileri
    @Query("SELECT ra FROM ResearchAssistant ra WHERE ra.user.isActive = true ORDER BY ra.salary DESC")
    List<ResearchAssistant> findActiveResearchAssistantsOrderBySalary();

    // Departman bazında araştırma görevlisi sayısı
    @Query("SELECT ra.department, COUNT(ra) FROM ResearchAssistant ra WHERE ra.user.isActive = true GROUP BY ra.department")
    List<Object[]> getResearchAssistantCountByDepartment();

    // Dual role analizi - departman bazında
    @Query("SELECT ra.department, " +
            "COUNT(ra) as total, " +
            "COUNT(CASE WHEN ra.student IS NOT NULL THEN 1 END) as withStudentRole, " +
            "COUNT(CASE WHEN ra.student IS NULL THEN 1 END) as onlyRA " +
            "FROM ResearchAssistant ra WHERE ra.user.isActive = true GROUP BY ra.department")
    List<Object[]> getDualRoleAnalysisByDepartment();

    // Maaş istatistikleri
    @Query("SELECT AVG(ra.salary), MIN(ra.salary), MAX(ra.salary) FROM ResearchAssistant ra WHERE ra.user.isActive = true")
    List<Object[]> getSalaryStatistics();

    // Departman bazında ortalama maaş
    @Query("SELECT ra.department, AVG(ra.salary) FROM ResearchAssistant ra WHERE ra.user.isActive = true GROUP BY ra.department")
    List<Object[]> getAverageSalaryByDepartment();

    // Yıl bazında işe alım sayısı
    @Query("SELECT YEAR(ra.hireDate), COUNT(ra) FROM ResearchAssistant ra WHERE ra.hireDate IS NOT NULL GROUP BY YEAR(ra.hireDate) ORDER BY YEAR(ra.hireDate)")
    List<Object[]> getHiringCountByYear();

    // Kıdem bazında dağılım (yıl)
    @Query("SELECT " +
            "CASE " +
            "WHEN YEAR(CURRENT_DATE) - YEAR(ra.hireDate) < 1 THEN '0-1 year' " +
            "WHEN YEAR(CURRENT_DATE) - YEAR(ra.hireDate) < 3 THEN '1-3 years' " +
            "WHEN YEAR(CURRENT_DATE) - YEAR(ra.hireDate) < 5 THEN '3-5 years' " +
            "ELSE '5+ years' " +
            "END as seniorityRange, COUNT(ra) " +
            "FROM ResearchAssistant ra WHERE ra.hireDate IS NOT NULL AND ra.user.isActive = true " +
            "GROUP BY " +
            "CASE " +
            "WHEN YEAR(CURRENT_DATE) - YEAR(ra.hireDate) < 1 THEN '0-1 year' " +
            "WHEN YEAR(CURRENT_DATE) - YEAR(ra.hireDate) < 3 THEN '1-3 years' " +
            "WHEN YEAR(CURRENT_DATE) - YEAR(ra.hireDate) < 5 THEN '3-5 years' " +
            "ELSE '5+ years' " +
            "END")
    List<Object[]> getSeniorityDistribution();

    // Student program türüne göre dual role RA'lar
    @Query("SELECT s.program.type, COUNT(ra) FROM ResearchAssistant ra JOIN ra.student s WHERE ra.user.isActive = true GROUP BY s.program.type")
    List<Object[]> getDualRoleCountByProgramType();
}

