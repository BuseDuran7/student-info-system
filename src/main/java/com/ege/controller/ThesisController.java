package com.ege.controller;

import com.ege.dto.ThesisDto;
import com.ege.entities.enums.ThesisType;
import com.ege.entities.enums.ThesisStatus;
import com.ege.service.ThesisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/theses")
@CrossOrigin(origins = "*")
public class ThesisController {

    @Autowired
    private ThesisService thesisService;

    // CRUD Operations

    // Tüm tezleri getir
    @GetMapping
    public ResponseEntity<List<ThesisDto>> getAllTheses() {
        try {
            List<ThesisDto> theses = thesisService.getAllTheses();
            return ResponseEntity.ok(theses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ID ile tez getir
    @GetMapping("/{id}")
    public ResponseEntity<ThesisDto> getThesisById(@PathVariable Long id) {
        try {
            Optional<ThesisDto> thesis = thesisService.getThesisById(id);
            return thesis.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Yeni tez oluştur
    @PostMapping
    public ResponseEntity<ThesisDto> createThesis(@RequestBody ThesisDto thesisDto) {
        try {
            ThesisDto createdThesis = thesisService.createThesis(thesisDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdThesis);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Tez güncelle
    @PutMapping("/{id}")
    public ResponseEntity<ThesisDto> updateThesis(@PathVariable Long id, @RequestBody ThesisDto thesisDto) {
        try {
            ThesisDto updatedThesis = thesisService.updateThesis(id, thesisDto);
            return ResponseEntity.ok(updatedThesis);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Tez sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThesis(@PathVariable Long id) {
        try {
            thesisService.deleteThesis(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Query Operations

    // Student ID ile tez getir
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ThesisDto> getThesisByStudentId(@PathVariable Long studentId) {
        try {
            Optional<ThesisDto> thesis = thesisService.getThesisByStudentId(studentId);
            return thesis.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Student User ID ile tez getir
    @GetMapping("/student-user/{userId}")
    public ResponseEntity<ThesisDto> getThesisByStudentUserId(@PathVariable Long userId) {
        try {
            Optional<ThesisDto> thesis = thesisService.getThesisByStudentUserId(userId);
            return thesis.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Student number ile tez getir
    @GetMapping("/student-number/{studentNumber}")
    public ResponseEntity<ThesisDto> getThesisByStudentNumber(@PathVariable String studentNumber) {
        try {
            Optional<ThesisDto> thesis = thesisService.getThesisByStudentNumber(studentNumber);
            return thesis.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Supervisor ID ile tezleri getir
    @GetMapping("/supervisor/{supervisorId}")
    public ResponseEntity<List<ThesisDto>> getThesesBySupervisorId(@PathVariable Long supervisorId) {
        try {
            List<ThesisDto> theses = thesisService.getThesesBySupervisorId(supervisorId);
            return ResponseEntity.ok(theses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Supervisor employee ID ile tezleri getir
    @GetMapping("/supervisor-employee/{employeeId}")
    public ResponseEntity<List<ThesisDto>> getThesesBySupervisorEmployeeId(@PathVariable String employeeId) {
        try {
            List<ThesisDto> theses = thesisService.getThesesBySupervisorEmployeeId(employeeId);
            return ResponseEntity.ok(theses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Tez tipine göre tezleri getir
    @GetMapping("/type/{type}")
    public ResponseEntity<List<ThesisDto>> getThesesByType(@PathVariable ThesisType type) {
        try {
            List<ThesisDto> theses = thesisService.getThesesByType(type);
            return ResponseEntity.ok(theses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Tez durumuna göre tezleri getir
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ThesisDto>> getThesesByStatus(@PathVariable ThesisStatus status) {
        try {
            List<ThesisDto> theses = thesisService.getThesesByStatus(status);
            return ResponseEntity.ok(theses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Departmana göre tezleri getir
    @GetMapping("/department/{department}")
    public ResponseEntity<List<ThesisDto>> getThesesByDepartment(@PathVariable String department) {
        try {
            List<ThesisDto> theses = thesisService.getThesesByDepartment(department);
            return ResponseEntity.ok(theses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Aktif tezleri getir
    @GetMapping("/active")
    public ResponseEntity<List<ThesisDto>> getActiveTheses() {
        try {
            List<ThesisDto> theses = thesisService.getActiveTheses();
            return ResponseEntity.ok(theses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Onaylanan (tamamlanan) tezleri getir
    @GetMapping("/completed")
    public ResponseEntity<List<ThesisDto>> getCompletedTheses() {
        try {
            List<ThesisDto> theses = thesisService.getCompletedTheses();
            return ResponseEntity.ok(theses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Teslim edilmiş tezleri getir
    @GetMapping("/submitted")
    public ResponseEntity<List<ThesisDto>> getSubmittedTheses() {
        try {
            List<ThesisDto> theses = thesisService.getSubmittedTheses();
            return ResponseEntity.ok(theses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Savunulmuş tezleri getir
    @GetMapping("/defended")
    public ResponseEntity<List<ThesisDto>> getDefendedTheses() {
        try {
            List<ThesisDto> theses = thesisService.getDefendedTheses();
            return ResponseEntity.ok(theses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Tarih aralığı sorgular

    // Başlangıç tarihi aralığına göre tezleri getir
    @GetMapping("/start-date-range")
    public ResponseEntity<List<ThesisDto>> getThesesByStartDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<ThesisDto> theses = thesisService.getThesesByStartDateRange(startDate, endDate);
            return ResponseEntity.ok(theses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Savunma tarihi aralığına göre tezleri getir
    @GetMapping("/defense-date-range")
    public ResponseEntity<List<ThesisDto>> getThesesByDefenseDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<ThesisDto> theses = thesisService.getThesesByDefenseDateRange(startDate, endDate);
            return ResponseEntity.ok(theses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Search Operations

    // Başlık ile arama
    @GetMapping("/search/title")
    public ResponseEntity<List<ThesisDto>> searchThesesByTitle(@RequestParam String title) {
        try {
            List<ThesisDto> theses = thesisService.searchThesesByTitle(title);
            return ResponseEntity.ok(theses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Öğrenci adıyla arama
    @GetMapping("/search/student")
    public ResponseEntity<List<ThesisDto>> searchThesesByStudentName(@RequestParam String studentName) {
        try {
            List<ThesisDto> theses = thesisService.searchThesesByStudentName(studentName);
            return ResponseEntity.ok(theses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Supervisor adıyla arama
    @GetMapping("/search/supervisor")
    public ResponseEntity<List<ThesisDto>> searchThesesBySupervisorName(@RequestParam String supervisorName) {
        try {
            List<ThesisDto> theses = thesisService.searchThesesBySupervisorName(supervisorName);
            return ResponseEntity.ok(theses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Special Operations

    // Supervisor değiştir
    @PutMapping("/{id}/supervisor")
    public ResponseEntity<ThesisDto> changeSupervisor(
            @PathVariable Long id,
            @RequestParam Long supervisorId) {
        try {
            ThesisDto updatedThesis = thesisService.changeSupervisor(id, supervisorId);
            return ResponseEntity.ok(updatedThesis);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Savunma tarihi belirle
    @PutMapping("/{id}/defense-date")
    public ResponseEntity<ThesisDto> setDefenseDate(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate defenseDate) {
        try {
            ThesisDto updatedThesis = thesisService.setDefenseDate(id, defenseDate);
            return ResponseEntity.ok(updatedThesis);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Tezi teslim et
    @PutMapping("/{id}/submit")
    public ResponseEntity<ThesisDto> submitThesis(@PathVariable Long id) {
        try {
            ThesisDto updatedThesis = thesisService.submitThesis(id);
            return ResponseEntity.ok(updatedThesis);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Tezi savun
    @PutMapping("/{id}/defend")
    public ResponseEntity<ThesisDto> defendThesis(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate defenseDate) {
        try {
            ThesisDto updatedThesis = thesisService.defendThesis(id, defenseDate);
            return ResponseEntity.ok(updatedThesis);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Tezi onayla
    @PutMapping("/{id}/approve")
    public ResponseEntity<ThesisDto> approveThesis(@PathVariable Long id) {
        try {
            ThesisDto updatedThesis = thesisService.approveThesis(id);
            return ResponseEntity.ok(updatedThesis);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Tezi tamamla (eski metod - genel kullanım için)
    @PutMapping("/{id}/complete")
    public ResponseEntity<ThesisDto> completeThesis(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate defenseDate) {
        try {
            ThesisDto updatedThesis = thesisService.completeThesis(id, defenseDate);
            return ResponseEntity.ok(updatedThesis);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Tez durumunu güncelle
    @PutMapping("/{id}/status")
    public ResponseEntity<ThesisDto> updateThesisStatus(
            @PathVariable Long id,
            @RequestParam ThesisStatus status) {
        try {
            ThesisDto updatedThesis = thesisService.updateThesisStatus(id, status);
            return ResponseEntity.ok(updatedThesis);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Advanced Queries

    // Uzun süredir devam eden tezleri getir
    @GetMapping("/long-running")
    public ResponseEntity<List<ThesisDto>> getLongRunningTheses(
            @RequestParam(defaultValue = "24") int monthsThreshold) {
        try {
            List<ThesisDto> theses = thesisService.getLongRunningTheses(monthsThreshold);
            return ResponseEntity.ok(theses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Yakında savunması olan tezleri getir
    @GetMapping("/upcoming-defenses")
    public ResponseEntity<List<ThesisDto>> getUpcomingDefenses(
            @RequestParam(defaultValue = "30") int daysAhead) {
        try {
            List<ThesisDto> theses = thesisService.getUpcomingDefenses(daysAhead);
            return ResponseEntity.ok(theses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Statistics Endpoints

    // Tez tipine göre sayı
    @GetMapping("/statistics/by-type")
    public ResponseEntity<List<Object[]>> getThesisCountByType() {
        try {
            List<Object[]> statistics = thesisService.getThesisCountByType();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Tez durumuna göre sayı
    @GetMapping("/statistics/by-status")
    public ResponseEntity<List<Object[]>> getThesisCountByStatus() {
        try {
            List<Object[]> statistics = thesisService.getThesisCountByStatus();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Departmana göre tez sayısı
    @GetMapping("/statistics/by-department")
    public ResponseEntity<List<Object[]>> getThesisCountByDepartment() {
        try {
            List<Object[]> statistics = thesisService.getThesisCountByDepartment();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Supervisor'a göre tez sayısı
    @GetMapping("/statistics/by-supervisor")
    public ResponseEntity<List<Object[]>> getThesisCountBySupervisor() {
        try {
            List<Object[]> statistics = thesisService.getThesisCountBySupervisor();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Yıllara göre tez sayısı (başlangıç yılı)
    @GetMapping("/statistics/by-start-year")
    public ResponseEntity<List<Object[]>> getThesisCountByStartYear() {
        try {
            List<Object[]> statistics = thesisService.getThesisCountByStartYear();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Yıllara göre savunma sayısı
    @GetMapping("/statistics/defenses-by-year")
    public ResponseEntity<List<Object[]>> getThesisDefenseCountByYear() {
        try {
            List<Object[]> statistics = thesisService.getThesisDefenseCountByYear();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Ortalama tez süresi
    @GetMapping("/statistics/average-duration")
    public ResponseEntity<Double> getAverageThesisDurationInMonths() {
        try {
            Double avgDuration = thesisService.getAverageThesisDurationInMonths();
            return ResponseEntity.ok(avgDuration);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Tip ve duruma göre tez sayısı
    @GetMapping("/statistics/by-type-and-status")
    public ResponseEntity<List<Object[]>> getThesisCountByTypeAndStatus() {
        try {
            List<Object[]> statistics = thesisService.getThesisCountByTypeAndStatus();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Departman ve tip bazında tez analizi
    @GetMapping("/statistics/by-department-and-type")
    public ResponseEntity<List<Object[]>> getThesisAnalysisByDepartmentAndType() {
        try {
            List<Object[]> statistics = thesisService.getThesisAnalysisByDepartmentAndType();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // En fazla tez danışmanlığı yapan supervisor'lar
    @GetMapping("/statistics/supervisors-with-most-theses")
    public ResponseEntity<List<Object[]>> getSupervisorsWithMostTheses(
            @RequestParam(defaultValue = "3") int minTheses) {
        try {
            List<Object[]> statistics = thesisService.getSupervisorsWithMostTheses(minTheses);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Belirli dönemde onaylanan tez sayısı
    @GetMapping("/statistics/completed-in-period")
    public ResponseEntity<Long> getCompletedThesesCountInPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            Long count = thesisService.getCompletedThesesCountInPeriod(startDate, endDate);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Aktif tez sayısı
    @GetMapping("/statistics/active-count")
    public ResponseEntity<Long> getActiveThesesCount() {
        try {
            Long count = thesisService.getActiveThesesCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}