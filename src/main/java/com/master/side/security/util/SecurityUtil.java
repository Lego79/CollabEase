package com.master.side.security.util;

import com.master.side.domain.model.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("No authentication information.");
        }
        return authentication.getName();
    }

    public static Member getCurrentMemberInfo() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("No authentication information.");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Member member) {
            return member;
        } else {
            throw new RuntimeException("Principal is not an instance of Member.");
        }
    }





}