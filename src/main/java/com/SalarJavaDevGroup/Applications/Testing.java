package com.SalarJavaDevGroup.Applications;

import com.SalarJavaDevGroup.Models.Networking.Request;
import com.SalarJavaDevGroup.Models.Networking.Response;
import com.SalarJavaDevGroup.Models.Networking.ResponseType;
import com.SalarJavaDevGroup.StreamHandler.StreamHandler;
import com.SalarJavaDevGroup.util.GsonHandler;

import java.io.DataOutputStream;

public class Testing {
    private DataOutputStream dataOutputStream;
    public Testing(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }
//
//    public void getTestBot(Request request) {
//        if (Holder.botTest == null)
//            Holder.botTest = new BotTest();
//
//        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted,
//                GsonHandler.getGson().toJson(Holder.botTest.getPane())));
//    }
//
//    public void eventTestBot(Request request) {
//        if (Holder.botTest == null)
//            Holder.botTest = new BotTest();
//        Integer id = Integer.valueOf(request.data);
//        Holder.botTest.event(id);
//        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted,
//                GsonHandler.getGson().toJson(Holder.botTest.getPane())));
//    }
}
