package com.SalarJavaDevGroup;

import com.SalarJavaDevGroup.MiddleWare.MiddleWareHandler;
import com.SalarJavaDevGroup.MiddleWare.Router;
import com.SalarJavaDevGroup.MiddleWare.TokenHandling;
import com.SalarJavaDevGroup.Models.Networking.Request;
import com.SalarJavaDevGroup.Models.Networking.RequestType;
import com.SalarJavaDevGroup.Models.Networking.Response;
import com.SalarJavaDevGroup.Models.Networking.ResponseType;
import com.SalarJavaDevGroup.StreamHandler.StreamHandler;
import com.SalarJavaDevGroup.util.StreamToIpHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class ClientAgent extends Thread{
    private static final Logger logger = LogManager.getLogger(ClientAgent.class);
    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;
    Router router;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    MiddleWareHandler middleWareHandler;
    public ClientAgent(Socket socket) {
        this.socket = socket;
        logger.info(socket.getInetAddress() + " connected to server");
        start();
    }

    @Override
    public void run() {
        try {
            inputStream = socket.getInputStream();
        } catch (IOException ioException) {
            logger.error("Couldn't get Socket " + socket + " inputStream");
        }
        try {
            outputStream = socket.getOutputStream();
        } catch (IOException ioException) {
            logger.error("Couldn't get Socket " + socket + " outputStream");
        }
        dataInputStream = new DataInputStream(inputStream);
        dataOutputStream = new DataOutputStream(outputStream);
        StreamToIpHolder.out.put(dataOutputStream, socket.getInetAddress().getHostAddress());
        StreamToIpHolder.in.put(dataInputStream, socket.getInetAddress().getHostAddress());
        router = new Router(dataOutputStream);
        middleWareHandler = new MiddleWareHandler(router, new TokenHandling());
        while (true) {
            Request request = StreamHandler.getRequest(dataInputStream);
            if (request == null)
                return;
            if (request.requestType == RequestType.Ping) {
                StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted, ""));
                continue;
            }
            middleWareHandler.run(request);
        }
    }
}
