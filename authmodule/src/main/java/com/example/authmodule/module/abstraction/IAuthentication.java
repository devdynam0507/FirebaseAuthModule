package com.example.authmodule.module.abstraction;

/**
 * 인증 모듈 인터페이스
 * @author 남대영
 */
public interface IAuthentication {

    public boolean isAuthenticated(); /** 이미 인증된 계정인지 확인합니다. */

    /**
     * 폼에서 받은 데이터를 인증합니다.
     * @return 인증 성공여부
     * */
    public boolean authenticating(String... authData);

}
