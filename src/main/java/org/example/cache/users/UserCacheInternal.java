package org.example.cache.users;

import org.example.model.user.User;

public class UserCacheInternal extends UserCache<String, User> {


    public UserCacheInternal() {
        super("users");
    }
}