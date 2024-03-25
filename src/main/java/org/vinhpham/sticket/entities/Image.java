package org.vinhpham.sticket.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int UNSIGNED")
    private Long id;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Size(max = 10)
    @NotNull
    @Column(name = "extension", nullable = false, length = 10)
    private String extension;

    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status = false;

    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image that = (Image) o;

        if (!Objects.equals(this.id, that.id)) return false;
        if (!Objects.equals(this.createdAt, that.createdAt)) return false;
        if (!Objects.equals(this.extension, that.extension)) return false;

        return Objects.equals(this.status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, extension, status);
    }
}
