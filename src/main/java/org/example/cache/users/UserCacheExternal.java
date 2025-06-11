package org.example.cache.users;

import org.example.model.user.User;

public class UserCacheExternal extends UserCache<String, User> {

    public UserCacheExternal() {
        super("users_external");
    }
}
