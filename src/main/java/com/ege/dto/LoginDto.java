// 6. LoginDto - Giriş için
package com.ege.dto;

public class LoginDto {
    private String username;
    private String password;

    // Constructors
    public LoginDto() {}

    public LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}