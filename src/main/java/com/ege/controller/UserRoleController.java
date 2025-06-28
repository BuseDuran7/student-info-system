package com.ege.controller;

import com.ege.dto.UserRoleDto;
import com.ege.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-roles")
@CrossOrigin(origins = "*")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    // Tüm rolleri getir
    @GetMapping
    public ResponseEntity<List<UserRoleDto>> getAllRoles() {
        try {
            List<UserRoleDto> roles = userRoleService.getAllRoles();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Aktif rolleri getir
    @GetMapping("/active")
    public ResponseEntity<List<UserRoleDto>> getActiveRoles() {
        try {
            List<UserRoleDto> roles = userRoleService.getActiveRoles();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ID ile rol getir
    @GetMapping("/{id}")
    public ResponseEntity<UserRoleDto> getRoleById(@PathVariable Integer id) {
        try {
            return userRoleService.getRoleById(id)
                    .map(role -> ResponseEntity.ok(role))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Role name ile rol getir
    @GetMapping("/name/{roleName}")
    public ResponseEntity<UserRoleDto> getRoleByName(@PathVariable String roleName) {
        try {
            return userRoleService.getRoleByName(roleName)
                    .map(role -> ResponseEntity.ok(role))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Yeni rol oluştur
    @PostMapping
    public ResponseEntity<UserRoleDto> createRole(@RequestBody UserRoleDto roleDto) {
        try {
            UserRoleDto createdRole = userRoleService.createRole(roleDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Rol güncelle
    @PutMapping("/{id}")
    public ResponseEntity<UserRoleDto> updateRole(@PathVariable Integer id, @RequestBody UserRoleDto roleDto) {
        try {
            UserRoleDto updatedRole = userRoleService.updateRole(id, roleDto);
            return ResponseEntity.ok(updatedRole);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Rol sil (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        try {
            userRoleService.deleteRole(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Rol aktifleştir
    @PatchMapping("/{id}/activate")
    public ResponseEntity<UserRoleDto> activateRole(@PathVariable Integer id) {
        try {
            UserRoleDto activatedRole = userRoleService.activateRole(id);
            return ResponseEntity.ok(activatedRole);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}