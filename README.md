# UntitiedFirebaseAuthModule v1.0
'Untitled'앱의 Firebase 회원가입 모듈입니다 

## Untitledapp authentication module usage java example
```java
/** AuthModuleFactory.runAuthModule 파라미터 */
AuthModuleFactory.runAuthModule(TaskCallback, AuthType, String...)

/** email, password로그인 호출 예제 */
AuthModuleFactory.getFactory().runAuthModule(LoginActivity.this, AuthType.LOGIN, email, password);

/** email, password회원가입 호출 예제 */
AuthModuleFactory.getFactory().runAuthModule(EmailRegisterActivity.this, AuthType.EMAIL_REGISTER, email, password, phone, card);
```

## 인증모듈을 사용하는 액티비티
*TaskCallback interface를 상속받고 TaskCallback함수들을 정의해주셔야 합니다.*
*모듈을 추가하고싶을땐 AuthenticationModule클래스를 상속받고 정의해주신뒤 AuthModuleFactory, AuthType을 수정하시면 됩니다.*


## 라이브러리
/**
  	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
*/

/**
  	dependencies {
	        implementation 'com.github.devdynam0507:FirebaseAuthModule:1.1'
	}
*/
