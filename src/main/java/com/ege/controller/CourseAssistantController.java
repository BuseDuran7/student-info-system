package com.ege.controller;

import com.ege.dto.CourseAssistantDto;
import com.ege.service.CourseAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/course-assistants")
@CrossOrigin(origins = "*")
public class CourseAssistantController {

    @Autowired
    private CourseAssistantService courseAssistantService;

    // Tüm course assistant'ları getir
    @GetMapping
    public ResponseEntity<List<CourseAssistantDto>> getAllCourseAssistants() {
        try {
            List<CourseAssistantDto> courseAssistants = courseAssistantService.getAllCourseAssistants();
            return ResponseEntity.ok(courseAssistants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Belirli bir course assistant ilişkisini getir
    @GetMapping("/course/{courseId}/assistant/{assistantId}")
    public ResponseEntity<CourseAssistantDto> getCourseAssistant(
            @PathVariable Long courseId,
            @PathVariable Long assistantId) {
        try {
            Optional<CourseAssistantDto> courseAssistant = courseAssistantService.getCourseAssistant(courseId, assistantId);
            return courseAssistant.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Course'a göre assistant'ları getir
    @GetMapping("/course/{courseId}/assistants")
    public ResponseEntity<List<CourseAssistantDto>> getAssistantsByCourse(@PathVariable Long courseId) {
        try {
            List<CourseAssistantDto> assistants = courseAssistantService.getAssistantsByCourseId(courseId);
            return ResponseEntity.ok(assistants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Assistant'a göre course'ları getir
    @GetMapping("/assistant/{assistantId}/courses")
    public ResponseEntity<List<CourseAssistantDto>> getCoursesByAssistant(@PathVariable Long assistantId) {
        try {
            List<CourseAssistantDto> courses = courseAssistantService.getCoursesByAssistantId(assistantId);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Course code'a göre assistant'ları getir
    @GetMapping("/course-code/{courseCode}/assistants")
    public ResponseEntity<List<CourseAssistantDto>> getAssistantsByCourseCode(@PathVariable String courseCode) {
        try {
            List<CourseAssistantDto> assistants = courseAssistantService.getAssistantsByCourseCode(courseCode);
            return ResponseEntity.ok(assistants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Employee ID'ye göre course'ları getir
    @GetMapping("/employee/{employeeId}/courses")
    public ResponseEntity<List<CourseAssistantDto>> getCoursesByEmployeeId(@PathVariable String employeeId) {
        try {
            List<CourseAssistantDto> courses = courseAssistantService.getCoursesByEmployeeId(employeeId);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Departmana göre course assistant'ları getir
    @GetMapping("/department/{department}")
    public ResponseEntity<List<CourseAssistantDto>> getCourseAssistantsByDepartment(@PathVariable String department) {
        try {
            List<CourseAssistantDto> courseAssistants = courseAssistantService.getCourseAssistantsByDepartment(department);
            return ResponseEntity.ok(courseAssistants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Semester'a göre course assistant'ları getir
    @GetMapping("/semester/{semester}")
    public ResponseEntity<List<CourseAssistantDto>> getCourseAssistantsBySemester(@PathVariable String semester) {
        try {
            List<CourseAssistantDto> courseAssistants = courseAssistantService.getCourseAssistantsBySemester(semester);
            return ResponseEntity.ok(courseAssistants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Aktif assistant'ların course'larını getir
    @GetMapping("/active-assistants")
    public ResponseEntity<List<CourseAssistantDto>> getActiveAssistantCourses() {
        try {
            List<CourseAssistantDto> courses = courseAssistantService.getActiveAssistantCourses();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Dual role assistant'ların course'larını getir
    @GetMapping("/student-assistants")
    public ResponseEntity<List<CourseAssistantDto>> getStudentAssistantCourses() {
        try {
            List<CourseAssistantDto> courses = courseAssistantService.getStudentAssistantCourses();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Sadece çalışan assistant'ların course'larını getir
    @GetMapping("/non-student-assistants")
    public ResponseEntity<List<CourseAssistantDto>> getNonStudentAssistantCourses() {
        try {
            List<CourseAssistantDto> courses = courseAssistantService.getNonStudentAssistantCourses();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Assistant'ı course'a ata
    @PostMapping("/assign")
    public ResponseEntity<CourseAssistantDto> assignAssistantToCourse(
            @RequestParam Long courseId,
            @RequestParam Long assistantId) {
        try {
            CourseAssistantDto courseAssistant = courseAssistantService.assignAssistantToCourse(courseId, assistantId);
            return ResponseEntity.status(HttpStatus.CREATED).body(courseAssistant);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Assistant atamasını kaldır
    @DeleteMapping("/course/{courseId}/assistant/{assistantId}")
    public ResponseEntity<Void> removeAssistantFromCourse(
            @PathVariable Long courseId,
            @PathVariable Long assistantId) {
        try {
            courseAssistantService.removeAssistantFromCourse(courseId, assistantId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Assistant'ın tüm course atamalarını kaldır
    @DeleteMapping("/assistant/{assistantId}/all-courses")
    public ResponseEntity<Void> removeAllCourseAssignments(@PathVariable Long assistantId) {
        try {
            courseAssistantService.removeAllCourseAssignments(assistantId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Course'un tüm assistant atamalarını kaldır
    @DeleteMapping("/course/{courseId}/all-assistants")
    public ResponseEntity<Void> removeAllAssistantAssignments(@PathVariable Long courseId) {
        try {
            courseAssistantService.removeAllAssistantAssignments(courseId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // İstatistik endpoint'leri

    // Departmana göre assistant sayısı
    @GetMapping("/statistics/assistants-by-department")
    public ResponseEntity<List<Object[]>> getAssistantCountByDepartment() {
        try {
            List<Object[]> statistics = courseAssistantService.getAssistantCountByDepartment();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Course'a göre assistant sayısı
    @GetMapping("/statistics/assistants-by-course")
    public ResponseEntity<List<Object[]>> getAssistantCountByCourse() {
        try {
            List<Object[]> statistics = courseAssistantService.getAssistantCountByCourse();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Assistant'a göre course sayısı
    @GetMapping("/statistics/courses-by-assistant")
    public ResponseEntity<List<Object[]>> getCourseCountByAssistant() {
        try {
            List<Object[]> statistics = courseAssistantService.getCourseCountByAssistant();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Semester'a göre course assistant sayısı
    @GetMapping("/statistics/by-semester")
    public ResponseEntity<List<Object[]>> getCourseAssistantCountBySemester() {
        try {
            List<Object[]> statistics = courseAssistantService.getCourseAssistantCountBySemester();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Dual role analizi
    @GetMapping("/statistics/dual-role-analysis")
    public ResponseEntity<List<Object[]>> getDualRoleAnalysis() {
        try {
            List<Object[]> statistics = courseAssistantService.getDualRoleAnalysis();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // En fazla assistant'a sahip course'lar
    @GetMapping("/statistics/courses-with-most-assistants")
    public ResponseEntity<List<Object[]>> getCoursesWithMostAssistants(
            @RequestParam(defaultValue = "2") int minAssistants) {
        try {
            List<Object[]> statistics = courseAssistantService.getCoursesWithMostAssistants(minAssistants);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // En fazla course'a yardım eden assistant'lar
    @GetMapping("/statistics/assistants-with-most-courses")
    public ResponseEntity<List<Object[]>> getAssistantsWithMostCourses(
            @RequestParam(defaultValue = "2") int minCourses) {
        try {
            List<Object[]> statistics = courseAssistantService.getAssistantsWithMostCourses(minCourses);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}