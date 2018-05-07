package com.auth.untitledappauth.activityutils;

import android.content.Context;

/**
 * Created by 남대영 on 2018-05-07.
 */

public interface TaskCallback {
    public void taskFinish(boolean success);
    public Context getContext();
}
