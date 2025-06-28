package com.ege.service;

import com.ege.dto.ThesisDto;
import com.ege.entities.Thesis;
import com.ege.entities.Student;
import com.ege.entities.AcademicStaff;
import com.ege.entities.enums.ThesisType;
import com.ege.entities.enums.ThesisStatus;
import com.ege.entities.enums.ProgramType;
import com.ege.repository.ThesisRepository;
import com.ege.repository.StudentRepository;
import com.ege.repository.AcademicStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ThesisService {

    @Autowired
    private ThesisRepository thesisRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AcademicStaffRepository academicStaffRepository;

    // Tüm tezleri getir
    public List<ThesisDto> getAllTheses() {
        return thesisRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ID ile tez getir
    public Optional<ThesisDto> getThesisById(Long id) {
        return thesisRepository.findById(id)
                .map(this::convertToDto);
    }

    // Student ID ile tez getir
    public Optional<ThesisDto> getThesisByStudentId(Long studentId) {
        return thesisRepository.findByStudentId(studentId)
                .map(this::convertToDto);
    }

    // Student User ID ile tez getir
    public Optional<ThesisDto> getThesisByStudentUserId(Long userId) {
        return thesisRepository.findByStudentUserId(userId)
                .map(this::convertToDto);
    }

    // Student number ile tez getir
    public Optional<ThesisDto> getThesisByStudentNumber(String studentId) {
        return thesisRepository.findByStudentNumber(studentId)
                .map(this::convertToDto);
    }

    // Supervisor ID ile tezleri getir
    public List<ThesisDto> getThesesBySupervisorId(Long supervisorId) {
        return thesisRepository.findBySupervisorId(supervisorId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Supervisor employee ID ile tezleri getir
    public List<ThesisDto> getThesesBySupervisorEmployeeId(String employeeId) {
        return thesisRepository.findBySupervisorEmployeeId(employeeId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Tez tipine göre tezleri getir
    public List<ThesisDto> getThesesByType(ThesisType type) {
        return thesisRepository.findByType(type).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Tez durumuna göre tezleri getir
    public List<ThesisDto> getThesesByStatus(ThesisStatus status) {
        return thesisRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Aktif tezleri getir
    public List<ThesisDto> getActiveTheses() {
        return thesisRepository.findActiveTheses().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Onaylanan (tamamlanan) tezleri getir
    public List<ThesisDto> getCompletedTheses() {
        return thesisRepository.findCompletedTheses().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Teslim edilmiş tezleri getir
    public List<ThesisDto> getSubmittedTheses() {
        return thesisRepository.findByStatus(ThesisStatus.TESLIM_EDILDI).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Savunulmuş tezleri getir
    public List<ThesisDto> getDefendedTheses() {
        return thesisRepository.findByStatus(ThesisStatus.SAVUNULDU).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Departmana göre tezleri getir
    public List<ThesisDto> getThesesByDepartment(String department) {
        return thesisRepository.findByDepartment(department).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Başlangıç tarihi aralığına göre tezleri getir
    public List<ThesisDto> getThesesByStartDateRange(LocalDate startDate, LocalDate endDate) {
        return thesisRepository.findByStartDateBetween(startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Savunma tarihi aralığına göre tezleri getir
    public List<ThesisDto> getThesesByDefenseDateRange(LocalDate startDate, LocalDate endDate) {
        return thesisRepository.findByDefenseDateBetween(startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Başlık ile arama
    public List<ThesisDto> searchThesesByTitle(String title) {
        return thesisRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Öğrenci adıyla arama
    public List<ThesisDto> searchThesesByStudentName(String studentName) {
        return thesisRepository.findByStudentNameContaining(studentName).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Supervisor adıyla arama
    public List<ThesisDto> searchThesesBySupervisorName(String supervisorName) {
        return thesisRepository.findBySupervisorNameContaining(supervisorName).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Uzun süredir devam eden tezleri getir
    public List<ThesisDto> getLongRunningTheses(int monthsThreshold) {
        LocalDate thresholdDate = LocalDate.now().minusMonths(monthsThreshold);
        return thesisRepository.findLongRunningTheses(thresholdDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Yakında savunması olan tezleri getir
    public List<ThesisDto> getUpcomingDefenses(int daysAhead) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(daysAhead);
        return thesisRepository.findUpcomingDefenses(startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Yeni tez oluştur
    public ThesisDto createThesis(ThesisDto thesisDto) {
        // Validasyonlar
        validateThesisDto(thesisDto);

        // Student kontrolü
        Student student = studentRepository.findById(thesisDto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + thesisDto.getStudentId()));

        // Öğrencinin zaten bir tezi var mı kontrol et
        Optional<Thesis> existingThesis = thesisRepository.findByStudent(student);
        if (existingThesis.isPresent()) {
            throw new RuntimeException("Student already has a thesis");
        }

        // Supervisor kontrolü
        AcademicStaff supervisor = academicStaffRepository.findById(thesisDto.getSupervisorId())
                .orElseThrow(() -> new RuntimeException("Supervisor not found with id: " + thesisDto.getSupervisorId()));

        // Supervisor aktif mi kontrol et
        if (!supervisor.getUser().getIsActive()) {
            throw new RuntimeException("Cannot assign inactive supervisor to thesis");
        }

        // Program tipi ile tez tipi uyumluluğu kontrolü
        validateThesisTypeWithProgramType(student, thesisDto.getType());

        Thesis thesis = convertToEntity(thesisDto);
        thesis.setStudent(student);
        thesis.setSupervisor(supervisor);

        // Default değerler
        if (thesis.getStatus() == null) {
            thesis.setStatus(ThesisStatus.DEVAM_EDIYOR);
        }
        if (thesis.getStartDate() == null) {
            thesis.setStartDate(LocalDate.now());
        }

        Thesis savedThesis = thesisRepository.save(thesis);
        return convertToDto(savedThesis);
    }

    // Tez güncelle
    public ThesisDto updateThesis(Long id, ThesisDto thesisDto) {
        Thesis existingThesis = thesisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Thesis not found with id: " + id));

        // Validasyonlar
        validateThesisDto(thesisDto);
        validateThesisUpdate(existingThesis, thesisDto);

        updateThesisFields(existingThesis, thesisDto);

        Thesis updatedThesis = thesisRepository.save(existingThesis);
        return convertToDto(updatedThesis);
    }

    // Supervisor değiştir
    public ThesisDto changeSupervisor(Long thesisId, Long newSupervisorId) {
        Thesis thesis = thesisRepository.findById(thesisId)
                .orElseThrow(() -> new RuntimeException("Thesis not found with id: " + thesisId));

        AcademicStaff newSupervisor = academicStaffRepository.findById(newSupervisorId)
                .orElseThrow(() -> new RuntimeException("Supervisor not found with id: " + newSupervisorId));

        if (!newSupervisor.getUser().getIsActive()) {
            throw new RuntimeException("Cannot assign inactive supervisor");
        }

        thesis.setSupervisor(newSupervisor);
        Thesis updatedThesis = thesisRepository.save(thesis);
        return convertToDto(updatedThesis);
    }

    // Savunma tarihi belirle
    public ThesisDto setDefenseDate(Long thesisId, LocalDate defenseDate) {
        Thesis thesis = thesisRepository.findById(thesisId)
                .orElseThrow(() -> new RuntimeException("Thesis not found with id: " + thesisId));

        // Validasyonlar
        if (defenseDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("Defense date cannot be in the past");
        }
        if (thesis.getStartDate() != null && defenseDate.isBefore(thesis.getStartDate())) {
            throw new RuntimeException("Defense date cannot be before start date");
        }

        thesis.setDefenseDate(defenseDate);
        Thesis updatedThesis = thesisRepository.save(thesis);
        return convertToDto(updatedThesis);
    }

    // Tezi teslim et
    public ThesisDto submitThesis(Long thesisId) {
        Thesis thesis = thesisRepository.findById(thesisId)
                .orElseThrow(() -> new RuntimeException("Thesis not found with id: " + thesisId));

        if (thesis.getStatus() != ThesisStatus.DEVAM_EDIYOR) {
            throw new RuntimeException("Only ongoing theses can be submitted");
        }

        thesis.setStatus(ThesisStatus.TESLIM_EDILDI);
        Thesis updatedThesis = thesisRepository.save(thesis);
        return convertToDto(updatedThesis);
    }

    // Tezi savun
    public ThesisDto defendThesis(Long thesisId, LocalDate defenseDate) {
        Thesis thesis = thesisRepository.findById(thesisId)
                .orElseThrow(() -> new RuntimeException("Thesis not found with id: " + thesisId));

        if (thesis.getStatus() != ThesisStatus.TESLIM_EDILDI) {
            throw new RuntimeException("Only submitted theses can be defended");
        }

        if (defenseDate != null) {
            thesis.setDefenseDate(defenseDate);
        }
        thesis.setStatus(ThesisStatus.SAVUNULDU);

        Thesis updatedThesis = thesisRepository.save(thesis);
        return convertToDto(updatedThesis);
    }

    // Tezi onayla (tamamla)
    public ThesisDto approveThesis(Long thesisId) {
        Thesis thesis = thesisRepository.findById(thesisId)
                .orElseThrow(() -> new RuntimeException("Thesis not found with id: " + thesisId));

        if (thesis.getStatus() != ThesisStatus.SAVUNULDU) {
            throw new RuntimeException("Only defended theses can be approved");
        }

        thesis.setStatus(ThesisStatus.ONAYLANDI);
        Thesis updatedThesis = thesisRepository.save(thesis);
        return convertToDto(updatedThesis);
    }

    // Tezi tamamla (eski metod - artık onaylama için kullanılıyor)
    public ThesisDto completeThesis(Long thesisId, LocalDate defenseDate) {
        Thesis thesis = thesisRepository.findById(thesisId)
                .orElseThrow(() -> new RuntimeException("Thesis not found with id: " + thesisId));

        if (defenseDate != null) {
            thesis.setDefenseDate(defenseDate);
        }
        thesis.setStatus(ThesisStatus.ONAYLANDI);

        Thesis updatedThesis = thesisRepository.save(thesis);
        return convertToDto(updatedThesis);
    }

    // Tez durumunu güncelle
    public ThesisDto updateThesisStatus(Long thesisId, ThesisStatus status) {
        Thesis thesis = thesisRepository.findById(thesisId)
                .orElseThrow(() -> new RuntimeException("Thesis not found with id: " + thesisId));

        thesis.setStatus(status);
        Thesis updatedThesis = thesisRepository.save(thesis);
        return convertToDto(updatedThesis);
    }

    // Tez sil
    public void deleteThesis(Long id) {
        Thesis thesis = thesisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Thesis not found with id: " + id));

        thesisRepository.delete(thesis);
    }

    // İstatistikler
    public List<Object[]> getThesisCountByType() {
        return thesisRepository.getThesisCountByType();
    }

    public List<Object[]> getThesisCountByStatus() {
        return thesisRepository.getThesisCountByStatus();
    }

    public List<Object[]> getThesisCountByDepartment() {
        return thesisRepository.getThesisCountByDepartment();
    }

    public List<Object[]> getThesisCountBySupervisor() {
        return thesisRepository.getThesisCountBySupervisor();
    }

    public List<Object[]> getThesisCountByStartYear() {
        return thesisRepository.getThesisCountByStartYear();
    }

    public List<Object[]> getThesisDefenseCountByYear() {
        return thesisRepository.getThesisDefenseCountByYear();
    }

    public Double getAverageThesisDurationInMonths() {
        return thesisRepository.getAverageThesisDurationInMonths();
    }

    public List<Object[]> getThesisCountByTypeAndStatus() {
        return thesisRepository.getThesisCountByTypeAndStatus();
    }

    public List<Object[]> getThesisAnalysisByDepartmentAndType() {
        return thesisRepository.getThesisAnalysisByDepartmentAndType();
    }

    public List<Object[]> getSupervisorsWithMostTheses(int minTheses) {
        return thesisRepository.getSupervisorsWithMostTheses(minTheses);
    }

    public Long getCompletedThesesCountInPeriod(LocalDate startDate, LocalDate endDate) {
        return thesisRepository.getCompletedThesesCountInPeriod(startDate, endDate);
    }

    public Long getActiveThesesCount() {
        return thesisRepository.getActiveThesesCount();
    }

    // Validasyon metodları
    private void validateThesisDto(ThesisDto dto) {
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new RuntimeException("Thesis title is required");
        }
        if (dto.getTitle().length() > 500) {
            throw new RuntimeException("Thesis title cannot exceed 500 characters");
        }
        if (dto.getType() == null) {
            throw new RuntimeException("Thesis type is required");
        }
        if (dto.getStudentId() == null) {
            throw new RuntimeException("Student ID is required");
        }
        if (dto.getSupervisorId() == null) {
            throw new RuntimeException("Supervisor ID is required");
        }
        if (dto.getStartDate() != null && dto.getDefenseDate() != null) {
            if (dto.getDefenseDate().isBefore(dto.getStartDate())) {
                throw new RuntimeException("Defense date cannot be before start date");
            }
        }
        if (dto.getDefenseDate() != null && dto.getDefenseDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Defense date cannot be in the past");
        }
    }

    private void validateThesisUpdate(Thesis existingThesis, ThesisDto dto) {
        // Onaylanmış tezlerin bazı alanları değiştirilemez
        if (existingThesis.getStatus() == ThesisStatus.ONAYLANDI) {
            if (!existingThesis.getTitle().equals(dto.getTitle())) {
                throw new RuntimeException("Cannot change title of approved thesis");
            }
            if (!existingThesis.getType().equals(dto.getType())) {
                throw new RuntimeException("Cannot change type of approved thesis");
            }
        }
    }

    private void validateThesisTypeWithProgramType(Student student, ThesisType thesisType) {
        ProgramType programType = student.getProgram().getType();

        // Tezli yüksek lisans ve doktora öğrencileri tez yapabilir
        if (programType == ProgramType.TEZSIZ_YL) {
            throw new RuntimeException("Non-thesis master's students cannot have a thesis");
        }

        // Yüksek lisans öğrencisi doktora tezi yapamaz
        if (programType == ProgramType.TEZLI_YL && thesisType == ThesisType.DOKTORA_TEZI) {
            throw new RuntimeException("Master's student cannot have PhD thesis");
        }

        // Doktora öğrencisi yüksek lisans tezi yapamaz
        if (programType == ProgramType.DOKTORA && thesisType == ThesisType.YL_TEZI) {
            throw new RuntimeException("PhD student cannot have master's thesis");
        }
    }

    private void updateThesisFields(Thesis thesis, ThesisDto dto) {
        thesis.setTitle(dto.getTitle());
        thesis.setType(dto.getType());
        thesis.setStartDate(dto.getStartDate());
        thesis.setDefenseDate(dto.getDefenseDate());

        if (dto.getStatus() != null) {
            thesis.setStatus(dto.getStatus());
        }

        // Supervisor değişikliği
        if (!thesis.getSupervisor().getId().equals(dto.getSupervisorId())) {
            AcademicStaff newSupervisor = academicStaffRepository.findById(dto.getSupervisorId())
                    .orElseThrow(() -> new RuntimeException("Supervisor not found"));
            thesis.setSupervisor(newSupervisor);
        }
    }

    // Entity'yi DTO'ya çevir
    private ThesisDto convertToDto(Thesis thesis) {
        ThesisDto dto = new ThesisDto();
        dto.setId(thesis.getId());
        dto.setTitle(thesis.getTitle());
        dto.setType(thesis.getType());
        dto.setStartDate(thesis.getStartDate());
        dto.setDefenseDate(thesis.getDefenseDate());
        dto.setStatus(thesis.getStatus());

        // Student bilgileri
        if (thesis.getStudent() != null) {
            dto.setStudentId(thesis.getStudent().getId());
            dto.setStudentName(thesis.getStudent().getUser().getFirstName() + " " +
                    thesis.getStudent().getUser().getLastName());
        }

        // Supervisor bilgileri
        if (thesis.getSupervisor() != null) {
            dto.setSupervisorId(thesis.getSupervisor().getId());
            dto.setSupervisorName(thesis.getSupervisor().getUser().getFirstName() + " " +
                    thesis.getSupervisor().getUser().getLastName());
        }

        return dto;
    }

    // DTO'yu Entity'ye çevir
    private Thesis convertToEntity(ThesisDto dto) {
        Thesis thesis = new Thesis();
        thesis.setTitle(dto.getTitle());
        thesis.setType(dto.getType());
        thesis.setStartDate(dto.getStartDate());
        thesis.setDefenseDate(dto.getDefenseDate());

        if (dto.getStatus() != null) {
            thesis.setStatus(dto.getStatus());
        }

        return thesis;
    }
}