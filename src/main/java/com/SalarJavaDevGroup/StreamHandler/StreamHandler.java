package com.SalarJavaDevGroup.StreamHandler;
import com.SalarJavaDevGroup.Models.Networking.Request;
import com.SalarJavaDevGroup.Models.Networking.Response;
import com.SalarJavaDevGroup.util.GsonHandler;
import com.SalarJavaDevGroup.util.StreamToIpHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class StreamHandler {
    private static final Logger logger = LogManager.getLogger(StreamHandler.class);
    public static void sendResponse(DataOutputStream dataOut, Response response) {
        try {
            String s = GsonHandler.getGson().toJson(response);
            byte[] bytes = s.getBytes();
            dataOut.writeInt(bytes.length);
            dataOut.write(bytes);
        } catch (IOException ioException) {
            logger.error("Couldn't send a response");
            logger.info(StreamToIpHolder.out.get(dataOut) + " disconnected from server");
        }
    }

    public static Request getRequest(DataInputStream dataIn) {
        byte[] bytes = null;
        try {
            int len = dataIn.readInt();
            bytes = dataIn.readNBytes(len);
        } catch (IOException ioException) {
            logger.error("Couldn't read a request");
            logger.info(StreamToIpHolder.in.get(dataIn) + " disconnected from server");
        }
        String requestJson = new String(bytes);
        Request request = GsonHandler.getGson().fromJson(requestJson, Request.class);
        return request;
    }
}
