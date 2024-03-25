package org.vinhpham.sticket.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "devices")
public class Device {

    @Id
    @Size(max = 64)
    @Column(name = "device_id", nullable = false, length = 64)
    private String deviceId;

    @Size(max = 255)
    @NotNull
    @Column(name = "device_name", nullable = false)
    private String deviceName;

    @Size(max = 50)
    @NotNull
    @Column(name = "ip_address", nullable = false, length = 50)
    private String ipAddress;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @NotNull
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }
}
