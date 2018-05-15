package ywh.fan.lift.counter.service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ywh.fan.lift.counter.service.entity.Device;
import ywh.fan.lift.counter.service.repository.DeviceRepository;
import ywh.fan.lift.counter.service.service.DeviceService;

@Service
public class DeviceServiceImpl implements DeviceService{

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public Device findByName(String name){
        return deviceRepository.findByName(name);
    }

    @Override
    public Device save(Device device){
        return deviceRepository.save(device);
    }

}
