package org.vinhpham.sticket.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class ZoneManagerId implements Serializable {

    @Serial
    private static final long serialVersionUID = -3663630916239185308L;

    @NotNull
    @Column(name = "zone_id", nullable = false)
    private Long zoneId;

    @NotNull
    @Size(max = 50)
    @Column(name = "manager", nullable = false, length = 50)
    private String manager;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;

        ZoneManagerId that = (ZoneManagerId) o;

        return Objects.equals(this.manager, that.manager) &&
                Objects.equals(this.zoneId, that.zoneId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(manager, zoneId);
    }

}
