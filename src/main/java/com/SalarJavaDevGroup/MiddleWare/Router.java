package com.SalarJavaDevGroup.MiddleWare;
import com.SalarJavaDevGroup.Applications.*;
import com.SalarJavaDevGroup.Models.Conversation;
import com.SalarJavaDevGroup.Models.Networking.Request;
import com.SalarJavaDevGroup.Models.Networking.RequestType;
import com.SalarJavaDevGroup.Models.Networking.Response;
import com.SalarJavaDevGroup.Models.Networking.ResponseType;
import com.SalarJavaDevGroup.ReflectUtil.SuperMethod;
import com.SalarJavaDevGroup.StreamHandler.StreamHandler;
import com.SalarJavaDevGroup.util.GsonHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.HashMap;

public class Router {
    private static final Logger logger = LogManager.getLogger(Router.class);
    EnumMap<RequestType, SuperMethod> routes;
    DataOutputStream dataOutputStream;

    public Router(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
        routes = new EnumMap<>(RequestType.class);
        Authenticate authenticate = new Authenticate(dataOutputStream);
        Tweets tweets = new Tweets(dataOutputStream);
        UserRelations userRelations = new UserRelations(dataOutputStream);
        Comments comments = new Comments(dataOutputStream);
        Explore explore = new Explore(dataOutputStream);
        Profile profile = new Profile(dataOutputStream);
        UserLists userLists = new UserLists(dataOutputStream);
        Messenger messenger = new Messenger(dataOutputStream);
        Testing testing = new Testing(dataOutputStream);
        PrivacySetting privacySetting = new PrivacySetting(dataOutputStream);
        Conversations conversations = new Conversations(dataOutputStream);
        Setting setting = new Setting(dataOutputStream);
        Notifications notifications = new Notifications(dataOutputStream);
        Messages messages = new Messages(dataOutputStream);
        Online online = new Online(dataOutputStream);
        HyperLink hyperLink = new HyperLink(dataOutputStream);
        try {
            routes.put(RequestType.SignUp, new SuperMethod(Authenticate.class.getMethod("signUp", Request.class),
                    authenticate));
            routes.put(RequestType.Login, new SuperMethod(Authenticate.class.getMethod("login", Request.class),
                    authenticate));
            routes.put(RequestType.TimeLine, new SuperMethod(Tweets.class.getMethod("getTimeLine", Request.class),
                    tweets));
            routes.put(RequestType.SendTweet, new SuperMethod(Tweets.class.getMethod("sendTweet", Request.class),
                    tweets));
            routes.put(RequestType.Mute, new SuperMethod(UserRelations.class.getMethod("mute", Request.class),
                    userRelations));
            routes.put(RequestType.Block, new SuperMethod(UserRelations.class.getMethod("block", Request.class),
                    userRelations));
            routes.put(RequestType.SendRetweet, new SuperMethod(Tweets.class.getMethod("sendRetweet", Request.class),
                    tweets));
            routes.put(RequestType.Comments, new SuperMethod(Tweets.class.getMethod("getComments", Request.class),
                    tweets));
            routes.put(RequestType.LikeTweet, new SuperMethod(Tweets.class.getMethod("likeTweet", Request.class),
                    tweets));
            routes.put(RequestType.SendComment, new SuperMethod(Comments.class.getMethod("sendComment", Request.class),
                    comments));
            routes.put(RequestType.Discovery, new SuperMethod(Explore.class.getMethod("getDiscovery", Request.class),
                    explore));
            routes.put(RequestType.Profile, new SuperMethod(Profile.class.getMethod("getProfile", Request.class),
                    profile));
            routes.put(RequestType.Follow, new SuperMethod(UserRelations.class.getMethod("follow", Request.class),
                    userRelations));
            routes.put(RequestType.ListsList, new SuperMethod(UserLists.class.getMethod("getLists", Request.class),
                    userLists));
            routes.put(RequestType.AddList, new SuperMethod(UserLists.class.getMethod("addList", Request.class),
                    userLists));
            routes.put(RequestType.List, new SuperMethod(UserLists.class.getMethod("showList", Request.class),
                    userLists));
            routes.put(RequestType.Followings, new SuperMethod(Profile.class.getMethod("getFollowings", Request.class),
                    profile));
            routes.put(RequestType.AddUserToList,
                    new SuperMethod(UserLists.class.getMethod("addUserToList", Request.class), userLists));
            routes.put(RequestType.DeleteList, new SuperMethod(UserLists.class.getMethod("deleteList", Request.class),
                    userLists));
            routes.put(RequestType.SelfProfile, new SuperMethod(Profile.class.getMethod("getSelfProfile", Request.class),
                    profile));
            routes.put(RequestType.ConversationList,
                    new SuperMethod(Messenger.class.getMethod("getConversationList", Request.class), messenger));
            routes.put(RequestType.GroupMessageList,
                    new SuperMethod(Messenger.class.getMethod("getGroupMessageList", Request.class), messenger));
            routes.put(RequestType.Conversation,
                    new SuperMethod(Messenger.class.getMethod("getConversation", Request.class), messenger));
            routes.put(RequestType.SendMessageToConversation,
                    new SuperMethod(Messenger.class.getMethod("sendMessageToConversation", Request.class), messenger));
            routes.put(RequestType.SendGroupMessage,
                    new SuperMethod(Messenger.class.getMethod("sendGroupMessage", Request.class), messenger));
            routes.put(RequestType.SendMessageToUser,
                    new SuperMethod(Messenger.class.getMethod("sendMessageToUser", Request.class), messenger));
//            routes.put(RequestType.Test, new SuperMethod(Testing.class.getMethod("getTestBot", Request.class),
//                    testing));
//            routes.put(RequestType.Test2, new SuperMethod(Testing.class.getMethod("eventTestBot", Request.class),
//                    testing));
            routes.put(RequestType.LeaveGroup,
                    new SuperMethod(Conversations.class.getMethod("leaveGroup", Request.class), conversations));
            routes.put(RequestType.AddMemberToGroup,
                    new SuperMethod(Conversations.class.getMethod("addUsersToGroup", Request.class), conversations));
            routes.put(RequestType.CreateGroup,
                    new SuperMethod(Conversations.class.getMethod("createGroup", Request.class), conversations));
            routes.put(RequestType.UnBlock,
                    new SuperMethod(UserRelations.class.getMethod("unblock", Request.class), userRelations));
            routes.put(RequestType.BlackList,
                    new SuperMethod(UserRelations.class.getMethod("blackList", Request.class), userRelations));
            routes.put(RequestType.Privacy,
                    new SuperMethod(PrivacySetting.class.getMethod("getPrivacy", Request.class), privacySetting));
            routes.put(RequestType.ChangePrivacy,
                    new SuperMethod(PrivacySetting.class.getMethod("changePrivacy", Request.class), privacySetting));
            routes.put(RequestType.EditProfile,
                    new SuperMethod(Setting.class.getMethod("editProfile", Request.class), setting));
            routes.put(RequestType.EditProfileInfo,
                    new SuperMethod(Setting.class.getMethod("getEditProfileInfo", Request.class), setting));
            routes.put(RequestType.DeleteAccount,
                    new SuperMethod(Setting.class.getMethod("deleteAccount", Request.class), setting));
            routes.put(RequestType.Notifications,
                    new SuperMethod(Notifications.class.getMethod("getNotifications", Request.class), notifications));
            routes.put(RequestType.AcceptRequest,
                    new SuperMethod(Notifications.class.getMethod("acceptFollowRequest", Request.class), notifications));
            routes.put(RequestType.DenyRequest,
                    new SuperMethod(Notifications.class.getMethod("denyFollowRequest", Request.class), notifications));
            routes.put(RequestType.FollowRequests,
                    new SuperMethod(Setting.class.getMethod("getFollowRequests", Request.class), setting));
            routes.put(RequestType.EditMessage,
                    new SuperMethod(Messages.class.getMethod("editMessage", Request.class), messages));
            routes.put(RequestType.DeleteMessage,
                    new SuperMethod(Messages.class.getMethod("deleteMessage", Request.class), messages));
            routes.put(RequestType.Online,
                    new SuperMethod(Online.class.getMethod("stillOnline", Request.class), online));
            routes.put(RequestType.HyperLink,
                    new SuperMethod(HyperLink.class.getMethod("hyperLink", Request.class), hyperLink));
            routes.put(RequestType.SendTweetGroupMessage,
                    new SuperMethod(Messenger.class.getMethod("sendGroupTweetMessage", Request.class), messenger));
            routes.put(RequestType.ReportTweet,
                    new SuperMethod(Tweets.class.getMethod("reportTweet", Request.class), tweets));
        } catch (NoSuchMethodException e) {
            logger.error("Method not found in router");
        }
    }

    public void route(Request request) {
        try {
            routes.get(request.requestType).invoke(request);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            logger.error("Problem in running request : " + request.requestType);
            Response response = new Response(ResponseType.BadRequest, "Couldn't route");
            StreamHandler.sendResponse(dataOutputStream, response);
        }
    }

}
