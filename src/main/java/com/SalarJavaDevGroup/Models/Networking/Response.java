package com.SalarJavaDevGroup.Models.Networking;

import java.io.Serializable;

public class Response implements Serializable {
    ResponseType responseType;
    String data;
    public Response(ResponseType responseType, String data) {
        this.responseType = responseType;
        this.data = data;
    }
}
