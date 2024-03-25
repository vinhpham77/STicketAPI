package org.vinhpham.sticket.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vinhpham.sticket.entities.Device;
import org.vinhpham.sticket.repositories.DeviceRepository;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public Device findByDeviceId(String deviceId) {
        return deviceRepository.findById(deviceId).orElse(null);
    }

    public Device saveOrUpdate(Device device) {
        return deviceRepository.save(device);
    }
}
