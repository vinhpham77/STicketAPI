package org.vinhpham.sticket.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "zone_managers")
public class ZoneManager {

    @EmbeddedId
    private ZoneManagerId id;

    @MapsId("zoneId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @MapsId("manager")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manager", nullable = false)
    private User manager;

}
