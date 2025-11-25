package com.smartshop.util;

import com.smartshop.enums.Role;
import com.smartshop.exception.ForbiddenException;
import com.smartshop.exception.UnauthorizedException;
import jakarta.servlet.http.HttpSession;

public class SecurityUtil {

    public static void checkAuthentication(HttpSession session) {
        if (session == null || session.getAttribute("userId") == null) {
            throw new UnauthorizedException("Authentication required");
        }
    }

    public static void checkAdmin(HttpSession session) {
        checkAuthentication(session);
        Role role = (Role) session.getAttribute("userRole");
        if (!Role.ADMIN.equals(role)) {
            throw new ForbiddenException("Admin access required");
        }
    }

    public static void checkClient(HttpSession session) {
        checkAuthentication(session);
        Role role = (Role) session.getAttribute("userRole");
        if (!Role.CLIENT.equals(role)) {
            throw new ForbiddenException("Client access required");
        }
    }

    public static Long getCurrentUserId(HttpSession session) {
        checkAuthentication(session);
        return (Long) session.getAttribute("userId");
    }

    public static Role getCurrentUserRole(HttpSession session) {
        checkAuthentication(session);
        return (Role) session.getAttribute("userRole");
    }
}
