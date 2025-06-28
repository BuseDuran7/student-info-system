package com.ege.repository;

import com.ege.entities.User;
import com.ege.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Username ile kullanıcı bul
    Optional<User> findByUsername(String username);

    // Email ile kullanıcı bul
    Optional<User> findByEmail(String email);

    // Employee ID ile kullanıcı bul
    Optional<User> findByEmployeeId(String employeeId);

    // Student ID ile kullanıcı bul
    Optional<User> findByStudentId(String studentId);

    // Aktif kullanıcıları getir
    List<User> findByIsActiveTrue();

    // Role'e göre kullanıcıları getir
    List<User> findByUserRole(UserRole userRole);

    // Role name'e göre kullanıcıları getir
    @Query("SELECT u FROM User u WHERE u.userRole.roleName = :roleName")
    List<User> findByUserRoleName(@Param("roleName") String roleName);

    // Aktif kullanıcıları role'e göre getir
    @Query("SELECT u FROM User u WHERE u.userRole.roleName = :roleName AND u.isActive = true")
    List<User> findActiveUsersByRoleName(@Param("roleName") String roleName);

    // Username'in var olup olmadığını kontrol et
    boolean existsByUsername(String username);

    // Email'in var olup olmadığını kontrol et
    boolean existsByEmail(String email);

    // Employee ID'nin var olup olmadığını kontrol et
    boolean existsByEmployeeId(String employeeId);

    // Student ID'nin var olup olmadığını kontrol et
    boolean existsByStudentId(String studentId);

    // İsim ve soyisimde arama yap
    @Query("SELECT u FROM User u WHERE LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByFullNameContainingIgnoreCase(@Param("name") String name);

    // Email domain'ine göre arama
    @Query("SELECT u FROM User u WHERE u.email LIKE CONCAT('%', :domain)")
    List<User> findByEmailDomain(@Param("domain") String domain);

    // Aktif kullanıcıları ada göre sırala
    @Query("SELECT u FROM User u WHERE u.isActive = true ORDER BY u.firstName, u.lastName")
    List<User> findActiveUsersOrderByName();

    // Role'e göre kullanıcı sayısını getir
    @Query("SELECT u.userRole.roleName, COUNT(u) FROM User u WHERE u.isActive = true GROUP BY u.userRole.roleName")
    List<Object[]> getUserCountByRole();
}
