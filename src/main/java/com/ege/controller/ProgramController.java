package com.ege.controller;

import com.ege.dto.ProgramDto;
import com.ege.entities.enums.ProgramType;
import com.ege.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/programs")
@CrossOrigin(origins = "*")
public class ProgramController {

    @Autowired
    private ProgramService programService;

    // Tüm programları getir
    @GetMapping
    public ResponseEntity<List<ProgramDto>> getAllPrograms() {
        try {
            List<ProgramDto> programs = programService.getAllPrograms();
            return ResponseEntity.ok(programs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Aktif programları getir
    @GetMapping("/active")
    public ResponseEntity<List<ProgramDto>> getActivePrograms() {
        try {
            List<ProgramDto> programs = programService.getActivePrograms();
            return ResponseEntity.ok(programs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ID ile program getir
    @GetMapping("/{id}")
    public ResponseEntity<ProgramDto> getProgramById(@PathVariable Long id) {
        try {
            return programService.getProgramById(id)
                    .map(program -> ResponseEntity.ok(program))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Program adı ile program getir
    @GetMapping("/name/{name}")
    public ResponseEntity<ProgramDto> getProgramByName(@PathVariable String name) {
        try {
            return programService.getProgramByName(name)
                    .map(program -> ResponseEntity.ok(program))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Program türüne göre programları getir
    @GetMapping("/type/{type}")
    public ResponseEntity<List<ProgramDto>> getProgramsByType(@PathVariable ProgramType type) {
        try {
            List<ProgramDto> programs = programService.getProgramsByType(type);
            return ResponseEntity.ok(programs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Program adında arama yap
    @GetMapping("/search")
    public ResponseEntity<List<ProgramDto>> searchPrograms(@RequestParam String name) {
        try {
            List<ProgramDto> programs = programService.searchProgramsByName(name);
            return ResponseEntity.ok(programs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Tez gerektiren programları getir
    @GetMapping("/thesis-required")
    public ResponseEntity<List<ProgramDto>> getThesisRequiredPrograms() {
        try {
            List<ProgramDto> programs = programService.getThesisRequiredPrograms();
            return ResponseEntity.ok(programs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Yeni program oluştur
    @PostMapping
    public ResponseEntity<ProgramDto> createProgram(@RequestBody ProgramDto programDto) {
        try {
            ProgramDto createdProgram = programService.createProgram(programDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProgram);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Program güncelle
    @PutMapping("/{id}")
    public ResponseEntity<ProgramDto> updateProgram(@PathVariable Long id, @RequestBody ProgramDto programDto) {
        try {
            ProgramDto updatedProgram = programService.updateProgram(id, programDto);
            return ResponseEntity.ok(updatedProgram);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Program sil (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable Long id) {
        try {
            programService.deleteProgram(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Program aktifleştir
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ProgramDto> activateProgram(@PathVariable Long id) {
        try {
            ProgramDto activatedProgram = programService.activateProgram(id);
            return ResponseEntity.ok(activatedProgram);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Program türlerine göre istatistik
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getProgramStatistics() {
        try {
            List<Object[]> stats = programService.getProgramStatistics();
            Map<String, Object> response = new HashMap<>();

            for (Object[] stat : stats) {
                ProgramType type = (ProgramType) stat[0];
                Long count = (Long) stat[1];
                response.put(type.name(), count);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}