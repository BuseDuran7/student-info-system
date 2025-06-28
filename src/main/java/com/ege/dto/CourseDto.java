// 1. CourseDto - Ders bilgileri için
package com.ege.dto;

import com.ege.entities.enums.CourseLevel;
import java.util.List;

public class CourseDto {
    private Long id;
    private String code;
    private String name;
    private Integer credits;
    private CourseLevel level;
    private String semester;
    private Integer year;
    private Integer maxCapacity;
    private Integer currentEnrollment;
    private String instructorName;
    private Long instructorId;
    private List<String> assistantNames;
    private Boolean isActive;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public CourseLevel getLevel() { return level; }
    public void setLevel(CourseLevel level) { this.level = level; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Integer getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }

    public Integer getCurrentEnrollment() { return currentEnrollment; }
    public void setCurrentEnrollment(Integer currentEnrollment) { this.currentEnrollment = currentEnrollment; }

    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }

    public Long getInstructorId() { return instructorId; }
    public void setInstructorId(Long instructorId) { this.instructorId = instructorId; }

    public List<String> getAssistantNames() { return assistantNames; }
    public void setAssistantNames(List<String> assistantNames) { this.assistantNames = assistantNames; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}