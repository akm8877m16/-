package ywh.fan.lift.counter.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import ywh.fan.lift.counter.service.mqttConfig.MQTTSubscriber;
import ywh.fan.lift.counter.service.mqttConfig.impl.MQTTSubscriberImpl;
import ywh.fan.lift.counter.service.mqttConfig.impl.MessageListener;

@Component
public class AppConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

}
