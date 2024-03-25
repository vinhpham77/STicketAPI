package org.vinhpham.sticket.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner", nullable = false)
    private User owner;

    @Size(max = 255)
    @NotNull
    @Column(name = "qr_code", nullable = false)
    private String qrCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "checked_in_at")
    private Date checkedInAt;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @NotNull
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "updated_by", nullable = false)
    private User updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by")
    private User deletedBy;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = new Date();
        if (updatedAt == null) updatedAt = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket that = (Ticket) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(zone, that.zone)) return false;
        if (!Objects.equals(owner, that.owner)) return false;
        if (!Objects.equals(qrCode, that.qrCode)) return false;
        if (!Objects.equals(checkedInAt, that.checkedInAt)) return false;
        if (!Objects.equals(createdAt, that.createdAt)) return false;
        if (!Objects.equals(updatedAt, that.updatedAt)) return false;
        if (!Objects.equals(updatedBy, that.updatedBy)) return false;

        return Objects.equals(deletedAt, that.deletedAt);
    }
}
