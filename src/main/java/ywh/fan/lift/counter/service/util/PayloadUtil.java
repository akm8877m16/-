package ywh.fan.lift.counter.service.util;

import org.springframework.stereotype.Component;

public class PayloadUtil {
    public static final int PAYLOAD_LENGTH_STATUS = 14;
    public static final int PAYLOAD_LENGTH_OFF = 9;
    public static final int PAYLOAD_HEAD = 74;
    public static final int SHUTDOWN_MODE = 0;

    public PayloadUtil(){}

    public static final boolean isFreshAirDeviceAlive(byte[] payload){
        int head = payload[0];
        if(head != PAYLOAD_HEAD){
            return false;
        }
        if(payload.length >= PAYLOAD_LENGTH_STATUS){
            int shutdownMode = payload[6];
            if(shutdownMode == 0 || shutdownMode == 3){
                return false;
            }else{
                return true;
            }
        }
        if(payload.length >= PAYLOAD_LENGTH_OFF){
            if(payload[5] == 0 && payload[6] == 255){
                return false;
            }else{
                return true;
            }
        }
        return false;
    }

    /**
     * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     * * @param src byte[] data
     * @return hex string
    */
    public static final String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static final String getDeviceName(byte[] src){
        byte[] device=new byte[4];
        System.arraycopy(src, 1, device, 0, 4);
        return bytesToHexString(device);
    }


}
