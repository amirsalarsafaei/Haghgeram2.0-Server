package com.SalarJavaDevGroup.Models.Networking;

import com.SalarJavaDevGroup.Models.Networking.RequestType;
import com.SalarJavaDevGroup.Models.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;

public class Request implements Serializable {
    public RequestType requestType;
    public String data;
    public String token;
    public int user;

    public Request(RequestType requestType, String data) {
        this.requestType = requestType;
        this.data = data;
    }

    public Request(RequestType requestType, String data, String token) {
        this.requestType = requestType;
        this.data = data;
        this.token = token;
    }

}
