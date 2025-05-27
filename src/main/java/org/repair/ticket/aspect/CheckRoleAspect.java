package org.repair.ticket.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.repair.ticket.annotation.CheckRole;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class CheckRoleAspect {
    @Around("@annotation(checkRole)")
    public Object check(ProceedingJoinPoint pjp, CheckRole checkRole) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("Not authenticated");
        }

        var allowedRoles = Arrays.stream(checkRole.allowedRoles())
                .map(role -> "ROLE_" + role)
                .collect(Collectors.toSet());

        var hasAccess = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(allowedRoles::contains);

        if (!hasAccess) {
            throw new AccessDeniedException("Access denied for roles: " + Arrays.toString(checkRole.allowedRoles()));
        }

        return pjp.proceed();
    }
}
