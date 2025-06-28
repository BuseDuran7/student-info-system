// 3. ResearchAssistantDto - Araştırma görevlisi bilgileri için
package com.ege.dto;

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
    private StudentProfileDto studentProfile; // Eğer öğrenci ise
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

    public StudentProfileDto getStudentProfile() { return studentProfile; }
    public void setStudentProfile(StudentProfileDto studentProfile) { this.studentProfile = studentProfile; }

    public List<CourseDto> getAssistedCourses() { return assistedCourses; }
    public void setAssistedCourses(List<CourseDto> assistedCourses) { this.assistedCourses = assistedCourses; }
}