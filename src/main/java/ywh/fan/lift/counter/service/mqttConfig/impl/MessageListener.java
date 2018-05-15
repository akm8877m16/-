package ywh.fan.lift.counter.service.mqttConfig.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ywh.fan.lift.counter.service.mqttConfig.MQTTSubscriber;

@Component
public class MessageListener implements Runnable{

    @Autowired
    MQTTSubscriber mqttSubscriber;

    @Override
    public void run() {
        while(true) {
            mqttSubscriber.subscribeTopic("M/#");//receive data M  send data D
        }
    }
}