package com.SalarJavaDevGroup.MiddleWare;



import com.SalarJavaDevGroup.ManageDB.ManageTokens;
import com.SalarJavaDevGroup.Models.Networking.Request;
import com.SalarJavaDevGroup.Models.Networking.RequestType;
import com.SalarJavaDevGroup.Models.Token;
import com.SalarJavaDevGroup.Models.User;
import com.SalarJavaDevGroup.util.SessionUtil;
import org.hibernate.Session;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class TokenHandling {
    public static HashMap<String, Integer> tokens = new HashMap<>();
    public static void init() {
        Session session = SessionUtil.getSession();
        ManageTokens manageTokens = new ManageTokens(session);
        List tokensTmp = manageTokens.getAllTokens();
        for (Object object: tokensTmp) {
            Token token = (Token) object;
            tokens.put(token.getToken(), token.getUser().getId());
        }
        session.close();
    }


    public static String generateSafeToken(User user, Session session) {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        String token = encoder.encodeToString(bytes);
        tokens.put(token, user.getId());
        Token token1 = new Token(token, user);
        ManageTokens manageTokens = new ManageTokens(session);
        manageTokens.Save(token1);
        return token;
    }
    public static void HandleToken(Request request) {
        if (request.requestType == RequestType.Login ||
                request.requestType == RequestType.SignUp)
            return;
        request.user = tokens.getOrDefault(request.token, -1);
    }

}
