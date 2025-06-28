package com.ege.service;

import com.ege.dto.ResearchAssistantDto;
import com.ege.entities.ResearchAssistant;
import com.ege.entities.User;
import com.ege.entities.Student;
import com.ege.repository.ResearchAssistantRepository;
import com.ege.repository.UserRepository;
import com.ege.repository.StudentRepository;
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
public class ResearchAssistantService {

    @Autowired
    private ResearchAssistantRepository researchAssistantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    // Tüm araştırma görevlilerini getir
    public List<ResearchAssistantDto> getAllResearchAssistants() {
        return researchAssistantRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Aktif araştırma görevlilerini getir
    public List<ResearchAssistantDto> getActiveResearchAssistants() {
        return researchAssistantRepository.findActiveResearchAssistantsOrderByName().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ID ile araştırma görevlisi getir
    public Optional<ResearchAssistantDto> getResearchAssistantById(Long id) {
        return researchAssistantRepository.findById(id)
                .map(this::convertToDto);
    }

    // User ID ile araştırma görevlisi getir
    public Optional<ResearchAssistantDto> getResearchAssistantByUserId(Long userId) {
        return researchAssistantRepository.findByUserId(userId)
                .map(this::convertToDto);
    }

    // Username ile araştırma görevlisi getir
    public Optional<ResearchAssistantDto> getResearchAssistantByUsername(String username) {
        return researchAssistantRepository.findByUsername(username)
                .map(this::convertToDto);
    }

    // Employee ID ile araştırma görevlisi getir
    public Optional<ResearchAssistantDto> getResearchAssistantByEmployeeId(String employeeId) {
        return researchAssistantRepository.findByEmployeeId(employeeId)
                .map(this::convertToDto);
    }

    // Student ID ile araştırma görevlisi getir (dual role)
    public Optional<ResearchAssistantDto> getResearchAssistantByStudentId(Long studentId) {
        return researchAssistantRepository.findByStudentId(studentId)
                .map(this::convertToDto);
    }

    // Departmana göre araştırma görevlilerini getir
    public List<ResearchAssistantDto> getResearchAssistantsByDepartment(String department) {
        return researchAssistantRepository.findActiveByDepartment(department).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Dual role araştırma görevlilerini getir
    public List<ResearchAssistantDto> getStudentResearchAssistants() {
        return researchAssistantRepository.findStudentResearchAssistants().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Sadece çalışan araştırma görevlilerini getir
    public List<ResearchAssistantDto> getNonStudentResearchAssistants() {
        return researchAssistantRepository.findNonStudentResearchAssistants().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // İsimde arama yap
    public List<ResearchAssistantDto> searchResearchAssistantsByName(String name) {
        return researchAssistantRepository.findByFullNameContainingIgnoreCase(name).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Maaş aralığında araştırma görevlilerini getir
    public List<ResearchAssistantDto> getResearchAssistantsBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary) {
        return researchAssistantRepository.findBySalaryBetween(minSalary, maxSalary).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Kıdem sırasına göre araştırma görevlilerini getir
    public List<ResearchAssistantDto> getResearchAssistantsBySeniority() {
        return researchAssistantRepository.findActiveResearchAssistantsOrderBySeniority().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Maaş sırasına göre araştırma görevlilerini getir
    public List<ResearchAssistantDto> getResearchAssistantsBySalary() {
        return researchAssistantRepository.findActiveResearchAssistantsOrderBySalary().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Yeni araştırma görevlisi oluştur
    public ResearchAssistantDto createResearchAssistant(ResearchAssistantDto researchAssistantDto) {
        // Validasyon
        validateResearchAssistantDto(researchAssistantDto);

        // User kontrolü
        User user = userRepository.findById(researchAssistantDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + researchAssistantDto.getId()));

        // Bu user zaten araştırma görevlisi mi?
        if (researchAssistantRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("Research Assistant already exists for this user");
        }

        // User'ın rolü RESEARCH_ASSISTANT mi kontrol et
        if (!"RESEARCH_ASSISTANT".equals(user.getUserRole().getRoleName())) {
            throw new RuntimeException("User role must be RESEARCH_ASSISTANT");
        }

        ResearchAssistant researchAssistant = convertToEntity(researchAssistantDto);
        researchAssistant.setUser(user);

        // Student kontrolü (dual role için)
        if (researchAssistantDto.getStudentProfile() != null &&
                researchAssistantDto.getStudentProfile().getId() != null) {
            Student student = studentRepository.findById(researchAssistantDto.getStudentProfile().getId())
                    .orElseThrow(() -> new RuntimeException("Student not found for dual role"));

            // Student'ın User'ı ile RA'nın User'ı aynı mı kontrol et
            if (!student.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Student and Research Assistant must belong to the same user for dual role");
            }

            researchAssistant.setStudent(student);
        }

        ResearchAssistant savedResearchAssistant = researchAssistantRepository.save(researchAssistant);
        return convertToDto(savedResearchAssistant);
    }

    // Araştırma görevlisi güncelle
    public ResearchAssistantDto updateResearchAssistant(Long id, ResearchAssistantDto researchAssistantDto) {
        ResearchAssistant existingResearchAssistant = researchAssistantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Research Assistant not found with id: " + id));

        // Validasyon
        validateResearchAssistantDto(researchAssistantDto);

        updateResearchAssistantFields(existingResearchAssistant, researchAssistantDto);

        ResearchAssistant updatedResearchAssistant = researchAssistantRepository.save(existingResearchAssistant);
        return convertToDto(updatedResearchAssistant);
    }

    // Student rolü ata (dual role oluştur)
    public ResearchAssistantDto assignStudentRole(Long researchAssistantId, Long studentId) {
        ResearchAssistant researchAssistant = researchAssistantRepository.findById(researchAssistantId)
                .orElseThrow(() -> new RuntimeException("Research Assistant not found with id: " + researchAssistantId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        // Same user kontrolü
        if (!student.getUser().getId().equals(researchAssistant.getUser().getId())) {
            throw new RuntimeException("Student and Research Assistant must belong to the same user");
        }

        researchAssistant.setStudent(student);
        ResearchAssistant updatedResearchAssistant = researchAssistantRepository.save(researchAssistant);
        return convertToDto(updatedResearchAssistant);
    }

    // Student rolünü kaldır (dual role'u sonlandır)
    public ResearchAssistantDto removeStudentRole(Long researchAssistantId) {
        ResearchAssistant researchAssistant = researchAssistantRepository.findById(researchAssistantId)
                .orElseThrow(() -> new RuntimeException("Research Assistant not found with id: " + researchAssistantId));

        researchAssistant.setStudent(null);
        ResearchAssistant updatedResearchAssistant = researchAssistantRepository.save(researchAssistant);
        return convertToDto(updatedResearchAssistant);
    }

    // Araştırma görevlisi sil
    public void deleteResearchAssistant(Long id) {
        ResearchAssistant researchAssistant = researchAssistantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Research Assistant not found with id: " + id));

        // User'ı da pasif yap
        researchAssistant.getUser().setIsActive(false);
        userRepository.save(researchAssistant.getUser());

        // ResearchAssistant kaydını sil
        researchAssistantRepository.delete(researchAssistant);
    }

    // Araştırma görevlisi aktifleştir
    public ResearchAssistantDto activateResearchAssistant(Long id) {
        ResearchAssistant researchAssistant = researchAssistantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Research Assistant not found with id: " + id));

        // User'ı da aktif yap
        researchAssistant.getUser().setIsActive(true);
        userRepository.save(researchAssistant.getUser());

        return convertToDto(researchAssistant);
    }

    // İstatistikler
    public List<Object[]> getResearchAssistantStatisticsByDepartment() {
        return researchAssistantRepository.getResearchAssistantCountByDepartment();
    }

    public List<Object[]> getDualRoleAnalysisByDepartment() {
        return researchAssistantRepository.getDualRoleAnalysisByDepartment();
    }

    public List<Object[]> getSalaryStatistics() {
        return researchAssistantRepository.getSalaryStatistics();
    }

    public List<Object[]> getAverageSalaryByDepartment() {
        return researchAssistantRepository.getAverageSalaryByDepartment();
    }

    public List<Object[]> getHiringCountByYear() {
        return researchAssistantRepository.getHiringCountByYear();
    }

    public List<Object[]> getSeniorityDistribution() {
        return researchAssistantRepository.getSeniorityDistribution();
    }

    public List<Object[]> getDualRoleCountByProgramType() {
        return researchAssistantRepository.getDualRoleCountByProgramType();
    }

    // Araştırma görevlisi DTO validasyonu
    private void validateResearchAssistantDto(ResearchAssistantDto dto) {
        if (dto.getDepartment() == null || dto.getDepartment().trim().isEmpty()) {
            throw new RuntimeException("Department is required");
        }
        if (dto.getSalary() != null && dto.getSalary().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Salary cannot be negative");
        }
        if (dto.getHireDate() != null && dto.getHireDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Hire date cannot be in the future");
        }
    }

    // Mevcut araştırma görevlisi alanlarını güncelle
    private void updateResearchAssistantFields(ResearchAssistant researchAssistant, ResearchAssistantDto dto) {
        researchAssistant.setDepartment(dto.getDepartment());
        researchAssistant.setHireDate(dto.getHireDate());
        researchAssistant.setSalary(dto.getSalary());

        // Student rolü güncelleme (dual role)
        if (dto.getStudentProfile() != null && dto.getStudentProfile().getId() != null) {
            Student student = studentRepository.findById(dto.getStudentProfile().getId())
                    .orElse(null);
            researchAssistant.setStudent(student);
        } else {
            researchAssistant.setStudent(null);
        }
    }

    // Entity'yi DTO'ya çevir
    private ResearchAssistantDto convertToDto(ResearchAssistant researchAssistant) {
        ResearchAssistantDto dto = new ResearchAssistantDto();
        dto.setId(researchAssistant.getId());
        dto.setFirstName(researchAssistant.getUser().getFirstName());
        dto.setLastName(researchAssistant.getUser().getLastName());
        dto.setEmail(researchAssistant.getUser().getEmail());
        dto.setEmployeeId(researchAssistant.getUser().getEmployeeId());
        dto.setDepartment(researchAssistant.getDepartment());
        dto.setHireDate(researchAssistant.getHireDate());
        dto.setSalary(researchAssistant.getSalary());

        // Student profile (dual role)
        if (researchAssistant.getStudent() != null) {
            // StudentProfileDto'yu import etmek yerine basit bir dto oluşturabiliriz
            // Veya StudentService'i inject edip convertToDto metodunu kullanabiliriz
            // Şimdilik basit bir yapı oluşturalım
            dto.setStudentProfile(createStudentProfileSummary(researchAssistant.getStudent()));
        }

        // Assisted courses (ileride CourseAssistant entity'si hazır olunca eklenecek)
        // dto.setAssistedCourses(new ArrayList<>());

        return dto;
    }

    // Student profile summary oluştur
    private ResearchAssistantDto.StudentProfileSummary createStudentProfileSummary(Student student) {
        ResearchAssistantDto.StudentProfileSummary summary = new ResearchAssistantDto.StudentProfileSummary();
        summary.setId(student.getId());
        summary.setStudentId(student.getUser().getStudentId());
        summary.setProgramName(student.getProgram().getName());
        summary.setProgramType(student.getProgram().getType());
        summary.setGpa(student.getGpa());
        return summary;
    }

    // DTO'yu Entity'ye çevir
    private ResearchAssistant convertToEntity(ResearchAssistantDto dto) {
        ResearchAssistant researchAssistant = new ResearchAssistant();
        researchAssistant.setDepartment(dto.getDepartment());
        researchAssistant.setHireDate(dto.getHireDate());
        researchAssistant.setSalary(dto.getSalary());
        return researchAssistant;
    }
}
