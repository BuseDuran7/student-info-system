package com.ege.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "grades", indexes = {
        @Index(name = "course_id", columnList = "course_id"),
        @Index(name = "graded_by", columnList = "graded_by")
}, uniqueConstraints = {
        @UniqueConstraint(name = "unique_student_course_grade", columnNames = {"student_id", "course_id"})
})
public class Grade {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "student_id", nullable = false)
    private Student studentId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "course_id", nullable = false)
    private Cours courseId;

    @Column(name = "numeric_grade", precision = 5, scale = 2)
    private BigDecimal numericGrade;

    @Lob
    @Column(name = "letter_grade")
    private String letterGrade;

    @ColumnDefault("0")
    @Column(name = "is_passed")
    private Boolean isPassed;

    @Column(name = "grade_date")
    private LocalDate gradeDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graded_by")
    private AcademicStaff gradedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    public Cours getCourseId() {
        return courseId;
    }

    public void setCourseId(Cours courseId) {
        this.courseId = courseId;
    }

    public BigDecimal getNumericGrade() {
        return numericGrade;
    }

    public void setNumericGrade(BigDecimal numericGrade) {
        this.numericGrade = numericGrade;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }

    public Boolean getIsPassed() {
        return isPassed;
    }

    public void setIsPassed(Boolean isPassed) {
        this.isPassed = isPassed;
    }

    public LocalDate getGradeDate() {
        return gradeDate;
    }

    public void setGradeDate(LocalDate gradeDate) {
        this.gradeDate = gradeDate;
    }

    public AcademicStaff getGradedBy() {
        return gradedBy;
    }

    public void setGradedBy(AcademicStaff gradedBy) {
        this.gradedBy = gradedBy;
    }

}