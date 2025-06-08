package com.ege.entities;

import com.ege.entities.enums.LetterGrade;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "numeric_grade", precision = 5, scale = 2)
    private BigDecimal numericGrade;

    @Enumerated(EnumType.STRING)
    @Column(name = "letter_grade")
    private LetterGrade letterGrade;

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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public BigDecimal getNumericGrade() {
        return numericGrade;
    }

    public void setNumericGrade(BigDecimal numericGrade) {
        this.numericGrade = numericGrade;
    }

    public LetterGrade getLetterGrade() {
        return letterGrade;
    }

    public void setLetterGrade(LetterGrade letterGrade) {
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