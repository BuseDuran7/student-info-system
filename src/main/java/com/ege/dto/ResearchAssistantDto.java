// 3. ResearchAssistantDto - Araştırma görevlisi bilgileri için
package com.ege.dto;

import com.ege.entities.enums.ProgramType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ResearchAssistantDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String employeeId;
    private String department;
    private LocalDate hireDate;
    private BigDecimal salary;
    private StudentProfileSummary studentProfile; // Tip değiştirildi
    private List<CourseDto> assistedCourses;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }

    public StudentProfileSummary getStudentProfile() { return studentProfile; }
    public void setStudentProfile(StudentProfileSummary studentProfile) { this.studentProfile = studentProfile; }

    public List<CourseDto> getAssistedCourses() { return assistedCourses; }
    public void setAssistedCourses(List<CourseDto> assistedCourses) { this.assistedCourses = assistedCourses; }

    // StudentProfileSummary nested class - ResearchAssistantDto içine taşındı
    public static class StudentProfileSummary {
        private Long id;
        private String studentId;
        private String programName;
        private ProgramType programType;
        private BigDecimal gpa;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }

        public String getProgramName() { return programName; }
        public void setProgramName(String programName) { this.programName = programName; }

        public ProgramType getProgramType() { return programType; }
        public void setProgramType(ProgramType programType) { this.programType = programType; }

        public BigDecimal getGpa() { return gpa; }
        public void setGpa(BigDecimal gpa) { this.gpa = gpa; }
    }
}