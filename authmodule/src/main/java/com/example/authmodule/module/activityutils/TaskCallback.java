package com.example.authmodule.module.activityutils;

import android.content.Context;

/**
 * AsyncTask에서 Activity변경을 위한 CallBack 인터페이스
 * @author 남대영
 */
public interface TaskCallback {
    public void taskFinish(boolean success);
    public Context getContext();
}
