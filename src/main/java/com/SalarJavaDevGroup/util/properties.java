package com.SalarJavaDevGroup.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class properties {
    private static final Logger logger = LogManager.getLogger(properties.class);
    public static String loadServerConfig(String propertyKey) {
        try {
            InputStream input = new FileInputStream(loadAddress("server"));
            java.util.Properties properties = new java.util.Properties();
            properties.load(input);
            input.close();
            return properties.getProperty(propertyKey);
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            logger.fatal("couldn't find server config properties");
        }
        logger.error("couldn't find server config " + propertyKey);
        if (propertyKey.equals("address"))
            return "localhost";
        if (propertyKey.equals("port"))
            return "8000";
        return null;
    }

    public static String loadAddress(String propertyKey) {
        try {
            InputStream input = new FileInputStream("./src/main/resources/config/Addresses.properties");
            java.util.Properties properties = new java.util.Properties();
            properties.load(input);
            input.close();
            return properties.getProperty(propertyKey);
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            logger.fatal("couldn't find address properties");
        }
        logger.error("couldn't find address " + propertyKey);
        return "";
    }

    public static String loadDialog(String propertyKey) {
        try {
            InputStream input = new FileInputStream(loadAddress("dialog"));
            java.util.Properties properties = new java.util.Properties();
            properties.load(input);
            input.close();
            return properties.getProperty(propertyKey);
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            logger.fatal("couldn't find dialog config properties");
        }
        logger.error("couldn't find dialog config " + propertyKey);
        return "null";
    }

}
