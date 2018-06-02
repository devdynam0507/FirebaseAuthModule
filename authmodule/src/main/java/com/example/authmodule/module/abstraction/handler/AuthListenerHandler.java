package com.example.authmodule.module.abstraction.handler;

import com.example.authmodule.module.abstraction.IAuthModuleListener;

/**
 * Created by 남대영 on 2018-05-19.
 */

public class AuthListenerHandler  {

    private IAuthModuleListener listener;

    public static AuthListenerHandler getHandler() {
        return new AuthListenerHandler();
    }

    public AuthListenerHandler registerListener(IAuthModuleListener listener) {
        this.listener = listener;
        return this;
    }

    public void run(Object... objects) {
        if(listener != null) {
            listener.authModuleCallback(objects);
        }
    }

}
