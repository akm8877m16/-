package ywh.fan.lift.counter.service.repository;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ywh.fan.lift.counter.service.entity.Device;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Integer> {

    // 使用 p0 做key  这里碰巧设备名称就可以作为key 来唯一标识
    //@Cacheable(value = "devicesLife", key = "'device:life:'+ #p0", unless="#result==null")
    @Query("select t from Device t where t.name = :name")
    List<Device> findByName(@Param("name") String name);


    @Override
    //@CachePut(value = "devicesLife", key = "'device:life:'+ #p0.name")
    Device save(Device device);

}
