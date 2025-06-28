package com.ege.service;

import com.ege.dto.AcademicStaffDto;
import com.ege.entities.AcademicStaff;
import com.ege.entities.User;
import com.ege.entities.enums.Title;
import com.ege.repository.AcademicStaffRepository;
import com.ege.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AcademicStaffService {

    @Autowired
    private AcademicStaffRepository academicStaffRepository;

    @Autowired
    private UserRepository userRepository;

    // Tüm akademik personeli getir
    public List<AcademicStaffDto> getAllAcademicStaff() {
        return academicStaffRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Aktif akademik personeli getir
    public List<AcademicStaffDto> getActiveAcademicStaff() {
        return academicStaffRepository.findActiveOrderByName().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ID ile akademik personel getir
    public Optional<AcademicStaffDto> getAcademicStaffById(Long id) {
        return academicStaffRepository.findById(id)
                .map(this::convertToDto);
    }

    // User ID ile akademik personel getir
    public Optional<AcademicStaffDto> getAcademicStaffByUserId(Long userId) {
        return academicStaffRepository.findByUserId(userId)
                .map(this::convertToDto);
    }

    // Username ile akademik personel getir
    public Optional<AcademicStaffDto> getAcademicStaffByUsername(String username) {
        return academicStaffRepository.findByUsername(username)
                .map(this::convertToDto);
    }

    // Ünvana göre akademik personel getir
    public List<AcademicStaffDto> getAcademicStaffByTitle(Title title) {
        return academicStaffRepository.findActiveByTitle(title).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Bölüme göre akademik personel getir
    public List<AcademicStaffDto> getAcademicStaffByDepartment(String department) {
        return academicStaffRepository.findActiveByDepartment(department).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // İsimde arama yap
    public List<AcademicStaffDto> searchAcademicStaffByName(String name) {
        return academicStaffRepository.findByFullNameContainingIgnoreCase(name).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Kıdeme göre akademik personel getir
    public List<AcademicStaffDto> getAcademicStaffBySeniority(Integer minYears) {
        return academicStaffRepository.findBySeniorityYearsGreaterThanEqual(minYears).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Yeni akademik personel oluştur
    public AcademicStaffDto createAcademicStaff(AcademicStaffDto academicStaffDto) {
        // Validasyon
        validateAcademicStaffDto(academicStaffDto);

        // User kontrolü
        User user = userRepository.findById(academicStaffDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + academicStaffDto.getId()));

        // Bu user zaten akademik personel mi?
        if (academicStaffRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("Academic staff already exists for this user");
        }

        // User'ın rolü ACADEMIC_STAFF mi kontrol et
        if (!"ACADEMIC_STAFF".equals(user.getUserRole().getRoleName())) {
            throw new RuntimeException("User role must be ACADEMIC_STAFF");
        }

        AcademicStaff academicStaff = convertToEntity(academicStaffDto);
        academicStaff.setUser(user);

        // Eğer kıdem yılı belirtilmemişse, işe alınma tarihinden hesapla
        if (academicStaff.getSeniorityYears() == null && academicStaff.getHireDate() != null) {
            int seniorityYears = LocalDate.now().getYear() - academicStaff.getHireDate().getYear();
            academicStaff.setSeniorityYears(Math.max(0, seniorityYears));
        }

        AcademicStaff savedAcademicStaff = academicStaffRepository.save(academicStaff);
        return convertToDto(savedAcademicStaff);
    }

    // Akademik personel güncelle
    public AcademicStaffDto updateAcademicStaff(Long id, AcademicStaffDto academicStaffDto) {
        AcademicStaff existingAcademicStaff = academicStaffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Academic staff not found with id: " + id));

        // Validasyon
        validateAcademicStaffDto(academicStaffDto);

        updateAcademicStaffFields(existingAcademicStaff, academicStaffDto);

        AcademicStaff updatedAcademicStaff = academicStaffRepository.save(existingAcademicStaff);
        return convertToDto(updatedAcademicStaff);
    }

    // Akademik personel sil
    public void deleteAcademicStaff(Long id) {
        AcademicStaff academicStaff = academicStaffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Academic staff not found with id: " + id));

        // User'ı da pasif yap
        academicStaff.getUser().setIsActive(false);
        userRepository.save(academicStaff.getUser());

        // Academic staff kaydını sil
        academicStaffRepository.delete(academicStaff);
    }

    // Akademik personel aktifleştir
    public AcademicStaffDto activateAcademicStaff(Long id) {
        AcademicStaff academicStaff = academicStaffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Academic staff not found with id: " + id));

        // User'ı da aktif yap
        academicStaff.getUser().setIsActive(true);
        userRepository.save(academicStaff.getUser());

        return convertToDto(academicStaff);
    }

    // Bölüm bazında istatistik
    public List<Object[]> getAcademicStaffStatisticsByDepartment() {
        return academicStaffRepository.getAcademicStaffCountByDepartment();
    }

    // Ünvan bazında istatistik
    public List<Object[]> getAcademicStaffStatisticsByTitle() {
        return academicStaffRepository.getAcademicStaffCountByTitle();
    }

    // Akademik personel DTO validasyonu
    private void validateAcademicStaffDto(AcademicStaffDto dto) {
        if (dto.getTitle() == null) {
            throw new RuntimeException("Title is required");
        }
        if (dto.getDepartment() == null || dto.getDepartment().trim().isEmpty()) {
            throw new RuntimeException("Department is required");
        }
        if (dto.getSalary() != null && dto.getSalary().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Salary cannot be negative");
        }
        if (dto.getSeniorityYears() != null && dto.getSeniorityYears() < 0) {
            throw new RuntimeException("Seniority years cannot be negative");
        }
    }

    // Mevcut akademik personel alanlarını güncelle
    private void updateAcademicStaffFields(AcademicStaff academicStaff, AcademicStaffDto dto) {
        academicStaff.setTitle(dto.getTitle());
        academicStaff.setDepartment(dto.getDepartment());
        academicStaff.setOfficeRoom(dto.getOfficeRoom());
        academicStaff.setHireDate(dto.getHireDate());
        academicStaff.setSalary(dto.getSalary());
        academicStaff.setSeniorityYears(dto.getSeniorityYears());
    }

    // Entity'yi DTO'ya çevir
    private AcademicStaffDto convertToDto(AcademicStaff academicStaff) {
        AcademicStaffDto dto = new AcademicStaffDto();
        dto.setId(academicStaff.getId());
        dto.setFirstName(academicStaff.getUser().getFirstName());
        dto.setLastName(academicStaff.getUser().getLastName());
        dto.setEmail(academicStaff.getUser().getEmail());
        dto.setEmployeeId(academicStaff.getUser().getEmployeeId());
        dto.setTitle(academicStaff.getTitle());
        dto.setDepartment(academicStaff.getDepartment());
        dto.setOfficeRoom(academicStaff.getOfficeRoom());
        dto.setHireDate(academicStaff.getHireDate());
        dto.setSalary(academicStaff.getSalary());
        dto.setSeniorityYears(academicStaff.getSeniorityYears());

        // İleride course listesi ve advised students eklenecek
        // dto.setCourses(new ArrayList<>());
        // dto.setAdvisedStudents(new ArrayList<>());
        // dto.setSupervisedTheses(new ArrayList<>());

        return dto;
    }

    // DTO'yu Entity'ye çevir
    private AcademicStaff convertToEntity(AcademicStaffDto dto) {
        AcademicStaff academicStaff = new AcademicStaff();
        academicStaff.setTitle(dto.getTitle());
        academicStaff.setDepartment(dto.getDepartment());
        academicStaff.setOfficeRoom(dto.getOfficeRoom());
        academicStaff.setHireDate(dto.getHireDate());
        academicStaff.setSalary(dto.getSalary());
        academicStaff.setSeniorityYears(dto.getSeniorityYears());
        return academicStaff;
    }
}