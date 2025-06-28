// 1. StudentProfileDto - Öğrenci profil bilgileri için
package com.ege.dto;

import com.ege.entities.enums.ProgramType;
import java.math.BigDecimal;
import java.time.LocalDate;

public class StudentProfileDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String studentId;
    private String programName;
    private ProgramType programType;
    private LocalDate enrollmentDate;
    private LocalDate expectedGraduationDate;
    private BigDecimal gpa;
    private Integer totalCredits;
    private Integer completedCourses;
    private Boolean thesisCompleted;
    private Boolean graduated;
    private AdvisorDto advisor;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getProgramName() { return programName; }
    public void setProgramName(String programName) { this.programName = programName; }

    public ProgramType getProgramType() { return programType; }
    public void setProgramType(ProgramType programType) { this.programType = programType; }

    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public LocalDate getExpectedGraduationDate() { return expectedGraduationDate; }
    public void setExpectedGraduationDate(LocalDate expectedGraduationDate) { this.expectedGraduationDate = expectedGraduationDate; }

    public BigDecimal getGpa() { return gpa; }
    public void setGpa(BigDecimal gpa) { this.gpa = gpa; }

    public Integer getTotalCredits() { return totalCredits; }
    public void setTotalCredits(Integer totalCredits) { this.totalCredits = totalCredits; }

    public Integer getCompletedCourses() { return completedCourses; }
    public void setCompletedCourses(Integer completedCourses) { this.completedCourses = completedCourses; }

    public Boolean getThesisCompleted() { return thesisCompleted; }
    public void setThesisCompleted(Boolean thesisCompleted) { this.thesisCompleted = thesisCompleted; }

    public Boolean getGraduated() { return graduated; }
    public void setGraduated(Boolean graduated) { this.graduated = graduated; }

    public AdvisorDto getAdvisor() { return advisor; }
    public void setAdvisor(AdvisorDto advisor) { this.advisor = advisor; }
}
