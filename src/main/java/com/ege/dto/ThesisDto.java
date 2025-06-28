// 2. ThesisDto - Tez bilgileri için
package com.ege.dto;

import com.ege.entities.enums.ThesisType;
import com.ege.entities.enums.ThesisStatus;
import java.time.LocalDate;

public class ThesisDto {
    private Long id;
    private String title;
    private ThesisType type;
    private LocalDate startDate;
    private LocalDate defenseDate;
    private ThesisStatus status;
    private String supervisorName;
    private Long supervisorId;
    private String studentName;
    private Long studentId;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public ThesisType getType() { return type; }
    public void setType(ThesisType type) { this.type = type; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getDefenseDate() { return defenseDate; }
    public void setDefenseDate(LocalDate defenseDate) { this.defenseDate = defenseDate; }

    public ThesisStatus getStatus() { return status; }
    public void setStatus(ThesisStatus status) { this.status = status; }

    public String getSupervisorName() { return supervisorName; }
    public void setSupervisorName(String supervisorName) { this.supervisorName = supervisorName; }

    public Long getSupervisorId() { return supervisorId; }
    public void setSupervisorId(Long supervisorId) { this.supervisorId = supervisorId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
}