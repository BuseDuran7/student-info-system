// 5. GradeDto - Not bilgileri için
package com.ege.dto;

import com.ege.entities.enums.LetterGrade;
import java.math.BigDecimal;
import java.time.LocalDate;

public class GradeDto {
    private Long id;
    private String courseCode;
    private String courseName;
    private Integer credits;
    private BigDecimal numericGrade;
    private LetterGrade letterGrade;
    private Boolean passed;
    private LocalDate gradeDate;
    private String gradedBy;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public BigDecimal getNumericGrade() { return numericGrade; }
    public void setNumericGrade(BigDecimal numericGrade) { this.numericGrade = numericGrade; }

    public LetterGrade getLetterGrade() { return letterGrade; }
    public void setLetterGrade(LetterGrade letterGrade) { this.letterGrade = letterGrade; }

    public Boolean getPassed() { return passed; }
    public void setPassed(Boolean passed) { this.passed = passed; }

    public LocalDate getGradeDate() { return gradeDate; }
    public void setGradeDate(LocalDate gradeDate) { this.gradeDate = gradeDate; }

    public String getGradedBy() { return gradedBy; }
    public void setGradedBy(String gradedBy) { this.gradedBy = gradedBy; }
}