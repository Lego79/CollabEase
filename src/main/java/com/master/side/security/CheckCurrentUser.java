// src/main/java/com/master/side/security/CheckCurrentUser.java
package com.master.side.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 현재 로그인한 사용자가 존재하는지 검증하는 어노테이션.
 * 이 어노테이션이 붙은 메서드는 실행 전에 현재 사용자가 인증되었는지 AOP에서 확인합니다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckCurrentUser {
}
