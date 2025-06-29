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

    /// LAZY loading sorunu çözmek için JOIN FETCH kullanın
    @Query("SELECT u FROM User u JOIN FETCH u.userRole WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);


    // Employee ID ile kullanıcı bul
    Optional<User> findByEmployeeId(String employeeId);

    // Student ID ile kullanıcı bul
    Optional<User> findByStudentId(String studentId);

    // Aktif kullanıcıları getir
    List<User> findByIsActiveTrue();

    // Role'e göre kullanıcıları getir
    List<User> findByUserRole(UserRole userRole);

    // Case insensitive version (eğer gerekirse)
    @Query("SELECT u FROM User u JOIN FETCH u.userRole WHERE LOWER(u.username) = LOWER(:username)")
    Optional<User> findByUsernameIgnoreCase(@Param("username") String username);


    // Username veya email ile arama
    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :username")
    Optional<User> findByUsernameOrEmail(@Param("username") String username);

    // ID ile arama (JWT token doğrulama için)
    @Query("SELECT u FROM User u JOIN FETCH u.userRole WHERE u.id = :id")
    Optional<User> findById(@Param("id") Long id);

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

    // Email ile arama
    @Query("SELECT u FROM User u JOIN FETCH u.userRole WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    // Aktif kullanıcıları ada göre sırala
    @Query("SELECT u FROM User u WHERE u.isActive = true ORDER BY u.firstName, u.lastName")
    List<User> findActiveUsersOrderByName();

    // Role'e göre kullanıcı sayısını getir
    @Query("SELECT u.userRole.roleName, COUNT(u) FROM User u WHERE u.isActive = true GROUP BY u.userRole.roleName")
    List<Object[]> getUserCountByRole();
}
