package com.ege.controller;

import com.ege.dto.ResearchAssistantDto;
import com.ege.service.ResearchAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/research-assistants")
@CrossOrigin(origins = "*")
public class ResearchAssistantController {

    @Autowired
    private ResearchAssistantService researchAssistantService;

    // Tüm araştırma görevlilerini getir
    @GetMapping
    public ResponseEntity<List<ResearchAssistantDto>> getAllResearchAssistants() {
        try {
            List<ResearchAssistantDto> researchAssistants = researchAssistantService.getAllResearchAssistants();
            return ResponseEntity.ok(researchAssistants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Aktif araştırma görevlilerini getir
    @GetMapping("/active")
    public ResponseEntity<List<ResearchAssistantDto>> getActiveResearchAssistants() {
        try {
            List<ResearchAssistantDto> researchAssistants = researchAssistantService.getActiveResearchAssistants();
            return ResponseEntity.ok(researchAssistants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ID ile araştırma görevlisi getir
    @GetMapping("/{id}")
    public ResponseEntity<ResearchAssistantDto> getResearchAssistantById(@PathVariable Long id) {
        try {
            return researchAssistantService.getResearchAssistantById(id)
                    .map(researchAssistant -> ResponseEntity.ok(researchAssistant))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // User ID ile araştırma görevlisi getir
    @GetMapping("/user/{userId}")
    public ResponseEntity<ResearchAssistantDto> getResearchAssistantByUserId(@PathVariable Long userId) {
        try {
            return researchAssistantService.getResearchAssistantByUserId(userId)
                    .map(researchAssistant -> ResponseEntity.ok(researchAssistant))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Username ile araştırma görevlisi getir
    @GetMapping("/username/{username}")
    public ResponseEntity<ResearchAssistantDto> getResearchAssistantByUsername(@PathVariable String username) {
        try {
            return researchAssistantService.getResearchAssistantByUsername(username)
                    .map(researchAssistant -> ResponseEntity.ok(researchAssistant))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Employee ID ile araştırma görevlisi getir
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ResearchAssistantDto> getResearchAssistantByEmployeeId(@PathVariable String employeeId) {
        try {
            return researchAssistantService.getResearchAssistantByEmployeeId(employeeId)
                    .map(researchAssistant -> ResponseEntity.ok(researchAssistant))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Student ID ile araştırma görevlisi getir (dual role)
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ResearchAssistantDto> getResearchAssistantByStudentId(@PathVariable Long studentId) {
        try {
            return researchAssistantService.getResearchAssistantByStudentId(studentId)
                    .map(researchAssistant -> ResponseEntity.ok(researchAssistant))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Departmana göre araştırma görevlilerini getir
    @GetMapping("/department/{department}")
    public ResponseEntity<List<ResearchAssistantDto>> getResearchAssistantsByDepartment(@PathVariable String department) {
        try {
            List<ResearchAssistantDto> researchAssistants = researchAssistantService.getResearchAssistantsByDepartment(department);
            return ResponseEntity.ok(researchAssistants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Dual role araştırma görevlilerini getir
    @GetMapping("/dual-role")
    public ResponseEntity<List<ResearchAssistantDto>> getStudentResearchAssistants() {
        try {
            List<ResearchAssistantDto> researchAssistants = researchAssistantService.getStudentResearchAssistants();
            return ResponseEntity.ok(researchAssistants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Sadece çalışan araştırma görevlilerini getir
    @GetMapping("/non-student")
    public ResponseEntity<List<ResearchAssistantDto>> getNonStudentResearchAssistants() {
        try {
            List<ResearchAssistantDto> researchAssistants = researchAssistantService.getNonStudentResearchAssistants();
            return ResponseEntity.ok(researchAssistants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // İsimde arama yap
    @GetMapping("/search")
    public ResponseEntity<List<ResearchAssistantDto>> searchResearchAssistants(@RequestParam String name) {
        try {
            List<ResearchAssistantDto> researchAssistants = researchAssistantService.searchResearchAssistantsByName(name);
            return ResponseEntity.ok(researchAssistants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Maaş aralığında araştırma görevlilerini getir
    @GetMapping("/salary")
    public ResponseEntity<List<ResearchAssistantDto>> getResearchAssistantsBySalaryRange(@RequestParam BigDecimal minSalary, @RequestParam BigDecimal maxSalary) {
        try {
            List<ResearchAssistantDto> researchAssistants = researchAssistantService.getResearchAssistantsBySalaryRange(minSalary, maxSalary);
            return ResponseEntity.ok(researchAssistants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Kıdem sırasına göre araştırma görevlilerini getir
    @GetMapping("/by-seniority")
    public ResponseEntity<List<ResearchAssistantDto>> getResearchAssistantsBySeniority() {
        try {
            List<ResearchAssistantDto> researchAssistants = researchAssistantService.getResearchAssistantsBySeniority();
            return ResponseEntity.ok(researchAssistants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Maaş sırasına göre araştırma görevlilerini getir
    @GetMapping("/by-salary")
    public ResponseEntity<List<ResearchAssistantDto>> getResearchAssistantsBySalary() {
        try {
            List<ResearchAssistantDto> researchAssistants = researchAssistantService.getResearchAssistantsBySalary();
            return ResponseEntity.ok(researchAssistants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Yeni araştırma görevlisi oluştur
    @PostMapping
    public ResponseEntity<ResearchAssistantDto> createResearchAssistant(@RequestBody ResearchAssistantDto researchAssistantDto) {
        try {
            ResearchAssistantDto createdResearchAssistant = researchAssistantService.createResearchAssistant(researchAssistantDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdResearchAssistant);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Araştırma görevlisi güncelle
    @PutMapping("/{id}")
    public ResponseEntity<ResearchAssistantDto> updateResearchAssistant(@PathVariable Long id, @RequestBody ResearchAssistantDto researchAssistantDto) {
        try {
            ResearchAssistantDto updatedResearchAssistant = researchAssistantService.updateResearchAssistant(id, researchAssistantDto);
            return ResponseEntity.ok(updatedResearchAssistant);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Student rolü ata (dual role oluştur)
    @PutMapping("/{researchAssistantId}/assign-student/{studentId}")
    public ResponseEntity<ResearchAssistantDto> assignStudentRole(@PathVariable Long researchAssistantId, @PathVariable Long studentId) {
        try {
            ResearchAssistantDto updatedResearchAssistant = researchAssistantService.assignStudentRole(researchAssistantId, studentId);
            return ResponseEntity.ok(updatedResearchAssistant);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Student rolünü kaldır (dual role'u sonlandır)
    @PutMapping("/{researchAssistantId}/remove-student")
    public ResponseEntity<ResearchAssistantDto> removeStudentRole(@PathVariable Long researchAssistantId) {
        try {
            ResearchAssistantDto updatedResearchAssistant = researchAssistantService.removeStudentRole(researchAssistantId);
            return ResponseEntity.ok(updatedResearchAssistant);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Araştırma görevlisi sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResearchAssistant(@PathVariable Long id) {
        try {
            researchAssistantService.deleteResearchAssistant(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Araştırma görevlisi aktifleştir
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ResearchAssistantDto> activateResearchAssistant(@PathVariable Long id) {
        try {
            ResearchAssistantDto activatedResearchAssistant = researchAssistantService.activateResearchAssistant(id);
            return ResponseEntity.ok(activatedResearchAssistant);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Departman bazında istatistik
    @GetMapping("/statistics/department")
    public ResponseEntity<List<Object[]>> getResearchAssistantStatisticsByDepartment() {
        try {
            List<Object[]> stats = researchAssistantService.getResearchAssistantStatisticsByDepartment();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Dual role analizi departman bazında
    @GetMapping("/statistics/dual-role")
    public ResponseEntity<List<Object[]>> getDualRoleAnalysisByDepartment() {
        try {
            List<Object[]> analysis = researchAssistantService.getDualRoleAnalysisByDepartment();
            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Maaş istatistikleri
    @GetMapping("/statistics/salary")
    public ResponseEntity<Map<String, Object>> getSalaryStatistics() {
        try {
            List<Object[]> stats = researchAssistantService.getSalaryStatistics();
            Map<String, Object> response = new HashMap<>();

            if (!stats.isEmpty()) {
                Object[] stat = stats.get(0);
                response.put("averageSalary", stat[0]);
                response.put("minimumSalary", stat[1]);
                response.put("maximumSalary", stat[2]);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Departman bazında ortalama maaş
    @GetMapping("/statistics/salary-by-department")
    public ResponseEntity<List<Object[]>> getAverageSalaryByDepartment() {
        try {
            List<Object[]> stats = researchAssistantService.getAverageSalaryByDepartment();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Yıl bazında işe alım sayısı
    @GetMapping("/statistics/hiring-by-year")
    public ResponseEntity<List<Object[]>> getHiringCountByYear() {
        try {
            List<Object[]> stats = researchAssistantService.getHiringCountByYear();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Kıdem dağılımı
    @GetMapping("/statistics/seniority-distribution")
    public ResponseEntity<List<Object[]>> getSeniorityDistribution() {
        try {
            List<Object[]> distribution = researchAssistantService.getSeniorityDistribution();
            return ResponseEntity.ok(distribution);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Program türüne göre dual role sayısı
    @GetMapping("/statistics/dual-role-by-program")
    public ResponseEntity<List<Object[]>> getDualRoleCountByProgramType() {
        try {
            List<Object[]> stats = researchAssistantService.getDualRoleCountByProgramType();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}