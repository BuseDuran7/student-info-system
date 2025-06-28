package com.ege.service;

import com.ege.dto.UserRoleDto;
import com.ege.entities.UserRole;
import com.ege.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    // Tüm rolleri getir
    public List<UserRoleDto> getAllRoles() {
        return userRoleRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Aktif rolleri getir
    public List<UserRoleDto> getActiveRoles() {
        return userRoleRepository.findActiveRolesOrderByName().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ID ile rol getir
    public Optional<UserRoleDto> getRoleById(Integer id) {
        return userRoleRepository.findById(id)
                .map(this::convertToDto);
    }

    // Role name ile rol getir
    public Optional<UserRoleDto> getRoleByName(String roleName) {
        return userRoleRepository.findByRoleNameIgnoreCase(roleName)
                .map(this::convertToDto);
    }

    // Yeni rol oluştur
    public UserRoleDto createRole(UserRoleDto roleDto) {
        // Role name kontrolü
        if (userRoleRepository.existsByRoleName(roleDto.getRoleName())) {
            throw new RuntimeException("Role name already exists: " + roleDto.getRoleName());
        }

        UserRole userRole = convertToEntity(roleDto);
        userRole.setCreatedAt(Instant.now());
        userRole.setIsActive(true);

        UserRole savedRole = userRoleRepository.save(userRole);
        return convertToDto(savedRole);
    }

    // Rol güncelle
    public UserRoleDto updateRole(Integer id, UserRoleDto roleDto) {
        UserRole existingRole = userRoleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        // Eğer role name değişiyorsa, unique kontrolü yap
        if (!existingRole.getRoleName().equals(roleDto.getRoleName())) {
            if (userRoleRepository.existsByRoleName(roleDto.getRoleName())) {
                throw new RuntimeException("Role name already exists: " + roleDto.getRoleName());
            }
        }

        existingRole.setRoleName(roleDto.getRoleName());
        existingRole.setRoleDescription(roleDto.getRoleDescription());
        if (roleDto.getIsActive() != null) {
            existingRole.setIsActive(roleDto.getIsActive());
        }

        UserRole updatedRole = userRoleRepository.save(existingRole);
        return convertToDto(updatedRole);
    }

    // Rol sil (soft delete)
    public void deleteRole(Integer id) {
        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        userRole.setIsActive(false);
        userRoleRepository.save(userRole);
    }

    // Rol aktifleştir
    public UserRoleDto activateRole(Integer id) {
        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        userRole.setIsActive(true);
        UserRole updatedRole = userRoleRepository.save(userRole);
        return convertToDto(updatedRole);
    }

    // Entity'yi DTO'ya çevir
    private UserRoleDto convertToDto(UserRole userRole) {
        UserRoleDto dto = new UserRoleDto();
        dto.setId(userRole.getId());
        dto.setRoleName(userRole.getRoleName());
        dto.setRoleDescription(userRole.getRoleDescription());
        dto.setIsActive(userRole.getIsActive());
        dto.setCreatedAt(userRole.getCreatedAt());
        return dto;
    }

    // DTO'yu Entity'ye çevir
    private UserRole convertToEntity(UserRoleDto dto) {
        UserRole userRole = new UserRole();
        userRole.setRoleName(dto.getRoleName());
        userRole.setRoleDescription(dto.getRoleDescription());
        return userRole;
    }
}