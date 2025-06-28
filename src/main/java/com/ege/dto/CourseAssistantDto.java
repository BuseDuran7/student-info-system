package com.ege.dto;

public class CourseAssistantDto {
    private Long courseId;
    private Long assistantId;
    private CourseSummary course;
    private AssistantSummary assistant;

    // Constructors
    public CourseAssistantDto() {}

    public CourseAssistantDto(Long courseId, Long assistantId) {
        this.courseId = courseId;
        this.assistantId = assistantId;
    }

    // Getters and setters
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public Long getAssistantId() { return assistantId; }
    public void setAssistantId(Long assistantId) { this.assistantId = assistantId; }

    public CourseSummary getCourse() { return course; }
    public void setCourse(CourseSummary course) { this.course = course; }

    public AssistantSummary getAssistant() { return assistant; }
    public void setAssistant(AssistantSummary assistant) { this.assistant = assistant; }

    // Nested classes for summary information
    public static class CourseSummary {
        private Long id;
        private String courseCode;
        private String courseName;
        private String department;
        private Integer credits;
        private String semester;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getCourseCode() { return courseCode; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

        public String getCourseName() { return courseName; }
        public void setCourseName(String courseName) { this.courseName = courseName; }

        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }

        public Integer getCredits() { return credits; }
        public void setCredits(Integer credits) { this.credits = credits; }

        public String getSemester() { return semester; }
        public void setSemester(String semester) { this.semester = semester; }
    }

    public static class AssistantSummary {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String employeeId;
        private String department;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getEmployeeId() { return employeeId; }
        public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
    }
}