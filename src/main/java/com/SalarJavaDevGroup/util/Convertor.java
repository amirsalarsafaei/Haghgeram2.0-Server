package com.SalarJavaDevGroup.util;

import com.SalarJavaDevGroup.Models.User;

import java.util.ArrayList;
import java.util.Set;

public abstract class Convertor {
    public static ArrayList<Integer> UserSetToIdArrayList(Set<User> users) {
        ArrayList<Integer> res = new ArrayList<>();
        for (User user:users) {
            res.add(user.getId());
        }
        return res;
    }
}
