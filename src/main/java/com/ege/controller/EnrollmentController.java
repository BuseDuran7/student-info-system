package com.ege.controller;

import com.ege.dto.EnrollmentDto;
import com.ege.dto.CourseEnrollmentRequestDto;
import com.ege.entities.enums.EnrollmentStatus;
import com.ege.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    // Tüm enrollments'ları getir
    @GetMapping
    public ResponseEntity<List<EnrollmentDto>> getAllEnrollments() {
        try {
            List<EnrollmentDto> enrollments = enrollmentService.getAllEnrollments();
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Aktif enrollments'ları getir
    @GetMapping("/active")
    public ResponseEntity<List<EnrollmentDto>> getActiveEnrollments() {
        try {
            List<EnrollmentDto> enrollments = enrollmentService.getActiveEnrollments();
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ID ile enrollment getir
    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDto> getEnrollmentById(@PathVariable Long id) {
        try {
            return enrollmentService.getEnrollmentById(id)
                    .map(enrollment -> ResponseEntity.ok(enrollment))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Student'a göre enrollments
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        try {
            List<EnrollmentDto> enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Student'ın aktif enrollments
    @GetMapping("/student/{studentId}/active")
    public ResponseEntity<List<EnrollmentDto>> getActiveEnrollmentsByStudent(@PathVariable Long studentId) {
        try {
            List<EnrollmentDto> enrollments = enrollmentService.getActiveEnrollmentsByStudentId(studentId);
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Course'a göre enrollments
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        try {
            List<EnrollmentDto> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Course'un aktif enrollments
    @GetMapping("/course/{courseId}/active")
    public ResponseEntity<List<EnrollmentDto>> getActiveEnrollmentsByCourse(@PathVariable Long courseId) {
        try {
            List<EnrollmentDto> enrollments = enrollmentService.getActiveEnrollmentsByCourseId(courseId);
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Status'a göre enrollments
    @GetMapping("/status/{status}")
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsByStatus(@PathVariable EnrollmentStatus status) {
        try {
            List<EnrollmentDto> enrollments = enrollmentService.getEnrollmentsByStatus(status);
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Student ve status'a göre enrollments
    @GetMapping("/student/{studentId}/status/{status}")
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsByStudentAndStatus(@PathVariable Long studentId, @PathVariable EnrollmentStatus status) {
        try {
            List<EnrollmentDto> enrollments = enrollmentService.getEnrollmentsByStudentIdAndStatus(studentId, status);
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Öğretim üyesine göre enrollments
    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsByInstructor(@PathVariable Long instructorId) {
        try {
            List<EnrollmentDto> enrollments = enrollmentService.getEnrollmentsByInstructorId(instructorId);
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Semester ve yıla göre enrollments
    @GetMapping("/semester/{semester}/year/{year}")
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsBySemesterAndYear(@PathVariable String semester, @PathVariable Integer year) {
        try {
            List<EnrollmentDto> enrollments = enrollmentService.getEnrollmentsBySemesterAndYear(semester, year);
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Student'ın semester ve yıla göre enrollments
    @GetMapping("/student/{studentId}/semester/{semester}/year/{year}")
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsByStudentAndSemesterAndYear(@PathVariable Long studentId, @PathVariable String semester, @PathVariable Integer year) {
        try {
            List<EnrollmentDto> enrollments = enrollmentService.getEnrollmentsByStudentIdAndSemesterAndYear(studentId, semester, year);
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Yeni enrollment oluştur
    @PostMapping
    public ResponseEntity<EnrollmentDto> createEnrollment(@RequestBody CourseEnrollmentRequestDto requestDto) {
        try {
            EnrollmentDto createdEnrollment = enrollmentService.createEnrollment(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEnrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Enrollment status güncelle
    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<EnrollmentDto> updateEnrollmentStatus(@PathVariable Long id, @PathVariable EnrollmentStatus status) {
        try {
            EnrollmentDto updatedEnrollment = enrollmentService.updateEnrollmentStatus(id, status);
            return ResponseEntity.ok(updatedEnrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Enrollment drop et
    @PutMapping("/{id}/drop")
    public ResponseEntity<EnrollmentDto> dropEnrollment(@PathVariable Long id) {
        try {
            EnrollmentDto droppedEnrollment = enrollmentService.dropEnrollment(id);
            return ResponseEntity.ok(droppedEnrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Enrollment tamamla
    @PutMapping("/{id}/complete")
    public ResponseEntity<EnrollmentDto> completeEnrollment(@PathVariable Long id) {
        try {
            EnrollmentDto completedEnrollment = enrollmentService.completeEnrollment(id);
            return ResponseEntity.ok(completedEnrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Enrollment sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        try {
            enrollmentService.deleteEnrollment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Student'ın course'a kayıt olup olmadığını kontrol et
    @GetMapping("/check/student/{studentId}/course/{courseId}")
    public ResponseEntity<Map<String, Object>> checkStudentEnrollment(@PathVariable Long studentId, @PathVariable Long courseId) {
        try {
            boolean isEnrolled = enrollmentService.isStudentEnrolledInCourse(studentId, courseId);
            Map<String, Object> response = new HashMap<>();
            response.put("isEnrolled", isEnrolled);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Student'ın enrollment sayıları
    @GetMapping("/student/{studentId}/count")
    public ResponseEntity<Map<String, Object>> getStudentEnrollmentCounts(@PathVariable Long studentId) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("totalEnrollments", enrollmentService.getStudentTotalEnrollmentCount(studentId));
            response.put("activeEnrollments", enrollmentService.getStudentActiveEnrollmentCount(studentId));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Course'un enrollment sayısı
    @GetMapping("/course/{courseId}/count")
    public ResponseEntity<Map<String, Object>> getCourseEnrollmentCount(@PathVariable Long courseId) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("activeEnrollments", enrollmentService.getCourseActiveEnrollmentCount(courseId));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Duplicate enrollment'ları bul
    @GetMapping("/duplicates")
    public ResponseEntity<List<EnrollmentDto>> findDuplicateEnrollments() {
        try {
            List<EnrollmentDto> duplicates = enrollmentService.findDuplicateEnrollments();
            return ResponseEntity.ok(duplicates);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Status bazında istatistik
    @GetMapping("/statistics/status")
    public ResponseEntity<Map<String, Object>> getEnrollmentStatisticsByStatus() {
        try {
            List<Object[]> stats = enrollmentService.getEnrollmentStatisticsByStatus();
            Map<String, Object> response = new HashMap<>();

            for (Object[] stat : stats) {
                EnrollmentStatus status = (EnrollmentStatus) stat[0];
                Long count = (Long) stat[1];
                response.put(status.name(), count);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Course bazında istatistik
    @GetMapping("/statistics/course")
    public ResponseEntity<List<Object[]>> getEnrollmentStatisticsByCourse() {
        try {
            List<Object[]> stats = enrollmentService.getEnrollmentStatisticsByCourse();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Program bazında istatistik
    @GetMapping("/statistics/program")
    public ResponseEntity<List<Object[]>> getEnrollmentStatisticsByProgram() {
        try {
            List<Object[]> stats = enrollmentService.getEnrollmentStatisticsByProgram();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Semester bazında istatistik
    @GetMapping("/statistics/semester")
    public ResponseEntity<List<Object[]>> getEnrollmentStatisticsBySemester() {
        try {
            List<Object[]> stats = enrollmentService.getEnrollmentStatisticsBySemester();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Öğretim üyesi bazında istatistik
    @GetMapping("/statistics/instructor")
    public ResponseEntity<List<Object[]>> getEnrollmentStatisticsByInstructor() {
        try {
            List<Object[]> stats = enrollmentService.getEnrollmentStatisticsByInstructor();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Drop rate istatistikleri
    @GetMapping("/statistics/drop-rate")
    public ResponseEntity<List<Object[]>> getDropRateByCourse() {
        try {
            List<Object[]> stats = enrollmentService.getDropRateByCourse();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Student course load analizi
    @GetMapping("/statistics/student-load")
    public ResponseEntity<List<Object[]>> getStudentCourseLoad() {
        try {
            List<Object[]> stats = enrollmentService.getStudentCourseLoad();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
