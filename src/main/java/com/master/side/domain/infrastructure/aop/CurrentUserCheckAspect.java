package com.master.side.domain.infrastructure.aop;

import com.master.side.security.CheckCurrentUser;
import com.master.side.security.util.SecurityContextHelper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class CurrentUserCheckAspect {

    /**
     * @Before 어드바이스를 이용하여, @CheckCurrentUser 어노테이션이 붙은 메서드 실행 전에 현재 사용자를 검증합니다.
     */
    @Before("@annotation(checkCurrentUser)")
    public void validateCurrentUser(JoinPoint joinPoint, CheckCurrentUser checkCurrentUser) {
        UUID currentUserId = SecurityContextHelper.getCurrentUserId();
        if (currentUserId == null) {
            // 사용자 인증이 되어 있지 않은 경우 예외 발생 (필요에 따라 커스텀 예외를 정의할 수 있습니다)
            throw new RuntimeException("사용자가 인증되지 않았습니다.");
        }
        // 추가 검증 로직이 필요하다면 이곳에서 구현할 수 있습니다.
    }
}