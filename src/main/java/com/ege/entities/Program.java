package com.ege.entities;

import com.ege.entities.enums.ProgramType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity
@Table(name = "programs")
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ProgramType type;

    @NotNull
    @Column(name = "required_courses", nullable = false)
    private Integer requiredCourses;

    @NotNull
    @Column(name = "required_credits", nullable = false)
    private Integer requiredCredits;

    @NotNull
    @Column(name = "minimum_gpa", nullable = false, precision = 3, scale = 2)
    private BigDecimal minimumGpa;

    @NotNull
    @Column(name = "max_duration_years", nullable = false)
    private Integer maxDurationYears;

    @NotNull
    @Column(name = "has_thesis", nullable = false)
    private Boolean hasThesis = false;

    @NotNull
    @ColumnDefault("70")
    @Column(name = "passing_grade_masters", nullable = false)
    private Integer passingGradeMasters;

    @NotNull
    @ColumnDefault("75")
    @Column(name = "passing_grade_phd", nullable = false)
    private Integer passingGradePhd;

    @ColumnDefault("1")
    @Column(name = "is_active")
    private Boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProgramType getType() {
        return type;
    }

    public void setType(ProgramType type) {
        this.type = type;
    }

    public Integer getRequiredCourses() {
        return requiredCourses;
    }

    public void setRequiredCourses(Integer requiredCourses) {
        this.requiredCourses = requiredCourses;
    }

    public Integer getRequiredCredits() {
        return requiredCredits;
    }

    public void setRequiredCredits(Integer requiredCredits) {
        this.requiredCredits = requiredCredits;
    }

    public BigDecimal getMinimumGpa() {
        return minimumGpa;
    }

    public void setMinimumGpa(BigDecimal minimumGpa) {
        this.minimumGpa = minimumGpa;
    }

    public Integer getMaxDurationYears() {
        return maxDurationYears;
    }

    public void setMaxDurationYears(Integer maxDurationYears) {
        this.maxDurationYears = maxDurationYears;
    }

    public Boolean getHasThesis() {
        return hasThesis;
    }

    public void setHasThesis(Boolean hasThesis) {
        this.hasThesis = hasThesis;
    }

    public Integer getPassingGradeMasters() {
        return passingGradeMasters;
    }

    public void setPassingGradeMasters(Integer passingGradeMasters) {
        this.passingGradeMasters = passingGradeMasters;
    }

    public Integer getPassingGradePhd() {
        return passingGradePhd;
    }

    public void setPassingGradePhd(Integer passingGradePhd) {
        this.passingGradePhd = passingGradePhd;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}