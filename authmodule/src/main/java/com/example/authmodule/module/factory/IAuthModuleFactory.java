package com.example.authmodule.module.factory;

import com.example.authmodule.module.activityutils.TaskCallback;

/**
 * Created by 남대영 on 2018-05-06.
 */
public interface IAuthModuleFactory {
    boolean runAuthModule(TaskCallback context, AuthType authType, String... authData);
}
