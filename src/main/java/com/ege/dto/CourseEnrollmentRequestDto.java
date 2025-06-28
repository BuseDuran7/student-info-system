// 10. CourseEnrollmentRequestDto - Ders kayıt talebi için
package com.ege.dto;

public class CourseEnrollmentRequestDto {
    private Long studentId;
    private Long courseId;
    private String semester;
    private Integer year;

    // Getters and setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
}