package TaskListManagmentSecurity.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    @NotBlank(message = "Title should not be empty")
    @Size(min = 2, max = 255, message = "Title should be between 2 and 30 characters")
    private String title;

    @Column(name = "description")
    @NotBlank(message = "Description should not be empty")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public Task() {
    }

    public Task(int id, String title, String description, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
