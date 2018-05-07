package com.auth.untitledappauth.module.factory;

import com.auth.untitledappauth.activityutils.TaskCallback;

/**
 * Created by 남대영 on 2018-05-06.
 */
public interface IAuthModuleFactory {
    boolean runAuthModule(TaskCallback context, AuthType authType, String... authData);
}
