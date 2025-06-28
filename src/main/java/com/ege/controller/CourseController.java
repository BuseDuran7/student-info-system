package com.ege.controller;

import com.ege.dto.CourseDto;
import com.ege.entities.enums.CourseLevel;
import com.ege.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // Tüm dersleri getir
    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        try {
            List<CourseDto> courses = courseService.getAllCourses();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Aktif dersleri getir
    @GetMapping("/active")
    public ResponseEntity<List<CourseDto>> getActiveCourses() {
        try {
            List<CourseDto> courses = courseService.getActiveCourses();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ID ile ders getir
    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        try {
            return courseService.getCourseById(id)
                    .map(course -> ResponseEntity.ok(course))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Course code ile ders getir
    @GetMapping("/code/{code}")
    public ResponseEntity<CourseDto> getCourseByCode(@PathVariable String code) {
        try {
            return courseService.getCourseByCode(code)
                    .map(course -> ResponseEntity.ok(course))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Ders adında arama yap
    @GetMapping("/search")
    public ResponseEntity<List<CourseDto>> searchCourses(@RequestParam String name) {
        try {
            List<CourseDto> courses = courseService.searchCoursesByName(name);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Öğretim üyesine göre dersleri getir
    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<CourseDto>> getCoursesByInstructor(@PathVariable Long instructorId) {
        try {
            List<CourseDto> courses = courseService.getCoursesByInstructorId(instructorId);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Course level'a göre dersleri getir
    @GetMapping("/level/{level}")
    public ResponseEntity<List<CourseDto>> getCoursesByLevel(@PathVariable CourseLevel level) {
        try {
            List<CourseDto> courses = courseService.getCoursesByLevel(level);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Semester ve yıla göre dersleri getir
    @GetMapping("/semester/{semester}/year/{year}")
    public ResponseEntity<List<CourseDto>> getCoursesBySemesterAndYear(@PathVariable String semester, @PathVariable Integer year) {
        try {
            List<CourseDto> courses = courseService.getCoursesBySemesterAndYear(semester, year);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Kapasitesi dolu olmayan dersleri getir
    @GetMapping("/available")
    public ResponseEntity<List<CourseDto>> getCoursesWithAvailableCapacity() {
        try {
            List<CourseDto> courses = courseService.getCoursesWithAvailableCapacity();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Kapasitesi dolu olan dersleri getir
    @GetMapping("/full")
    public ResponseEntity<List<CourseDto>> getFullCourses() {
        try {
            List<CourseDto> courses = courseService.getFullCourses();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Düşük kayıtlı dersleri getir
    @GetMapping("/low-enrollment/{minEnrollment}")
    public ResponseEntity<List<CourseDto>> getCoursesWithLowEnrollment(@PathVariable Integer minEnrollment) {
        try {
            List<CourseDto> courses = courseService.getCoursesWithLowEnrollment(minEnrollment);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Departmana göre dersleri getir
    @GetMapping("/department/{department}")
    public ResponseEntity<List<CourseDto>> getCoursesByDepartment(@PathVariable String department) {
        try {
            List<CourseDto> courses = courseService.getCoursesByDepartment(department);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // En çok kayıtlı dersleri getir
    @GetMapping("/most-enrolled")
    public ResponseEntity<List<CourseDto>> getMostEnrolledCourses(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<CourseDto> courses = courseService.getMostEnrolledCourses(limit);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // En az kayıtlı dersleri getir
    @GetMapping("/least-enrolled")
    public ResponseEntity<List<CourseDto>> getLeastEnrolledCourses(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<CourseDto> courses = courseService.getLeastEnrolledCourses(limit);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Yeni ders oluştur
    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto courseDto) {
        try {
            CourseDto createdCourse = courseService.createCourse(courseDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Ders güncelle
    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id, @RequestBody CourseDto courseDto) {
        try {
            CourseDto updatedCourse = courseService.updateCourse(id, courseDto);
            return ResponseEntity.ok(updatedCourse);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Ders kapasitesini güncelle
    @PutMapping("/{id}/capacity/{newCapacity}")
    public ResponseEntity<CourseDto> updateCourseCapacity(@PathVariable Long id, @PathVariable Integer newCapacity) {
        try {
            CourseDto updatedCourse = courseService.updateCourseCapacity(id, newCapacity);
            return ResponseEntity.ok(updatedCourse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Ders sil (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Ders aktifleştir
    @PatchMapping("/{id}/activate")
    public ResponseEntity<CourseDto> activateCourse(@PathVariable Long id) {
        try {
            CourseDto activatedCourse = courseService.activateCourse(id);
            return ResponseEntity.ok(activatedCourse);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Course level bazında istatistik
    @GetMapping("/statistics/level")
    public ResponseEntity<Map<String, Object>> getCourseStatisticsByLevel() {
        try {
            List<Object[]> stats = courseService.getCourseStatisticsByLevel();
            Map<String, Object> response = new HashMap<>();

            for (Object[] stat : stats) {
                CourseLevel level = (CourseLevel) stat[0];
                Long count = (Long) stat[1];
                response.put(level.name(), count);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Öğretim üyesi bazında istatistik
    @GetMapping("/statistics/instructor")
    public ResponseEntity<Map<String, Object>> getCourseStatisticsByInstructor() {
        try {
            List<Object[]> stats = courseService.getCourseStatisticsByInstructor();
            Map<String, Object> response = new HashMap<>();

            for (Object[] stat : stats) {
                String instructorName = (String) stat[0];
                Long count = (Long) stat[1];
                response.put(instructorName, count);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Semester bazında istatistik
    @GetMapping("/statistics/semester")
    public ResponseEntity<Map<String, Object>> getCourseStatisticsBySemester() {
        try {
            List<Object[]> stats = courseService.getCourseStatisticsBySemester();
            Map<String, Object> response = new HashMap<>();

            for (Object[] stat : stats) {
                String semester = (String) stat[0];
                Long count = (Long) stat[1];
                response.put(semester != null ? semester : "Unspecified", count);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Departman bazında istatistik
    @GetMapping("/statistics/department")
    public ResponseEntity<Map<String, Object>> getCourseStatisticsByDepartment() {
        try {
            List<Object[]> stats = courseService.getCourseStatisticsByDepartment();
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

    // Genel istatistikler
    @GetMapping("/statistics/general")
    public ResponseEntity<Map<String, Object>> getGeneralStatistics() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("averageMaxCapacity", courseService.getAverageMaxCapacity());
            response.put("averageEnrollmentRate", courseService.getAverageEnrollmentRate());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
