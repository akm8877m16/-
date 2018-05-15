package ywh.fan.lift.counter.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ywh.fan.lift.counter.service.entity.Device;
import ywh.fan.lift.counter.service.service.DeviceService;

@RestController()
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    @RequestMapping(value = "/lifeCheck/{deviceName}", method = RequestMethod.GET)
    public String getDevice(@PathVariable String deviceName) {
        Device device = deviceService.findByName(deviceName);
        double life = 0;
        if(device != null){
            life = (device.getEndTime()-device.getStartTime())/1000/60.0; //minute
        }
        return String.valueOf(life);
    }

    @RequestMapping(value = "/lifeCheck/reset/{deviceName}", method = RequestMethod.POST)
    public String resetDevice(@PathVariable String deviceName) {
        Device device = deviceService.findByName(deviceName);
        String  result = "";
        if(device != null){
           long resetTime = System.currentTimeMillis();
           device.setStartTime(resetTime);
           device.setEndTime(resetTime);
           device.setResetTime(resetTime);
           deviceService.save(device);
           result = "counting reset complete";
        }else{
            result = "no device available";
        }
        return result;
    }

}
