package com.SalarJavaDevGroup.Applications;

import com.SalarJavaDevGroup.ManageDB.ManageList;
import com.SalarJavaDevGroup.Models.Events.AddListEvent;
import com.SalarJavaDevGroup.Models.Events.AddUsersToListCompletedEvent;
import com.SalarJavaDevGroup.Models.Events.DeleteListEvent;
import com.SalarJavaDevGroup.Models.Events.ShowListEvent;
import com.SalarJavaDevGroup.Models.Networking.Request;
import com.SalarJavaDevGroup.Models.Networking.Response;
import com.SalarJavaDevGroup.Models.Networking.ResponseType;
import com.SalarJavaDevGroup.Models.Responses.ListResponse;
import com.SalarJavaDevGroup.Models.Responses.ListsListResponse;
import com.SalarJavaDevGroup.Models.User;
import com.SalarJavaDevGroup.Models.UserList;
import com.SalarJavaDevGroup.StreamHandler.StreamHandler;
import com.SalarJavaDevGroup.ManageDB.ManageUser;
import com.SalarJavaDevGroup.util.Compare;
import com.SalarJavaDevGroup.util.GsonHandler;
import com.SalarJavaDevGroup.util.SessionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserLists {
    private static final Logger logger = LogManager.getLogger(UserLists.class);
    private DataOutputStream dataOutputStream;
    public UserLists(DataOutputStream dataOutputStream) {
        this.dataOutputStream  = dataOutputStream;
    }
    public void getLists(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, "invalid user"));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        ArrayList <UserList> lists = new ArrayList<>(user.getLists());
        Collections.sort(lists, Compare.compare_lists_by_name);
        ListsListResponse listsListResponse = new ListsListResponse(lists);
        logger.info("User " + user.getUsername() + " requested user lists");
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted,
                GsonHandler.getGson().toJson(listsListResponse)));
        session.close();
    }

    public void addList(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, "invalid user"));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        ManageList manageList = new ManageList(session);
        User user = manageUser.getUserById(request.user);
        AddListEvent addListEvent = GsonHandler.getGson().fromJson(request.data, AddListEvent.class);
        for (UserList list: user.getLists()) {
            if (list.getName().equals(addListEvent.getListName())) {
                logger.warn("User " + user.getUsername() + " tried to add a list that exists");
                StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.InvalidData, "The List Exists"));
                return;
            }
        }
        UserList userList = new UserList(addListEvent.getListName());
        user.getLists().add(userList);
        logger.info("User " + user.getUsername() + " added a new list " + addListEvent.getListName());
        manageList.Save(userList);
        manageUser.Save(user);
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted, ""));
        session.close();
    }

    public void showList(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, "invalid user"));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        ShowListEvent showListEvent = GsonHandler.getGson().fromJson(request.data, ShowListEvent.class);
        for (UserList list : user.getLists())
            if (list.getName().equals(showListEvent.getListEvent())) {
                logger.info("User " + user.getUsername() + " requested a list " + list.getName());
                StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted,
                        GsonHandler.getGson().toJson(new ListResponse(list))));
                return;
            }
        logger.info("User " + user.getUsername() + " requested a user that doesn't exist");
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.InvalidData, "Group doesn't Exist"));
        session.close();
    }

    public void addUserToList(Request request)  {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, "invalid user"));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        ManageList manageList = new ManageList(session);
        User user = manageUser.getUserById(request.user);
        AddUsersToListCompletedEvent e = GsonHandler.getGson().fromJson(request.data, AddUsersToListCompletedEvent.class);
        UserList list = null;
        for (UserList userList: user.getLists())
            if (e.getListName().equals(userList.getName()))
                list = userList;
        if (list == null) {
            logger.warn("User " + user.getUsername() + " requested to add to list that doesn't exist");
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, ""));
            session.close();
            return;
        }
        Set<User> userSet = new HashSet<>();
        for (String username:e.getUsers()) {
            User target = manageUser.getUser(username);
            if (!user.getFollowing().contains(target))
                continue;
            userSet.add(target);
            logger.info("User " + user.getUsername() + " added to list " + list.getName() + " User " + target.getUsername());
        }
        list.setUsers(userSet);

        manageList.Save(list);
        session.close();
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted, ""));
    }

    public void deleteList(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, "invalid user"));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        DeleteListEvent e = GsonHandler.getGson().fromJson(request.data, DeleteListEvent.class);
        UserList list = null;
        for (UserList userList: user.getLists())
            if (e.getListName().equals(userList.getName()))
                list = userList;
        if (list == null) {
            logger.warn("User " + user.getUsername() + " tried to delete a list that doesn't exist");
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, ""));
            session.close();
            return;
        }
        user.getLists().remove(list);
        logger.info("User " + user.getUsername() + " removed list " + list.getId());
        manageUser.Save(user);
        session.close();
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted, ""));


    }

}
