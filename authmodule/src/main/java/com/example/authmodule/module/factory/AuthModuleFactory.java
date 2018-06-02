package com.example.authmodule.module.factory;

import com.example.authmodule.module.EmailRegisterModule;
import com.example.authmodule.module.LoginModule;
import com.example.authmodule.module.abstraction.AuthenticationModule;
import com.example.authmodule.module.activityutils.TaskCallback;

/**
 * Created by 남대영 on 2018-05-06.
 */

public class AuthModuleFactory implements IAuthModuleFactory {

    private static AuthModuleFactory factory;

    private AuthModuleFactory(){}

    public static AuthModuleFactory getFactory()
    {
        if(factory == null) factory = new AuthModuleFactory();
        return factory;
    }

    /** 모듈 실행 메소드 */
    @Override
    public boolean runAuthModule(TaskCallback context, AuthType authType, String... authData) {
        AuthenticationModule module = null;
        switch (authType){
            case LOGIN:
                module = new LoginModule(context);
                return module.authenticating(authData);
            case EMAIL_REGISTER:
                module = new EmailRegisterModule(context);
                return module.authenticating(authData);
        }
        return false;
    }
}
