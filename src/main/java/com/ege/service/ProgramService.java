package com.ege.service;

import com.ege.dto.ProgramDto;
import com.ege.entities.Program;
import com.ege.entities.enums.ProgramType;
import com.ege.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProgramService {

    @Autowired
    private ProgramRepository programRepository;

    // Tüm programları getir
    public List<ProgramDto> getAllPrograms() {
        return programRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Aktif programları getir
    public List<ProgramDto> getActivePrograms() {
        return programRepository.findActiveProgramsOrderByName().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ID ile program getir
    public Optional<ProgramDto> getProgramById(Long id) {
        return programRepository.findById(id)
                .map(this::convertToDto);
    }

    // Program adı ile program getir
    public Optional<ProgramDto> getProgramByName(String name) {
        return programRepository.findByName(name)
                .map(this::convertToDto);
    }

    // Program türüne göre programları getir
    public List<ProgramDto> getProgramsByType(ProgramType type) {
        return programRepository.findByTypeAndIsActiveTrue(type).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Program adında arama yap
    public List<ProgramDto> searchProgramsByName(String name) {
        return programRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Tez gerektiren programları getir
    public List<ProgramDto> getThesisRequiredPrograms() {
        return programRepository.findByHasThesisTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Yeni program oluştur
    public ProgramDto createProgram(ProgramDto programDto) {
        // Program adı kontrolü
        if (programRepository.existsByName(programDto.getName())) {
            throw new RuntimeException("Program name already exists: " + programDto.getName());
        }

        // Gerekli alanların kontrolü
        validateProgramDto(programDto);

        Program program = convertToEntity(programDto);
        program.setIsActive(true);

        Program savedProgram = programRepository.save(program);
        return convertToDto(savedProgram);
    }

    // Program güncelle
    public ProgramDto updateProgram(Long id, ProgramDto programDto) {
        Program existingProgram = programRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Program not found with id: " + id));

        // Eğer program adı değişiyorsa, unique kontrolü yap
        if (!existingProgram.getName().equals(programDto.getName())) {
            if (programRepository.existsByName(programDto.getName())) {
                throw new RuntimeException("Program name already exists: " + programDto.getName());
            }
        }

        // Gerekli alanların kontrolü
        validateProgramDto(programDto);

        updateProgramFields(existingProgram, programDto);

        Program updatedProgram = programRepository.save(existingProgram);
        return convertToDto(updatedProgram);
    }

    // Program sil (soft delete)
    public void deleteProgram(Long id) {
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Program not found with id: " + id));

        program.setIsActive(false);
        programRepository.save(program);
    }

    // Program aktifleştir
    public ProgramDto activateProgram(Long id) {
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Program not found with id: " + id));

        program.setIsActive(true);
        Program updatedProgram = programRepository.save(program);
        return convertToDto(updatedProgram);
    }

    // Program türlerine göre istatistik
    public List<Object[]> getProgramStatistics() {
        return programRepository.getProgramStatisticsByType();
    }

    // Program DTO doğrulama
    private void validateProgramDto(ProgramDto dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new RuntimeException("Program name is required");
        }
        if (dto.getType() == null) {
            throw new RuntimeException("Program type is required");
        }
        if (dto.getRequiredCourses() == null || dto.getRequiredCourses() <= 0) {
            throw new RuntimeException("Required courses must be greater than 0");
        }
        if (dto.getRequiredCredits() == null || dto.getRequiredCredits() <= 0) {
            throw new RuntimeException("Required credits must be greater than 0");
        }
        if (dto.getMinimumGpa() == null || dto.getMinimumGpa().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Minimum GPA must be 0 or greater");
        }
        if (dto.getMaxDurationYears() == null || dto.getMaxDurationYears() <= 0) {
            throw new RuntimeException("Max duration years must be greater than 0");
        }
    }

    // Mevcut program alanlarını güncelle
    private void updateProgramFields(Program program, ProgramDto dto) {
        program.setName(dto.getName());
        program.setType(dto.getType());
        program.setRequiredCourses(dto.getRequiredCourses());
        program.setRequiredCredits(dto.getRequiredCredits());
        program.setMinimumGpa(dto.getMinimumGpa());
        program.setMaxDurationYears(dto.getMaxDurationYears());
        program.setHasThesis(dto.getHasThesis() != null ? dto.getHasThesis() : false);

        if (dto.getPassingGradeMasters() != null) {
            program.setPassingGradeMasters(dto.getPassingGradeMasters());
        }
        if (dto.getPassingGradePhd() != null) {
            program.setPassingGradePhd(dto.getPassingGradePhd());
        }
        if (dto.getIsActive() != null) {
            program.setIsActive(dto.getIsActive());
        }
    }

    // Entity'yi DTO'ya çevir
    private ProgramDto convertToDto(Program program) {
        ProgramDto dto = new ProgramDto();
        dto.setId(program.getId());
        dto.setName(program.getName());
        dto.setType(program.getType());
        dto.setRequiredCourses(program.getRequiredCourses());
        dto.setRequiredCredits(program.getRequiredCredits());
        dto.setMinimumGpa(program.getMinimumGpa());
        dto.setMaxDurationYears(program.getMaxDurationYears());
        dto.setHasThesis(program.getHasThesis());
        dto.setPassingGradeMasters(program.getPassingGradeMasters());
        dto.setPassingGradePhd(program.getPassingGradePhd());
        dto.setIsActive(program.getIsActive());

        // Öğrenci sayısını hesapla (şimdilik 0, Student entity'si hazır olunca güncellenecek)
        dto.setEnrolledStudentsCount(0);

        return dto;
    }

    // DTO'yu Entity'ye çevir
    private Program convertToEntity(ProgramDto dto) {
        Program program = new Program();
        program.setName(dto.getName());
        program.setType(dto.getType());
        program.setRequiredCourses(dto.getRequiredCourses());
        program.setRequiredCredits(dto.getRequiredCredits());
        program.setMinimumGpa(dto.getMinimumGpa());
        program.setMaxDurationYears(dto.getMaxDurationYears());
        program.setHasThesis(dto.getHasThesis() != null ? dto.getHasThesis() : false);
        program.setPassingGradeMasters(dto.getPassingGradeMasters() != null ? dto.getPassingGradeMasters() : 70);
        program.setPassingGradePhd(dto.getPassingGradePhd() != null ? dto.getPassingGradePhd() : 75);
        return program;
    }
}