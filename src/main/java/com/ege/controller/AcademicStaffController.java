package com.ege.controller;

import com.ege.dto.AcademicStaffDto;
import com.ege.entities.enums.Title;
import com.ege.service.AcademicStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/academic-staff")
@CrossOrigin(origins = "*")
public class AcademicStaffController {

    @Autowired
    private AcademicStaffService academicStaffService;

    // Tüm akademik personeli getir
    @GetMapping
    public ResponseEntity<List<AcademicStaffDto>> getAllAcademicStaff() {
        try {
            List<AcademicStaffDto> academicStaff = academicStaffService.getAllAcademicStaff();
            return ResponseEntity.ok(academicStaff);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Aktif akademik personeli getir
    @GetMapping("/active")
    public ResponseEntity<List<AcademicStaffDto>> getActiveAcademicStaff() {
        try {
            List<AcademicStaffDto> academicStaff = academicStaffService.getActiveAcademicStaff();
            return ResponseEntity.ok(academicStaff);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ID ile akademik personel getir
    @GetMapping("/{id}")
    public ResponseEntity<AcademicStaffDto> getAcademicStaffById(@PathVariable Long id) {
        try {
            return academicStaffService.getAcademicStaffById(id)
                    .map(academicStaff -> ResponseEntity.ok(academicStaff))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // User ID ile akademik personel getir
    @GetMapping("/user/{userId}")
    public ResponseEntity<AcademicStaffDto> getAcademicStaffByUserId(@PathVariable Long userId) {
        try {
            return academicStaffService.getAcademicStaffByUserId(userId)
                    .map(academicStaff -> ResponseEntity.ok(academicStaff))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Username ile akademik personel getir
    @GetMapping("/username/{username}")
    public ResponseEntity<AcademicStaffDto> getAcademicStaffByUsername(@PathVariable String username) {
        try {
            return academicStaffService.getAcademicStaffByUsername(username)
                    .map(academicStaff -> ResponseEntity.ok(academicStaff))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Ünvana göre akademik personel getir
    @GetMapping("/title/{title}")
    public ResponseEntity<List<AcademicStaffDto>> getAcademicStaffByTitle(@PathVariable Title title) {
        try {
            List<AcademicStaffDto> academicStaff = academicStaffService.getAcademicStaffByTitle(title);
            return ResponseEntity.ok(academicStaff);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Bölüme göre akademik personel getir
    @GetMapping("/department/{department}")
    public ResponseEntity<List<AcademicStaffDto>> getAcademicStaffByDepartment(@PathVariable String department) {
        try {
            List<AcademicStaffDto> academicStaff = academicStaffService.getAcademicStaffByDepartment(department);
            return ResponseEntity.ok(academicStaff);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // İsimde arama yap
    @GetMapping("/search")
    public ResponseEntity<List<AcademicStaffDto>> searchAcademicStaff(@RequestParam String name) {
        try {
            List<AcademicStaffDto> academicStaff = academicStaffService.searchAcademicStaffByName(name);
            return ResponseEntity.ok(academicStaff);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Kıdeme göre akademik personel getir
    @GetMapping("/seniority/{minYears}")
    public ResponseEntity<List<AcademicStaffDto>> getAcademicStaffBySeniority(@PathVariable Integer minYears) {
        try {
            List<AcademicStaffDto> academicStaff = academicStaffService.getAcademicStaffBySeniority(minYears);
            return ResponseEntity.ok(academicStaff);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Yeni akademik personel oluştur
    @PostMapping
    public ResponseEntity<AcademicStaffDto> createAcademicStaff(@RequestBody AcademicStaffDto academicStaffDto) {
        try {
            AcademicStaffDto createdAcademicStaff = academicStaffService.createAcademicStaff(academicStaffDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAcademicStaff);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Akademik personel güncelle
    @PutMapping("/{id}")
    public ResponseEntity<AcademicStaffDto> updateAcademicStaff(@PathVariable Long id, @RequestBody AcademicStaffDto academicStaffDto) {
        try {
            AcademicStaffDto updatedAcademicStaff = academicStaffService.updateAcademicStaff(id, academicStaffDto);
            return ResponseEntity.ok(updatedAcademicStaff);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Akademik personel sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAcademicStaff(@PathVariable Long id) {
        try {
            academicStaffService.deleteAcademicStaff(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Akademik personel aktifleştir
    @PatchMapping("/{id}/activate")
    public ResponseEntity<AcademicStaffDto> activateAcademicStaff(@PathVariable Long id) {
        try {
            AcademicStaffDto activatedAcademicStaff = academicStaffService.activateAcademicStaff(id);
            return ResponseEntity.ok(activatedAcademicStaff);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Bölüm bazında istatistik
    @GetMapping("/statistics/department")
    public ResponseEntity<Map<String, Object>> getAcademicStaffStatisticsByDepartment() {
        try {
            List<Object[]> stats = academicStaffService.getAcademicStaffStatisticsByDepartment();
            Map<String, Object> response = new HashMap<>();

            for (Object[] stat : stats) {
                String department = (String) stat[0];
                Long count = (Long) stat[1];
                response.put(department, count);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Ünvan bazında istatistik
    @GetMapping("/statistics/title")
    public ResponseEntity<Map<String, Object>> getAcademicStaffStatisticsByTitle() {
        try {
            List<Object[]> stats = academicStaffService.getAcademicStaffStatisticsByTitle();
            Map<String, Object> response = new HashMap<>();

            for (Object[] stat : stats) {
                Title title = (Title) stat[0];
                Long count = (Long) stat[1];
                response.put(title.name(), count);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}