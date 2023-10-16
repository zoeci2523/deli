package com.delicoffee.deli.util;

import com.delicoffee.deli.model.entity.DeliUser;


public class UserHolder {
    private static final ThreadLocal<DeliUser> tl = new ThreadLocal<>();

    public static void saveUser(DeliUser user){
        tl.set(user);
    }

    public static DeliUser getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
