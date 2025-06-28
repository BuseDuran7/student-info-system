package com.ege.repository;

import com.ege.entities.AcademicStaff;
import com.ege.entities.User;
import com.ege.entities.enums.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AcademicStaffRepository extends JpaRepository<AcademicStaff, Long> {

    // User ile akademik personel bul
    Optional<AcademicStaff> findByUser(User user);

    // User ID ile akademik personel bul
    @Query("SELECT a FROM AcademicStaff a WHERE a.user.id = :userId")
    Optional<AcademicStaff> findByUserId(@Param("userId") Long userId);

    // Username ile akademik personel bul
    @Query("SELECT a FROM AcademicStaff a WHERE a.user.username = :username")
    Optional<AcademicStaff> findByUsername(@Param("username") String username);

    // Ünvana göre akademik personel listesi
    List<AcademicStaff> findByTitle(Title title);

    // Bölüme göre akademik personel listesi
    List<AcademicStaff> findByDepartment(String department);

    // Ünvan ve bölüme göre akademik personel listesi
    List<AcademicStaff> findByTitleAndDepartment(Title title, String department);

    // İsim ve soyisimde arama
    @Query("SELECT a FROM AcademicStaff a WHERE LOWER(CONCAT(a.user.firstName, ' ', a.user.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<AcademicStaff> findByFullNameContainingIgnoreCase(@Param("name") String name);

    // Kıdeme göre akademik personel listesi
    List<AcademicStaff> findBySeniorityYearsGreaterThanEqual(Integer years);

    // Maaş aralığında arama
    List<AcademicStaff> findBySalaryBetween(BigDecimal minSalary, BigDecimal maxSalary);

    // İşe alınma tarihine göre arama
    List<AcademicStaff> findByHireDateBetween(LocalDate startDate, LocalDate endDate);

    // Aktif akademik personel (User aktif olan)
    @Query("SELECT a FROM AcademicStaff a WHERE a.user.isActive = true")
    List<AcademicStaff> findActiveAcademicStaff();

    // Bölüme göre aktif akademik personel
    @Query("SELECT a FROM AcademicStaff a WHERE a.department = :department AND a.user.isActive = true")
    List<AcademicStaff> findActiveByDepartment(@Param("department") String department);

    // Ünvana göre aktif akademik personel
    @Query("SELECT a FROM AcademicStaff a WHERE a.title = :title AND a.user.isActive = true")
    List<AcademicStaff> findActiveByTitle(@Param("title") Title title);

    // Akademik personeli ada göre sırala
    @Query("SELECT a FROM AcademicStaff a WHERE a.user.isActive = true ORDER BY a.user.firstName, a.user.lastName")
    List<AcademicStaff> findActiveOrderByName();

    // Bölüm bazında istatistik
    @Query("SELECT a.department, COUNT(a) FROM AcademicStaff a WHERE a.user.isActive = true GROUP BY a.department")
    List<Object[]> getAcademicStaffCountByDepartment();

    // Ünvan bazında istatistik
    @Query("SELECT a.title, COUNT(a) FROM AcademicStaff a WHERE a.user.isActive = true GROUP BY a.title")
    List<Object[]> getAcademicStaffCountByTitle();

    // Belirli ünvandan yukarı olanları getir
    @Query("SELECT a FROM AcademicStaff a WHERE a.title IN :titles AND a.user.isActive = true")
    List<AcademicStaff> findByTitleIn(@Param("titles") List<Title> titles);
}
