package com.ege.repository;

import com.ege.entities.Thesis;
import com.ege.entities.Student;
import com.ege.entities.AcademicStaff;
import com.ege.entities.enums.ThesisType;
import com.ege.entities.enums.ThesisStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ThesisRepository extends JpaRepository<Thesis, Long> {

    // Student'a göre tez getir (OneToOne ilişki)
    Optional<Thesis> findByStudent(Student student);

    Optional<Thesis> findByStudentId(Long studentId);

    // Student'ın user ID'sine göre tez getir
    @Query("SELECT t FROM Thesis t " +
            "JOIN FETCH t.student s " +
            "JOIN FETCH s.user u " +
            "JOIN FETCH t.supervisor sup " +
            "WHERE u.id = :userId")
    Optional<Thesis> findByStudentUserId(@Param("userId") Long userId);

    // Student ID (studentId string) ile tez getir
    @Query("SELECT t FROM Thesis t " +
            "JOIN FETCH t.student s " +
            "JOIN FETCH s.user u " +
            "JOIN FETCH t.supervisor sup " +
            "WHERE u.studentId = :studentId")
    Optional<Thesis> findByStudentNumber(@Param("studentId") String studentId);

    // Supervisor'a göre tezleri getir
    @Query("SELECT t FROM Thesis t " +
            "JOIN FETCH t.student s " +
            "JOIN FETCH s.user su " +
            "JOIN FETCH t.supervisor sup " +
            "WHERE t.supervisor.id = :supervisorId " +
            "ORDER BY t.startDate DESC")
    List<Thesis> findBySupervisorId(@Param("supervisorId") Long supervisorId);

    // Supervisor entity'si ile tezleri getir
    List<Thesis> findBySupervisor(AcademicStaff supervisor);

    // Supervisor'ın employee ID'sine göre tezleri getir
    @Query("SELECT t FROM Thesis t " +
            "JOIN FETCH t.student s " +
            "JOIN FETCH s.user su " +
            "JOIN FETCH t.supervisor sup " +
            "JOIN FETCH sup.user supu " +
            "WHERE supu.employeeId = :employeeId " +
            "ORDER BY t.startDate DESC")
    List<Thesis> findBySupervisorEmployeeId(@Param("employeeId") String employeeId);

    // Tez tipine göre tezleri getir
    @Query("SELECT t FROM Thesis t " +
            "JOIN FETCH t.student s " +
            "JOIN FETCH s.user su " +
            "JOIN FETCH t.supervisor sup " +
            "WHERE t.type = :type " +
            "ORDER BY t.startDate DESC")
    List<Thesis> findByType(@Param("type") ThesisType type);

    // Tez durumuna göre tezleri getir
    @Query("SELECT t FROM Thesis t " +
            "JOIN FETCH t.student s " +
            "JOIN FETCH s.user su " +
            "JOIN FETCH t.supervisor sup " +
            "WHERE t.status = :status " +
            "ORDER BY t.startDate DESC")
    List<Thesis> findByStatus(@Param("status") ThesisStatus status);

    // Aktif (devam eden) tezleri getir
    @Query("SELECT t FROM Thesis t " +
            "JOIN FETCH t.student s " +
            "JOIN FETCH s.user su " +
            "JOIN FETCH t.supervisor sup " +
            "WHERE t.status = 'DEVAM_EDIYOR' " +
            "ORDER BY t.startDate DESC")
    List<Thesis> findActiveTheses();

    // Onaylanan (tamamlanan) tezleri getir
    @Query("SELECT t FROM Thesis t " +
            "JOIN FETCH t.student s " +
            "JOIN FETCH s.user su " +
            "JOIN FETCH t.supervisor sup " +
            "WHERE t.status = 'ONAYLANDI' " +
            "ORDER BY t.defenseDate DESC")
    List<Thesis> findCompletedTheses();

    // Departmana göre tezleri getir (supervisor'ın department'ı)
    @Query("SELECT t FROM Thesis t " +
            "JOIN FETCH t.student s " +
            "JOIN FETCH s.user su " +
            "JOIN FETCH t.supervisor sup " +
            "WHERE sup.department = :department " +
            "ORDER BY t.startDate DESC")
    List<Thesis> findByDepartment(@Param("department") String department);

    // Başlangıç tarihi aralığına göre tezleri getir
    @Query("SELECT t FROM Thesis t " +
            "JOIN FETCH t.student s " +
            "JOIN FETCH s.user su " +
            "JOIN FETCH t.supervisor sup " +
            "WHERE t.startDate BETWEEN :startDate AND :endDate " +
            "ORDER BY t.startDate DESC")
    List<Thesis> findByStartDateBetween(@Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);

    // Savunma tarihi aralığına göre tezleri getir
    @Query("SELECT t FROM Thesis t " +
            "JOIN FETCH t.student s " +
            "JOIN FETCH s.user su " +
            "JOIN FETCH t.supervisor sup " +
            "WHERE t.defenseDate BETWEEN :startDate AND :endDate " +
            "ORDER BY t.defenseDate DESC")
    List<Thesis> findByDefenseDateBetween(@Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);

    // Başlık ile arama
    @Query("SELECT t FROM Thesis t " +
            "JOIN FETCH t.student s " +
            "JOIN FETCH s.user su " +
            "JOIN FETCH t.supervisor sup " +
            "WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%')) " +
            "ORDER BY t.startDate DESC")
    List<Thesis> findByTitleContainingIgnoreCase(@Param("title") String title);

    // Öğrenci adıyla arama
    @Query("SELECT t FROM Thesis t " +
            "JOIN FETCH t.student s " +
            "JOIN FETCH s.user su " +
            "JOIN FETCH t.supervisor sup " +
            "WHERE LOWER(CONCAT(su.firstName, ' ', su.lastName)) LIKE LOWER(CONCAT('%', :studentName, '%')) " +
            "ORDER BY t.startDate DESC")
    List<Thesis> findByStudentNameContaining(@Param("studentName") String studentName);

    // Supervisor adıyla arama
    @Query("SELECT t FROM Thesis t " +
            "JOIN FETCH t.student s " +
            "JOIN FETCH s.user su " +
            "JOIN FETCH t.supervisor sup " +
            "JOIN FETCH sup.user supu " +
            "WHERE LOWER(CONCAT(supu.firstName, ' ', supu.lastName)) LIKE LOWER(CONCAT('%', :supervisorName, '%')) " +
            "ORDER BY t.startDate DESC")
    List<Thesis> findBySupervisorNameContaining(@Param("supervisorName") String supervisorName);

    // Uzun süredir devam eden tezleri getir
    @Query("SELECT t FROM Thesis t " +
            "JOIN FETCH t.student s " +
            "JOIN FETCH s.user su " +
            "JOIN FETCH t.supervisor sup " +
            "WHERE t.status = 'DEVAM_EDIYOR' " +
            "AND t.startDate <= :thresholdDate " +
            "ORDER BY t.startDate ASC")
    List<Thesis> findLongRunningTheses(@Param("thresholdDate") LocalDate thresholdDate);

    // Yakında savunması olan tezleri getir
    @Query("SELECT t FROM Thesis t " +
            "JOIN FETCH t.student s " +
            "JOIN FETCH s.user su " +
            "JOIN FETCH t.supervisor sup " +
            "WHERE t.defenseDate BETWEEN :startDate AND :endDate " +
            "AND t.status IN ('TESLIM_EDILDI', 'SAVUNULDU') " +
            "ORDER BY t.defenseDate ASC")
    List<Thesis> findUpcomingDefenses(@Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);

    // İstatistikler ve analizler

    // Tez tipine göre sayı
    @Query("SELECT t.type, COUNT(t) " +
            "FROM Thesis t " +
            "GROUP BY t.type " +
            "ORDER BY t.type")
    List<Object[]> getThesisCountByType();

    // Tez durumuna göre sayı
    @Query("SELECT t.status, COUNT(t) " +
            "FROM Thesis t " +
            "GROUP BY t.status " +
            "ORDER BY t.status")
    List<Object[]> getThesisCountByStatus();

    // Departmana göre tez sayısı
    @Query("SELECT sup.department, COUNT(t) " +
            "FROM Thesis t " +
            "JOIN t.supervisor sup " +
            "GROUP BY sup.department " +
            "ORDER BY sup.department")
    List<Object[]> getThesisCountByDepartment();

    // Supervisor'a göre tez sayısı
    @Query("SELECT CONCAT(supu.firstName, ' ', supu.lastName), supu.employeeId, COUNT(t) " +
            "FROM Thesis t " +
            "JOIN t.supervisor sup " +
            "JOIN sup.user supu " +
            "GROUP BY sup.id, supu.firstName, supu.lastName, supu.employeeId " +
            "ORDER BY COUNT(t) DESC")
    List<Object[]> getThesisCountBySupervisor();

    // Yıllara göre tez sayısı (başlangıç yılı)
    @Query("SELECT YEAR(t.startDate), COUNT(t) " +
            "FROM Thesis t " +
            "WHERE t.startDate IS NOT NULL " +
            "GROUP BY YEAR(t.startDate) " +
            "ORDER BY YEAR(t.startDate) DESC")
    List<Object[]> getThesisCountByStartYear();

    // Yıllara göre savunma sayısı
    @Query("SELECT YEAR(t.defenseDate), COUNT(t) " +
            "FROM Thesis t " +
            "WHERE t.defenseDate IS NOT NULL " +
            "GROUP BY YEAR(t.defenseDate) " +
            "ORDER BY YEAR(t.defenseDate) DESC")
    List<Object[]> getThesisDefenseCountByYear();

    // Ortalama tez süresi (ay cinsinden) - Sadece tamamlanmış tezler için
    @Query("SELECT AVG(CAST((YEAR(t.defenseDate) - YEAR(t.startDate)) * 12 + " +
            "(MONTH(t.defenseDate) - MONTH(t.startDate)) AS double)) " +
            "FROM Thesis t " +
            "WHERE t.startDate IS NOT NULL AND t.defenseDate IS NOT NULL " +
            "AND t.status = 'ONAYLANDI'")
    Double getAverageThesisDurationInMonths();

    // Tip ve duruma göre tez sayısı
    @Query("SELECT t.type, t.status, COUNT(t) " +
            "FROM Thesis t " +
            "GROUP BY t.type, t.status " +
            "ORDER BY t.type, t.status")
    List<Object[]> getThesisCountByTypeAndStatus();

    // Departman ve tip bazında tez analizi
    @Query("SELECT sup.department, t.type, COUNT(t) " +
            "FROM Thesis t " +
            "JOIN t.supervisor sup " +
            "GROUP BY sup.department, t.type " +
            "ORDER BY sup.department, t.type")
    List<Object[]> getThesisAnalysisByDepartmentAndType();

    // En fazla tez danışmanlığı yapan supervisor'lar
    @Query("SELECT CONCAT(supu.firstName, ' ', supu.lastName), supu.employeeId, sup.department, COUNT(t) " +
            "FROM Thesis t " +
            "JOIN t.supervisor sup " +
            "JOIN sup.user supu " +
            "GROUP BY sup.id, supu.firstName, supu.lastName, supu.employeeId, sup.department " +
            "HAVING COUNT(t) >= :minTheses " +
            "ORDER BY COUNT(t) DESC")
    List<Object[]> getSupervisorsWithMostTheses(@Param("minTheses") int minTheses);

    // Belirli dönemde onaylanan tezler
    @Query("SELECT COUNT(t) " +
            "FROM Thesis t " +
            "WHERE t.status = 'ONAYLANDI' " +
            "AND t.defenseDate BETWEEN :startDate AND :endDate")
    Long getCompletedThesesCountInPeriod(@Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

    // Aktif tez sayısı
    @Query("SELECT COUNT(t) FROM Thesis t WHERE t.status = 'DEVAM_EDIYOR'")
    Long getActiveThesesCount();
}