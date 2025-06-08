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
    private Course course;

    @MapsId("assistantId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "assistant_id", nullable = false)
    private ResearchAssistant assistant;

    public CourseAssistantId getId() {
        return id;
    }

    public void setId(CourseAssistantId id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public ResearchAssistant getAssistant() {
        return assistant;
    }

    public void setAssistant(ResearchAssistant assistant) {
        this.assistant = assistant;
    }
}
