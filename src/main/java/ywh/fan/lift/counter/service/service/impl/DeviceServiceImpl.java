package ywh.fan.lift.counter.service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ywh.fan.lift.counter.service.entity.Device;
import ywh.fan.lift.counter.service.repository.DeviceRepository;
import ywh.fan.lift.counter.service.service.DeviceService;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public List<Device> findByName(String name){
        List<Device> result = deviceRepository.findByName(name);
        logger.info("findByName count:"+result.size());
        return result;
    }

    @Override
    public Device save(Device device){
        return deviceRepository.save(device);
    }

}
