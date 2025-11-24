package com.smartshop.util;

import com.smartshop.enums.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public boolean isAuthenticated(HttpSession session) {

        return session != null && session.getAttribute("userId") != null;
    }

    public boolean isAdmin(HttpSession session){
        if(!isAuthenticated(session)){
            return false;
        }
        Role role = (Role) session.getAttribute("userRole");
        return  Role.ADMIN.equals(role);

    }

    public boolean isClient (HttpSession session){
        if(!isAuthenticated(session)){
            return false;
        }
        Role role = (Role) session.getAttribute("userRole");

        return Role.CLIENT.equals(role);
    }

    public Long getCurrentUserId(HttpSession session){
        if(!isAuthenticated(session)){
            return null;
        }
        return (Long) session.getAttribute("userId");
    }
    public Role getCurrentUserRole(HttpSession session){
        if(!isAuthenticated(session)){
            return null;
        }
        return (Role) session.getAttribute("userRole");
    }


}
