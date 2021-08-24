package com.SalarJavaDevGroup.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;

public class StreamToIpHolder {
    public static HashMap<DataOutputStream, String> out = new HashMap<>();
    public static HashMap<DataInputStream, String> in = new HashMap<>();
}
