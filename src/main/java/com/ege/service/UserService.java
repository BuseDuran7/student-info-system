package com.ege.service;

import com.ege.dto.UserDto;
import com.ege.dto.ChangePasswordDto;
import com.ege.entities.User;
import com.ege.entities.UserRole;
import com.ege.repository.UserRepository;
import com.ege.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // Tüm kullanıcıları getir
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Aktif kullanıcıları getir
    public List<UserDto> getActiveUsers() {
        return userRepository.findActiveUsersOrderByName().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ID ile kullanıcı getir
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDto);
    }

    // Username ile kullanıcı getir
    public Optional<UserDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToDto);
    }

    // Email ile kullanıcı getir
    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToDto);
    }

    // Role'e göre kullanıcıları getir
    public List<UserDto> getUsersByRoleName(String roleName) {
        return userRepository.findActiveUsersByRoleName(roleName).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // İsimde arama yap
    public List<UserDto> searchUsersByName(String name) {
        return userRepository.findByFullNameContainingIgnoreCase(name).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Yeni kullanıcı oluştur
    public UserDto createUser(UserDto userDto) {
        // Create için password gerekli
        if (userDto.getPassword() == null || userDto.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password is required for creating user");
        }

        // Validasyon
        validateUserDto(userDto, true); // true = create mode

        // Unique field kontrolü
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("Username already exists: " + userDto.getUsername());
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already exists: " + userDto.getEmail());
        }
        if (userDto.getEmployeeId() != null && userRepository.existsByStudentId(userDto.getEmployeeId())) {
            throw new RuntimeException("Employee ID already exists: " + userDto.getEmployeeId());
        }
        if (userDto.getStudentId() != null && userRepository.existsByStudentId(userDto.getStudentId())) {
            throw new RuntimeException("Student ID already exists: " + userDto.getStudentId());
        }

        // Role kontrolü
        UserRole userRole = userRoleRepository.findByRoleName(userDto.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found: " + userDto.getRoleName()));

        User user = convertToEntity(userDto);

        // ŞİFREYİ HASH'LE - BU SATIRLARI EKLEYİN
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        user.setUserRole(userRole);
        user.setCreatedAt(Instant.now());
        user.setIsActive(true);

        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    // Kullanıcı güncelle
    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Update için password opsiyonel - eğer gönderilmezse mevcut password korunur
        validateUserDto(userDto, false); // false = update mode

        // Unique field kontrolü (değişenler için)
        if (!existingUser.getUsername().equals(userDto.getUsername()) &&
                userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("Username already exists: " + userDto.getUsername());
        }
        if (!existingUser.getEmail().equals(userDto.getEmail()) &&
                userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already exists: " + userDto.getEmail());
        }

        // Role kontrolü
        UserRole userRole = userRoleRepository.findByRoleName(userDto.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found: " + userDto.getRoleName()));

        updateUserFields(existingUser, userDto, userRole);

        User updatedUser = userRepository.save(existingUser);
        return convertToDto(updatedUser);
    }

    // Şifre değiştir
    public boolean changePassword(Long userId, ChangePasswordDto changePasswordDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Mevcut şifre kontrolü - HASH'LENMİŞ ŞİFRE KONTROLÜ
        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Yeni şifre ve onay kontrolü
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
            throw new RuntimeException("New password and confirm password do not match");
        }

        // Şifre karmaşıklık kontrolü
        if (changePasswordDto.getNewPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters long");
        }

        // YENİ ŞİFREYİ HASH'LE
        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    // Kullanıcı sil (soft delete)
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setIsActive(false);
        userRepository.save(user);
    }

    // Kullanıcı aktifleştir
    public UserDto activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setIsActive(true);
        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    // Role'e göre kullanıcı sayısı istatistiği
    public List<Object[]> getUserCountByRole() {
        return userRepository.getUserCountByRole();
    }

    // Kullanıcı DTO validasyonu
    private void validateUserDto(UserDto dto, boolean isCreate) {
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Username is required");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        if (dto.getFirstName() == null || dto.getFirstName().trim().isEmpty()) {
            throw new RuntimeException("First name is required");
        }
        if (dto.getLastName() == null || dto.getLastName().trim().isEmpty()) {
            throw new RuntimeException("Last name is required");
        }
        if (dto.getRoleName() == null || dto.getRoleName().trim().isEmpty()) {
            throw new RuntimeException("Role name is required");
        }

        // Email format kontrolü
        if (!dto.getEmail().contains("@")) {
            throw new RuntimeException("Invalid email format");
        }

        // Username format kontrolü
        if (dto.getUsername().length() < 3) {
            throw new RuntimeException("Username must be at least 3 characters long");
        }

        // Password kontrolü sadece create için
        if (isCreate && (dto.getPassword() == null || dto.getPassword().length() < 6)) {
            throw new RuntimeException("Password must be at least 6 characters long");
        }
    }

    // Mevcut kullanıcı alanlarını güncelle
    private void updateUserFields(User user, UserDto dto, UserRole userRole) {
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setUserRole(userRole);
        user.setEmployeeId(dto.getEmployeeId());
        user.setStudentId(dto.getStudentId());

        // Password güncelleme (sadece gönderilmişse)
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword())); // HASH'LE
        }

        if (dto.getIsActive() != null) {
            user.setIsActive(dto.getIsActive());
        }
    }

    // Entity'yi DTO'ya çevir
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        // Password set etmiyoruz - WRITE_ONLY olduğu için response'da gözükmeyecek
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setRoleName(user.getUserRole().getRoleName());
        dto.setEmployeeId(user.getEmployeeId());
        dto.setStudentId(user.getStudentId());
        dto.setIsActive(user.getIsActive());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    // DTO'yu Entity'ye çevir
    private User convertToEntity(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // Plain text for now
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setEmployeeId(dto.getEmployeeId());
        user.setStudentId(dto.getStudentId());
        return user;
    }
}