package ywh.fan.lift.counter.service.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ywh.common.util.response.Msg;
import ywh.common.util.response.ResultUtil;
import ywh.fan.lift.counter.service.entity.Device;
import ywh.fan.lift.counter.service.service.DeviceService;
import ywh.fan.lift.counter.service.util.ErrorEnum;

import java.util.List;

@Api(tags = "MQTT服务接口", description = "MQTT服务模块相关接口")
@RestController()
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    @ApiOperation(value = "返回统计时间",notes = "单位: 分钟")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceName", value = "设备8位编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/lifeCheck/{deviceName}", method = RequestMethod.GET)
    public Msg getDevice(@PathVariable(name = "deviceName") String deviceName) {
        List<Device> devices = deviceService.findByName(deviceName);
        if(devices != null && devices.size() > 0){
            double life = 0;
            Device device = devices.get(0);
            if(device != null){
                life = (device.getEndTime()-device.getStartTime())/1000/60.0; //minute
            }
            return ResultUtil.success(String.valueOf(life));
        }
        return ResultUtil.error(ErrorEnum.DEVICE_EMPTY.getCode(),ErrorEnum.DEVICE_EMPTY.getMsg());
    }

    @ApiOperation(value = "统计时间清零")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceName", value = "设备8位编号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/lifeCheck/reset/{deviceName}", method = RequestMethod.GET)
    public Msg resetDevice(@PathVariable(name = "deviceName") String deviceName) {
        List<Device> devices = deviceService.findByName(deviceName);
        if(devices != null && devices.size() > 0){
            Device device = devices.get(0);
            long resetTime = System.currentTimeMillis();
            device.setStartTime(resetTime);
            device.setEndTime(resetTime);
            device.setResetTime(resetTime);
            deviceService.save(device);
            return ResultUtil.success("counting reset complete");
        }
        return ResultUtil.success("no device available");
    }

}
