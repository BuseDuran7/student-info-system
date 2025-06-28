// 11. GradeEntryDto - Not girişi için
package com.ege.dto;

import com.ege.entities.enums.LetterGrade;
import java.math.BigDecimal;

public class GradeEntryDto {
    private Long studentId;
    private Long courseId;
    private BigDecimal numericGrade;
    private LetterGrade letterGrade;
    private Boolean isPassed;

    // Getters and setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public BigDecimal getNumericGrade() { return numericGrade; }
    public void setNumericGrade(BigDecimal numericGrade) { this.numericGrade = numericGrade; }

    public LetterGrade getLetterGrade() { return letterGrade; }
    public void setLetterGrade(LetterGrade letterGrade) { this.letterGrade = letterGrade; }

    public Boolean getIsPassed() { return isPassed; }
    public void setIsPassed(Boolean isPassed) { this.isPassed = isPassed; }
}