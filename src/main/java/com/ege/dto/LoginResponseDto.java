// 7. LoginResponseDto - Giriş yanıtı için
package com.ege.dto;

public class LoginResponseDto {
    private boolean success;
    private String message;
    private UserDto user;
    private String token; // JWT token için

    // Constructors
    public LoginResponseDto() {}

    public LoginResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public LoginResponseDto(boolean success, String message, UserDto user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public UserDto getUser() { return user; }
    public void setUser(UserDto user) { this.user = user; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
