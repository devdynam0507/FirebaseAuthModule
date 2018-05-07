package com.auth.untitledappauth.module.abstraction;

/**
 * Created by 남대영 on 2018-05-05.
 */

public interface IAuthentication {

    public boolean isAuthenticated(); /** 이미 인증된 계정인지 확인합니다. */

    /**
     * 폼에서 받은 데이터를 인증합니다.
     * @return 인증 성공여부
     * */
    public boolean authenticating(String... authData);

}
