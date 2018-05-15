package com.auth.untitledappauth.module.factory;

import com.auth.untitledappauth.activityutils.TaskCallback;
import com.auth.untitledappauth.module.LoginModule;
import com.auth.untitledappauth.module.EmailRegisterModule;
import com.auth.untitledappauth.module.PhoneValidModule;
import com.auth.untitledappauth.module.abstraction.AuthenticationModule;

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
