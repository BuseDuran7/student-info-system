package com.ege.controller;

import com.ege.dto.StudentProfileDto;
import com.ege.dto.GraduationStatusDto;
import com.ege.entities.enums.ProgramType;
import com.ege.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Tüm öğrencileri getir
    @GetMapping
    public ResponseEntity<List<StudentProfileDto>> getAllStudents() {
        try {
            List<StudentProfileDto> students = studentService.getAllStudents();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Aktif öğrencileri getir
    @GetMapping("/active")
    public ResponseEntity<List<StudentProfileDto>> getActiveStudents() {
        try {
            List<StudentProfileDto> students = studentService.getActiveStudents();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ID ile öğrenci getir
    @GetMapping("/{id}")
    public ResponseEntity<StudentProfileDto> getStudentById(@PathVariable Long id) {
        try {
            return studentService.getStudentById(id)
                    .map(student -> ResponseEntity.ok(student))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // User ID ile öğrenci getir
    @GetMapping("/user/{userId}")
    public ResponseEntity<StudentProfileDto> getStudentByUserId(@PathVariable Long userId) {
        try {
            return studentService.getStudentByUserId(userId)
                    .map(student -> ResponseEntity.ok(student))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Username ile öğrenci getir
    @GetMapping("/username/{username}")
    public ResponseEntity<StudentProfileDto> getStudentByUsername(@PathVariable String username) {
        try {
            return studentService.getStudentByUsername(username)
                    .map(student -> ResponseEntity.ok(student))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Student ID ile öğrenci getir
    @GetMapping("/student-id/{studentId}")
    public ResponseEntity<StudentProfileDto> getStudentByStudentId(@PathVariable String studentId) {
        try {
            return studentService.getStudentByStudentId(studentId)
                    .map(student -> ResponseEntity.ok(student))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Program'a göre öğrencileri getir
    @GetMapping("/program/{programId}")
    public ResponseEntity<List<StudentProfileDto>> getStudentsByProgram(@PathVariable Long programId) {
        try {
            List<StudentProfileDto> students = studentService.getStudentsByProgramId(programId);
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Program türüne göre öğrencileri getir
    @GetMapping("/program-type/{programType}")
    public ResponseEntity<List<StudentProfileDto>> getStudentsByProgramType(@PathVariable ProgramType programType) {
        try {
            List<StudentProfileDto> students = studentService.getStudentsByProgramType(programType);
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Danışmana göre öğrencileri getir
    @GetMapping("/advisor/{advisorId}")
    public ResponseEntity<List<StudentProfileDto>> getStudentsByAdvisor(@PathVariable Long advisorId) {
        try {
            List<StudentProfileDto> students = studentService.getStudentsByAdvisorId(advisorId);
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // İsimde arama yap
    @GetMapping("/search")
    public ResponseEntity<List<StudentProfileDto>> searchStudents(@RequestParam String name) {
        try {
            List<StudentProfileDto> students = studentService.searchStudentsByName(name);
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GPA'ya göre öğrencileri getir
    @GetMapping("/gpa/{minGpa}")
    public ResponseEntity<List<StudentProfileDto>> getStudentsByMinGpa(@PathVariable BigDecimal minGpa) {
        try {
            List<StudentProfileDto> students = studentService.getStudentsByMinGpa(minGpa);
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Mezun olan öğrencileri getir
    @GetMapping("/graduated")
    public ResponseEntity<List<StudentProfileDto>> getGraduatedStudents() {
        try {
            List<StudentProfileDto> students = studentService.getGraduatedStudents();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Mezuniyet şartlarını karşılayan öğrencileri getir
    @GetMapping("/eligible-for-graduation")
    public ResponseEntity<List<StudentProfileDto>> getStudentsEligibleForGraduation() {
        try {
            List<StudentProfileDto> students = studentService.getStudentsEligibleForGraduation();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Danışmanı olmayan öğrencileri getir
    @GetMapping("/without-advisor")
    public ResponseEntity<List<StudentProfileDto>> getStudentsWithoutAdvisor() {
        try {
            List<StudentProfileDto> students = studentService.getStudentsWithoutAdvisor();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Yeni öğrenci oluştur
    @PostMapping
    public ResponseEntity<StudentProfileDto> createStudent(@RequestBody StudentProfileDto studentDto) {
        try {
            StudentProfileDto createdStudent = studentService.createStudent(studentDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Öğrenci güncelle
    @PutMapping("/{id}")
    public ResponseEntity<StudentProfileDto> updateStudent(@PathVariable Long id, @RequestBody StudentProfileDto studentDto) {
        try {
            StudentProfileDto updatedStudent = studentService.updateStudent(id, studentDto);
            return ResponseEntity.ok(updatedStudent);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Danışman ata
    @PutMapping("/{studentId}/advisor/{advisorId}")
    public ResponseEntity<StudentProfileDto> assignAdvisor(@PathVariable Long studentId, @PathVariable Long advisorId) {
        try {
            StudentProfileDto updatedStudent = studentService.assignAdvisor(studentId, advisorId);
            return ResponseEntity.ok(updatedStudent);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Mezuniyet durumunu kontrol et
    @GetMapping("/{id}/graduation-status")
    public ResponseEntity<GraduationStatusDto> checkGraduationStatus(@PathVariable Long id) {
        try {
            GraduationStatusDto graduationStatus = studentService.checkGraduationStatus(id);
            return ResponseEntity.ok(graduationStatus);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Öğrenci sil (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Öğrenci aktifleştir
    @PatchMapping("/{id}/activate")
    public ResponseEntity<StudentProfileDto> activateStudent(@PathVariable Long id) {
        try {
            StudentProfileDto activatedStudent = studentService.activateStudent(id);
            return ResponseEntity.ok(activatedStudent);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Program bazında istatistik
    @GetMapping("/statistics/program")
    public ResponseEntity<Map<String, Object>> getStudentStatisticsByProgram() {
        try {
            List<Object[]> stats = studentService.getStudentStatisticsByProgram();
            Map<String, Object> response = new HashMap<>();

            for (Object[] stat : stats) {
                String programName = (String) stat[0];
                Long count = (Long) stat[1];
                response.put(programName, count);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Danışman bazında istatistik
    @GetMapping("/statistics/advisor")
    public ResponseEntity<Map<String, Object>> getStudentStatisticsByAdvisor() {
        try {
            List<Object[]> stats = studentService.getStudentStatisticsByAdvisor();
            Map<String, Object> response = new HashMap<>();

            for (Object[] stat : stats) {
                String advisorName = (String) stat[0];
                Long count = (Long) stat[1];
                response.put(advisorName, count);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Program bazında ortalama GPA
    @GetMapping("/statistics/gpa")
    public ResponseEntity<Map<String, Object>> getAverageGpaByProgram() {
        try {
            List<Object[]> stats = studentService.getAverageGpaByProgram();
            Map<String, Object> response = new HashMap<>();

            for (Object[] stat : stats) {
                String programName = (String) stat[0];
                Double avgGpa = (Double) stat[1];
                response.put(programName, avgGpa);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
