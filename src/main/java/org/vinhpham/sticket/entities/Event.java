package org.vinhpham.sticket.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Lob
    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date")
    private Date endDate;

    @Size(max = 255)
    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "latitude", precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 9, scale = 6)
    private BigDecimal longitude;

    @NotNull
    @Column(name = "radius", nullable = false)
    private Double radius;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @NotNull
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by")
    private User deletedBy;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = new Date(System.currentTimeMillis());
        if (updatedAt == null) updatedAt = new Date(System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event that = (Event) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(description, that.description)) return false;
        if (!Objects.equals(startDate, that.startDate)) return false;
        if (!Objects.equals(endDate, that.endDate)) return false;
        if (!Objects.equals(location, that.location)) return false;
        if (!Objects.equals(latitude, that.latitude)) return false;
        if (!Objects.equals(longitude, that.longitude)) return false;
        if (!Objects.equals(radius, that.radius)) return false;
        if (!Objects.equals(createdBy, that.createdBy)) return false;
        if (!Objects.equals(createdAt, that.createdAt)) return false;
        if (!Objects.equals(updatedAt, that.updatedAt)) return false;
        if (!Objects.equals(deletedAt, that.deletedAt)) return false;

        return Objects.equals(deletedBy, that.deletedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, startDate, endDate, location, latitude, longitude, radius,
                createdBy, createdAt, updatedAt, deletedAt, deletedBy);
    }

}
