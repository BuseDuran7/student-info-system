package com.ege.entities;

import com.ege.entities.enums.ThesisType;
import com.ege.entities.enums.ThesisStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "theses", indexes = {
        @Index(name = "supervisor_id", columnList = "supervisor_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "unique_student_thesis", columnNames = {"student_id"})
})
public class Thesis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Size(max = 500)
    @NotNull
    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ThesisType type;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "defense_date")
    private LocalDate defenseDate;

    @ColumnDefault("'DEVAM_EDIYOR'")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ThesisStatus status;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supervisor_id", nullable = false)
    private AcademicStaff supervisor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ThesisType getType() {
        return type;
    }

    public void setType(ThesisType type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDefenseDate() {
        return defenseDate;
    }

    public void setDefenseDate(LocalDate defenseDate) {
        this.defenseDate = defenseDate;
    }

    public ThesisStatus getStatus() {
        return status;
    }

    public void setStatus(ThesisStatus status) {
        this.status = status;
    }

    public AcademicStaff getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(AcademicStaff supervisor) {
        this.supervisor = supervisor;
    }
}