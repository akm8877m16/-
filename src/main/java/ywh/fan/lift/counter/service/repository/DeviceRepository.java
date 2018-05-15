package ywh.fan.lift.counter.service.repository;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import ywh.fan.lift.counter.service.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, Integer> {

    // 使用 p0 做key  这里碰巧设备名称就可以作为key 来唯一标识
    @Cacheable(value = "devices", key = "#p0", unless="#result==null")
    Device findByName(String name);


    @Override
    @CachePut(value = "devices", key = "#p0.name")
    Device save(Device device);

}
