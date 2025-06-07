package com.ege.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "course_assistants", indexes = {
        @Index(name = "assistant_id", columnList = "assistant_id")
})
public class CourseAssistant {
    @EmbeddedId
    private CourseAssistantId id;

    @MapsId("courseId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "course_id", nullable = false)
    private Cours courseId;

    @MapsId("assistantId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "assistant_id", nullable = false)
    private ResearchAssistant assistantId;

    public CourseAssistantId getId() {
        return id;
    }

    public void setId(CourseAssistantId id) {
        this.id = id;
    }

    public Cours getCourseId() {
        return courseId;
    }

    public void setCourseId(Cours courseId) {
        this.courseId = courseId;
    }

    public ResearchAssistant getAssistantId() {
        return assistantId;
    }

    public void setAssistantId(ResearchAssistant assistantId) {
        this.assistantId = assistantId;
    }

}