// 4. ProgramDto - Program bilgileri için
package com.ege.dto;

import com.ege.entities.enums.ProgramType;
import java.math.BigDecimal;

public class ProgramDto {
    private Long id;
    private String name;
    private ProgramType type;
    private Integer requiredCourses;
    private Integer requiredCredits;
    private BigDecimal minimumGpa;
    private Integer maxDurationYears;
    private Boolean hasThesis;
    private Integer passingGradeMasters;
    private Integer passingGradePhd;
    private Boolean isActive;
    private Integer enrolledStudentsCount;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ProgramType getType() { return type; }
    public void setType(ProgramType type) { this.type = type; }

    public Integer getRequiredCourses() { return requiredCourses; }
    public void setRequiredCourses(Integer requiredCourses) { this.requiredCourses = requiredCourses; }

    public Integer getRequiredCredits() { return requiredCredits; }
    public void setRequiredCredits(Integer requiredCredits) { this.requiredCredits = requiredCredits; }

    public BigDecimal getMinimumGpa() { return minimumGpa; }
    public void setMinimumGpa(BigDecimal minimumGpa) { this.minimumGpa = minimumGpa; }

    public Integer getMaxDurationYears() { return maxDurationYears; }
    public void setMaxDurationYears(Integer maxDurationYears) { this.maxDurationYears = maxDurationYears; }

    public Boolean getHasThesis() { return hasThesis; }
    public void setHasThesis(Boolean hasThesis) { this.hasThesis = hasThesis; }

    public Integer getPassingGradeMasters() { return passingGradeMasters; }
    public void setPassingGradeMasters(Integer passingGradeMasters) { this.passingGradeMasters = passingGradeMasters; }

    public Integer getPassingGradePhd() { return passingGradePhd; }
    public void setPassingGradePhd(Integer passingGradePhd) { this.passingGradePhd = passingGradePhd; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Integer getEnrolledStudentsCount() { return enrolledStudentsCount; }
    public void setEnrolledStudentsCount(Integer enrolledStudentsCount) { this.enrolledStudentsCount = enrolledStudentsCount; }
}