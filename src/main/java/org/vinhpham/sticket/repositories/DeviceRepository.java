package org.vinhpham.sticket.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vinhpham.sticket.entities.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
}
