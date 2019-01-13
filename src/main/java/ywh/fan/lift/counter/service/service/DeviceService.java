package ywh.fan.lift.counter.service.service;

import ywh.fan.lift.counter.service.entity.Device;

import java.util.List;

public interface DeviceService {

    List<Device> findByName(String name);

    Device save(Device device);

}
