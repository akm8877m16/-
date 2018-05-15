package ywh.fan.lift.counter.service.mqttConfig;

import org.springframework.beans.factory.annotation.Value;

public interface  MqttConfig {



    /**
     * Custom Configuration
     *
     * @param broker
     * @param port
     * @param withUserNamePass
     */
    public void config(String broker, Integer port, Boolean withUserNamePass);

    /**
     * Default Configuration
     */
    public  void config();


}
