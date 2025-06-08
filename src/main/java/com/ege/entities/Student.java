package com.ege.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "students", indexes = {
        @Index(name = "program_id", columnList = "program_id"),
        @Index(name = "advisor_id", columnList = "advisor_id")
})
public class Student {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    @NotNull
    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @Column(name = "expected_graduation_date")
    private LocalDate expectedGraduationDate;

    @ColumnDefault("0.00")
    @Column(name = "gpa", precision = 3, scale = 2)
    private BigDecimal gpa;

    @ColumnDefault("0")
    @Column(name = "total_credits")
    private Integer totalCredits;

    @ColumnDefault("0")
    @Column(name = "completed_courses")
    private Integer completedCourses;

    @ColumnDefault("0")
    @Column(name = "thesis_completed")
    private Boolean thesisCompleted;

    @ColumnDefault("0")
    @Column(name = "is_graduated")
    private Boolean isGraduated;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "advisor_id")
    private AcademicStaff advisor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public LocalDate getExpectedGraduationDate() {
        return expectedGraduationDate;
    }

    public void setExpectedGraduationDate(LocalDate expectedGraduationDate) {
        this.expectedGraduationDate = expectedGraduationDate;
    }

    public BigDecimal getGpa() {
        return gpa;
    }

    public void setGpa(BigDecimal gpa) {
        this.gpa = gpa;
    }

    public Integer getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(Integer totalCredits) {
        this.totalCredits = totalCredits;
    }

    public Integer getCompletedCourses() {
        return completedCourses;
    }

    public void setCompletedCourses(Integer completedCourses) {
        this.completedCourses = completedCourses;
    }

    public Boolean getThesisCompleted() {
        return thesisCompleted;
    }

    public void setThesisCompleted(Boolean thesisCompleted) {
        this.thesisCompleted = thesisCompleted;
    }

    public Boolean getIsGraduated() {
        return isGraduated;
    }

    public void setIsGraduated(Boolean isGraduated) {
        this.isGraduated = isGraduated;
    }

    public AcademicStaff getAdvisor() {
        return advisor;
    }

    public void setAdvisor(AcademicStaff advisor) {
        this.advisor = advisor;
    }
}