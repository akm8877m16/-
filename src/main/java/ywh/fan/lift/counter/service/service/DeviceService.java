package ywh.fan.lift.counter.service.service;

import ywh.fan.lift.counter.service.entity.Device;

public interface DeviceService {

    Device findByName(String name);

    Device save(Device device);

}
