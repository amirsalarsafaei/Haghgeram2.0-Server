package com.SalarJavaDevGroup.MiddleWare;

import com.SalarJavaDevGroup.Models.Networking.Request;
import com.SalarJavaDevGroup.ReflectUtil.SuperMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

public class MiddleWareHandler {
    private static final Logger logger = LogManager.getLogger(MiddleWareHandler.class);
    LinkedList<SuperMethod> middlewares = new LinkedList<>();
    public MiddleWareHandler(Router router, TokenHandling tokenHandling) {
        try {
            middlewares.add(new SuperMethod(tokenHandling.getClass().getDeclaredMethod("HandleToken", Request.class),
                    tokenHandling));
            middlewares.add(new SuperMethod(router.getClass().getDeclaredMethod("route", Request.class), router));
        } catch (NoSuchMethodException e) {

        }
    }

    public void run(Request request) {
        for (SuperMethod method:middlewares) {
            try {
                method.invoke(request);
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error("Couldn't load method for request " + request.requestType);
            }
        }
    }

}
