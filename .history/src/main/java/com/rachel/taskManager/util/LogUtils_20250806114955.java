package com.rachel.taskManager.util;

import com.rachel.taskManager.model.User;

public class LogUtils {

    // return the string of the mail until the first @ - count it as the userName
    // if the mail is null, return "no email"
    public static String formatUser(User user) {
        if (user == null) return "null";
        if (user.getMail() != null){
            int atIndex = user.getMail().indexOf('@');
            if (atIndex != -1) {
                return user.getMail().substring(0, atIndex);
            } else {
                return user.getMail();
            }
        } else {
            return "no email";
        }
    }
}
