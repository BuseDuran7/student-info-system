// 9. GraduationStatusDto'yu tamamlayalım
package com.ege.dto;

import java.math.BigDecimal;
import java.util.List;

public class GraduationStatusDto {
    private Long studentId;
    private String studentName;
    private String programName;
    private boolean canGraduate;
    private String graduationStatus;

    // Gereksinimler
    private Integer requiredCourses;
    private Integer completedCourses;
    private boolean courseRequirementMet;

    private Integer requiredCredits;
    private Integer totalCredits;
    private boolean creditRequirementMet;

    private BigDecimal minimumGpa;
    private BigDecimal currentGpa;
    private boolean gpaRequirementMet;

    private boolean thesisRequired;
    private boolean thesisCompleted;
    private boolean thesisRequirementMet;

    private List<String> missingRequirements;
    private String nextSteps;

    // Getters and setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getProgramName() { return programName; }
    public void setProgramName(String programName) { this.programName = programName; }

    public boolean isCanGraduate() { return canGraduate; }
    public void setCanGraduate(boolean canGraduate) { this.canGraduate = canGraduate; }

    public String getGraduationStatus() { return graduationStatus; }
    public void setGraduationStatus(String graduationStatus) { this.graduationStatus = graduationStatus; }

    public Integer getRequiredCourses() { return requiredCourses; }
    public void setRequiredCourses(Integer requiredCourses) { this.requiredCourses = requiredCourses; }

    public Integer getCompletedCourses() { return completedCourses; }
    public void setCompletedCourses(Integer completedCourses) { this.completedCourses = completedCourses; }

    public boolean isCourseRequirementMet() { return courseRequirementMet; }
    public void setCourseRequirementMet(boolean courseRequirementMet) { this.courseRequirementMet = courseRequirementMet; }

    public Integer getRequiredCredits() { return requiredCredits; }
    public void setRequiredCredits(Integer requiredCredits) { this.requiredCredits = requiredCredits; }

    public Integer getTotalCredits() { return totalCredits; }
    public void setTotalCredits(Integer totalCredits) { this.totalCredits = totalCredits; }

    public boolean isCreditRequirementMet() { return creditRequirementMet; }
    public void setCreditRequirementMet(boolean creditRequirementMet) { this.creditRequirementMet = creditRequirementMet; }

    public BigDecimal getMinimumGpa() { return minimumGpa; }
    public void setMinimumGpa(BigDecimal minimumGpa) { this.minimumGpa = minimumGpa; }

    public BigDecimal getCurrentGpa() { return currentGpa; }
    public void setCurrentGpa(BigDecimal currentGpa) { this.currentGpa = currentGpa; }

    public boolean isGpaRequirementMet() { return gpaRequirementMet; }
    public void setGpaRequirementMet(boolean gpaRequirementMet) { this.gpaRequirementMet = gpaRequirementMet; }

    public boolean isThesisRequired() { return thesisRequired; }
    public void setThesisRequired(boolean thesisRequired) { this.thesisRequired = thesisRequired; }

    public boolean isThesisCompleted() { return thesisCompleted; }
    public void setThesisCompleted(boolean thesisCompleted) { this.thesisCompleted = thesisCompleted; }

    public boolean isThesisRequirementMet() { return thesisRequirementMet; }
    public void setThesisRequirementMet(boolean thesisRequirementMet) { this.thesisRequirementMet = thesisRequirementMet; }

    public List<String> getMissingRequirements() { return missingRequirements; }
    public void setMissingRequirements(List<String> missingRequirements) { this.missingRequirements = missingRequirements; }

    public String getNextSteps() { return nextSteps; }
    public void setNextSteps(String nextSteps) { this.nextSteps = nextSteps; }
}