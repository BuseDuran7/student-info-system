package com.ege.repository;

import com.ege.entities.Program;
import com.ege.entities.enums.ProgramType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    // Program adına göre arama
    Optional<Program> findByName(String name);

    // Program türüne göre arama
    List<Program> findByType(ProgramType type);

    // Aktif programları getir
    List<Program> findByIsActiveTrue();

    // Program türü ve aktiflik durumuna göre arama
    List<Program> findByTypeAndIsActiveTrue(ProgramType type);

    // Program adının var olup olmadığını kontrol et
    boolean existsByName(String name);

    // Minimum GPA'ya göre arama
    List<Program> findByMinimumGpaLessThanEqual(BigDecimal gpa);

    // Tez gerektiren programları getir
    List<Program> findByHasThesisTrue();

    // Tez gerektirmeyen programları getir
    List<Program> findByHasThesisFalse();

    // Program adına göre case insensitive arama
    @Query("SELECT p FROM Program p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Program> findByNameContainingIgnoreCase(@Param("name") String name);

    // Aktif programları ada göre sırala
    @Query("SELECT p FROM Program p WHERE p.isActive = true ORDER BY p.name")
    List<Program> findActiveProgramsOrderByName();

    // Program türüne göre istatistik
    @Query("SELECT p.type, COUNT(p) FROM Program p WHERE p.isActive = true GROUP BY p.type")
    List<Object[]> getProgramStatisticsByType();

    // Maksimum süreye göre arama
    List<Program> findByMaxDurationYearsLessThanEqual(Integer years);
}
