package org.vinhpham.sticket.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vinhpham.sticket.entities.Device;
import org.vinhpham.sticket.repositories.DeviceRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public Optional<Device> findById(String deviceId) {
        return deviceRepository.findById(deviceId);
    }

    public Device saveOrUpdate(Device device) {
        return deviceRepository.save(device);
    }
}
