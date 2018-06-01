package ywh.fan.lift.counter.service.mqttConfig.impl;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ywh.fan.lift.counter.service.entity.Device;
import ywh.fan.lift.counter.service.mqttConfig.MQTTSubscriber;
import ywh.fan.lift.counter.service.mqttConfig.MqttConfig;
import ywh.fan.lift.counter.service.service.DeviceService;
import ywh.fan.lift.counter.service.util.PayloadUtil;

import javax.annotation.PreDestroy;
import java.sql.Timestamp;

@Component
public class MQTTSubscriberImpl implements MQTTSubscriber,MqttConfig,MqttCallback {

    @Value("${ywh.mqtt.broker:127.0.0.1}")
    private String broker = "39.104.49.84";

    @Value("${ywh.mqtt.qos:1}")
    private int qos = 1;

    @Value("${ywh.mqtt.port:1883}")
    private int port = 1883;

    @Value("${ywh.mqtt.userName:}")
    private String userName;

    @Value("${ywh.mqtt.password:}")
    private String password;

    private final String TCP = "tcp://";

    @Autowired
    DeviceService deviceService;

    private String brokerUrl = null;
    final private String colon = ":";
    final private String clientId = "fanlife";

    private MqttClient mqttClient = null;
    private MqttConnectOptions connectionOptions = null;
    private MemoryPersistence persistence = null;

    private static final Logger logger = LoggerFactory.getLogger(MQTTSubscriberImpl.class);

    public MQTTSubscriberImpl() {
        this.config();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.
     * Throwable)
     */
    @Override
    public void connectionLost(Throwable cause) {
        logger.info("Connection Lost: " + cause.toString());
        cause.printStackTrace();
        try {
            mqttClient.connect(connectionOptions);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.String,
     * org.eclipse.paho.client.mqttv3.MqttMessage)
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // Called when a message arrives from the server that matches any
        // subscription made by the client
        String time = new Timestamp(System.currentTimeMillis()).toString();
        //System.out.println();
        //System.out.println("***********************************************************************");
        //System.out.println("Message Arrived at Time: " + time + "  Topic: " + topic + "  Message: "
                //+ new String(message.getPayload()));
        //System.out.println("***********************************************************************");
        //System.out.println();
        if(PayloadUtil.isFreshAirDeviceAlive(message.getPayload())){
            String deviceName = PayloadUtil.getDeviceName(message.getPayload());
            System.out.println(deviceName);
            Device result = deviceService.findByName(deviceName);
            if(result == null){
                long startTime = System.currentTimeMillis();
                Device newDevice = new Device(deviceName, startTime, startTime, startTime);
                result = deviceService.save(newDevice);
                if(result != null){
                    System.out.println("new device added: " + deviceName);
                }
            }else{
                result.setEndTime(System.currentTimeMillis());
                result = deviceService.save(result);
                if(result != null){
                    System.out.println("new device updated: " + deviceName);
                }
            }
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse.paho
     * .client.mqttv3.IMqttDeliveryToken)
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Leave it blank for subscriber

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.monirthought.mqtt.subscriber.MQTTSubscriberBase#subscribeMessage(java.
     * lang.String)
     */
    @Override
    public void subscribeTopic(String topic) {
        try {
            this.mqttClient.subscribe(topic, this.qos);
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }

    @Override
    public void unsubscribeTopic(String topic) {
        try {
            this.mqttClient.unsubscribe(topic);
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.monirthought.mqtt.subscriber.MQTTSubscriberBase#disconnect()
     */
    public void disconnect() {
        try {
            this.mqttClient.disconnect();
        } catch (MqttException me) {
            logger.error("ERROR", me);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.monirthought.config.MQTTConfig#config(java.lang.String,
     * java.lang.Integer, java.lang.Boolean, java.lang.Boolean)
     */
    @Override
    public void config(String broker, Integer port, Boolean withUserNamePass) {

        String protocal = this.TCP;
        this.brokerUrl = protocal + this.broker + colon + port;
        this.persistence = new MemoryPersistence();
        this.connectionOptions = new MqttConnectOptions();

        try {
            this.mqttClient = new MqttClient(brokerUrl, clientId, persistence);
            this.connectionOptions.setCleanSession(true);
            if (true == withUserNamePass) {
                if (!this.password.isEmpty()) {
                    this.connectionOptions.setPassword(this.password.toCharArray());
                }
                if (!this.userName.isEmpty()) {
                    this.connectionOptions.setUserName(this.userName);
                }
            }
            this.mqttClient.connect(this.connectionOptions);
            this.mqttClient.setCallback(this);
        } catch (MqttException me) {
            me.printStackTrace();
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see com.monirthought.config.MQTTConfig#config()
     */
    @Override
    public void config() {

        this.brokerUrl = this.TCP + this.broker + colon + this.port;
        this.persistence = new MemoryPersistence();
        this.connectionOptions = new MqttConnectOptions();
        try {
            this.mqttClient = new MqttClient(brokerUrl, clientId, persistence);
            this.connectionOptions.setCleanSession(true);
            this.connectionOptions.setKeepAliveInterval(10);
            this.mqttClient.connect(this.connectionOptions);
            this.mqttClient.setCallback(this);

        } catch (MqttException me) {
            me.printStackTrace();
        }

    }

    @PreDestroy
    public void destroy(){
        logger.info("destroy method");
        if(mqttClient.isConnected()){
            disconnect();
        }
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
