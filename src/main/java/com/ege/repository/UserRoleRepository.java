package com.ege.repository;

import com.ege.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    // Role name ile arama
    Optional<UserRole> findByRoleName(String roleName);

    // Aktif roller
    List<UserRole> findByIsActiveTrue();

    // Role name'in var olup olmadığını kontrol et
    boolean existsByRoleName(String roleName);

    // Role name ile arama (case insensitive)
    @Query("SELECT ur FROM UserRole ur WHERE LOWER(ur.roleName) = LOWER(?1)")
    Optional<UserRole> findByRoleNameIgnoreCase(String roleName);

    // Aktif rolleri role name'e göre sırala
    @Query("SELECT ur FROM UserRole ur WHERE ur.isActive = true ORDER BY ur.roleName")
    List<UserRole> findActiveRolesOrderByName();
}