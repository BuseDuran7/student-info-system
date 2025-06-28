// 3. AdvisorDto - Danışman bilgileri için
package com.ege.dto;

import com.ege.entities.enums.Title;

public class AdvisorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Title title;
    private String department;
    private String officeRoom;
    private String email;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Title getTitle() { return title; }
    public void setTitle(Title title) { this.title = title; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getOfficeRoom() { return officeRoom; }
    public void setOfficeRoom(String officeRoom) { this.officeRoom = officeRoom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}