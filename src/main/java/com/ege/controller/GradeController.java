package com.ege.controller;

import com.ege.dto.GradeDto;
import com.ege.dto.GradeEntryDto;
import com.ege.entities.enums.LetterGrade;
import com.ege.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/grades")
@CrossOrigin(origins = "*")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    // Tüm notları getir
    @GetMapping
    public ResponseEntity<List<GradeDto>> getAllGrades() {
        try {
            List<GradeDto> grades = gradeService.getAllGrades();
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ID ile not getir
    @GetMapping("/{id}")
    public ResponseEntity<GradeDto> getGradeById(@PathVariable Long id) {
        try {
            return gradeService.getGradeById(id)
                    .map(grade -> ResponseEntity.ok(grade))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Student'a göre notları getir
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<GradeDto>> getGradesByStudent(@PathVariable Long studentId) {
        try {
            List<GradeDto> grades = gradeService.getGradesByStudentId(studentId);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Student'ın transcript'ini getir
    @GetMapping("/student/{studentId}/transcript")
    public ResponseEntity<List<GradeDto>> getStudentTranscript(@PathVariable Long studentId) {
        try {
            List<GradeDto> transcript = gradeService.getStudentTranscript(studentId);
            return ResponseEntity.ok(transcript);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Student'ın geçtiği dersler
    @GetMapping("/student/{studentId}/passed")
    public ResponseEntity<List<GradeDto>> getPassedGradesByStudent(@PathVariable Long studentId) {
        try {
            List<GradeDto> grades = gradeService.getPassedGradesByStudentId(studentId);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Student'ın kaldığı dersler
    @GetMapping("/student/{studentId}/failed")
    public ResponseEntity<List<GradeDto>> getFailedGradesByStudent(@PathVariable Long studentId) {
        try {
            List<GradeDto> grades = gradeService.getFailedGradesByStudentId(studentId);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Course'a göre notları getir
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<GradeDto>> getGradesByCourse(@PathVariable Long courseId) {
        try {
            List<GradeDto> grades = gradeService.getGradesByCourseId(courseId);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Course'da geçen öğrenciler
    @GetMapping("/course/{courseId}/passed")
    public ResponseEntity<List<GradeDto>> getPassedGradesByCourse(@PathVariable Long courseId) {
        try {
            List<GradeDto> grades = gradeService.getPassedGradesByCourseId(courseId);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Course'da kalan öğrenciler
    @GetMapping("/course/{courseId}/failed")
    public ResponseEntity<List<GradeDto>> getFailedGradesByCourse(@PathVariable Long courseId) {
        try {
            List<GradeDto> grades = gradeService.getFailedGradesByCourseId(courseId);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Öğretim üyesinin verdiği notlar
    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<GradeDto>> getGradesByInstructor(@PathVariable Long instructorId) {
        try {
            List<GradeDto> grades = gradeService.getGradesByInstructorId(instructorId);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Letter grade'e göre notlar
    @GetMapping("/letter-grade/{letterGrade}")
    public ResponseEntity<List<GradeDto>> getGradesByLetterGrade(@PathVariable LetterGrade letterGrade) {
        try {
            List<GradeDto> grades = gradeService.getGradesByLetterGrade(letterGrade);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // En yüksek notlar
    @GetMapping("/top/{limit}")
    public ResponseEntity<List<GradeDto>> getTopGrades(@PathVariable int limit) {
        try {
            List<GradeDto> grades = gradeService.getTopGrades(limit);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // En düşük notlar
    @GetMapping("/bottom/{limit}")
    public ResponseEntity<List<GradeDto>> getBottomGrades(@PathVariable int limit) {
        try {
            List<GradeDto> grades = gradeService.getBottomGrades(limit);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Course'da en yüksek notlar
    @GetMapping("/course/{courseId}/top/{limit}")
    public ResponseEntity<List<GradeDto>> getTopGradesByCourse(@PathVariable Long courseId, @PathVariable int limit) {
        try {
            List<GradeDto> grades = gradeService.getTopGradesByCourseId(courseId, limit);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Yeni not oluştur/güncelle
    @PostMapping
    public ResponseEntity<GradeDto> createOrUpdateGrade(@RequestBody GradeEntryDto gradeEntryDto) {
        try {
            GradeDto createdGrade = gradeService.createOrUpdateGrade(gradeEntryDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGrade);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Not güncelle
    @PutMapping("/{id}")
    public ResponseEntity<GradeDto> updateGrade(@PathVariable Long id, @RequestBody GradeEntryDto gradeEntryDto) {
        try {
            GradeDto updatedGrade = gradeService.updateGrade(id, gradeEntryDto);
            return ResponseEntity.ok(updatedGrade);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Not sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        try {
            gradeService.deleteGrade(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Course ortalama notu
    @GetMapping("/course/{courseId}/average")
    public ResponseEntity<Map<String, Object>> getCourseAverageGrade(@PathVariable Long courseId) {
        try {
            Double average = gradeService.getCourseAverageGrade(courseId);
            Map<String, Object> response = new HashMap<>();
            response.put("courseId", courseId);
            response.put("averageGrade", average);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Student ortalama notu
    @GetMapping("/student/{studentId}/average")
    public ResponseEntity<Map<String, Object>> getStudentAverageGrade(@PathVariable Long studentId) {
        try {
            Double average = gradeService.getStudentAverageGrade(studentId);
            Map<String, Object> response = new HashMap<>();
            response.put("studentId", studentId);
            response.put("averageGrade", average);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Course geçme oranı
    @GetMapping("/course/{courseId}/pass-rate")
    public ResponseEntity<Map<String, Object>> getCoursePassRate(@PathVariable Long courseId) {
        try {
            Double passRate = gradeService.getCoursePassRate(courseId);
            Map<String, Object> response = new HashMap<>();
            response.put("courseId", courseId);
            response.put("passRate", passRate);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Öğretim üyesi geçme oranı
    @GetMapping("/instructor/{instructorId}/pass-rate")
    public ResponseEntity<Map<String, Object>> getInstructorPassRate(@PathVariable Long instructorId) {
        try {
            Double passRate = gradeService.getInstructorPassRate(instructorId);
            Map<String, Object> response = new HashMap<>();
            response.put("instructorId", instructorId);
            response.put("passRate", passRate);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Letter grade dağılımı - Course bazında
    @GetMapping("/course/{courseId}/letter-grade-distribution")
    public ResponseEntity<List<Object[]>> getLetterGradeDistributionByCourse(@PathVariable Long courseId) {
        try {
            List<Object[]> distribution = gradeService.getLetterGradeDistributionByCourse(courseId);
            return ResponseEntity.ok(distribution);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Letter grade dağılımı - Student bazında
    @GetMapping("/student/{studentId}/letter-grade-distribution")
    public ResponseEntity<List<Object[]>> getLetterGradeDistributionByStudent(@PathVariable Long studentId) {
        try {
            List<Object[]> distribution = gradeService.getLetterGradeDistributionByStudent(studentId);
            return ResponseEntity.ok(distribution);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Course bazında istatistik
    @GetMapping("/statistics/course")
    public ResponseEntity<List<Object[]>> getCourseGradeStatistics() {
        try {
            List<Object[]> stats = gradeService.getCourseGradeStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Student bazında istatistik
    @GetMapping("/statistics/student")
    public ResponseEntity<List<Object[]>> getStudentGradeStatistics() {
        try {
            List<Object[]> stats = gradeService.getStudentGradeStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Öğretim üyesi bazında istatistik
    @GetMapping("/statistics/instructor")
    public ResponseEntity<List<Object[]>> getInstructorGradeStatistics() {
        try {
            List<Object[]> stats = gradeService.getInstructorGradeStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Program bazında istatistik
    @GetMapping("/statistics/program")
    public ResponseEntity<List<Object[]>> getProgramGradeStatistics() {
        try {
            List<Object[]> stats = gradeService.getProgramGradeStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Semester bazında istatistik
    @GetMapping("/statistics/semester")
    public ResponseEntity<List<Object[]>> getSemesterGradeStatistics() {
        try {
            List<Object[]> stats = gradeService.getSemesterGradeStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Grade range dağılımı
    @GetMapping("/statistics/range-distribution")
    public ResponseEntity<List<Object[]>> getGradeRangeDistribution() {
        try {
            List<Object[]> distribution = gradeService.getGradeRangeDistribution();
            return ResponseEntity.ok(distribution);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
