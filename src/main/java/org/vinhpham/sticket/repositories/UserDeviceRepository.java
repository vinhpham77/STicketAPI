package org.vinhpham.sticket.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vinhpham.sticket.entities.Device;
import org.vinhpham.sticket.entities.User;
import org.vinhpham.sticket.entities.UserDevice;
import org.vinhpham.sticket.entities.UserDeviceId;

import java.util.List;
import java.util.Set;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, UserDeviceId> {
}
