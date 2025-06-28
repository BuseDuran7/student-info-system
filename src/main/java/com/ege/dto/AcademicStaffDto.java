// 2. AcademicStaffDto - Öğretim üyesi bilgileri için
package com.ege.dto;

import com.ege.entities.enums.Title;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AcademicStaffDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String employeeId;
    private Title title;
    private String department;
    private String officeRoom;
    private LocalDate hireDate;
    private BigDecimal salary;
    private Integer seniorityYears;
    private List<CourseDto> courses;
    private List<StudentProfileDto> advisedStudents;
    private List<ThesisDto> supervisedTheses;

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

    public Title getTitle() { return title; }
    public void setTitle(Title title) { this.title = title; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getOfficeRoom() { return officeRoom; }
    public void setOfficeRoom(String officeRoom) { this.officeRoom = officeRoom; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }

    public Integer getSeniorityYears() { return seniorityYears; }
    public void setSeniorityYears(Integer seniorityYears) { this.seniorityYears = seniorityYears; }

    public List<CourseDto> getCourses() { return courses; }
    public void setCourses(List<CourseDto> courses) { this.courses = courses; }

    public List<StudentProfileDto> getAdvisedStudents() { return advisedStudents; }
    public void setAdvisedStudents(List<StudentProfileDto> advisedStudents) { this.advisedStudents = advisedStudents; }

    public List<ThesisDto> getSupervisedTheses() { return supervisedTheses; }
    public void setSupervisedTheses(List<ThesisDto> supervisedTheses) { this.supervisedTheses = supervisedTheses; }
}