package aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.repair.ticket.annotation.CheckRole;
import org.repair.ticket.aspect.CheckRoleAspect;
import org.repair.ticket.dictionary.Role;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class CheckRoleAspectTest {

    CheckRoleAspect aspect;

    @BeforeEach
    void setUp() {
        aspect = new CheckRoleAspect();
        SecurityContextHolder.clearContext();
    }

    @Test
    void allowsIfHasRole() throws Throwable {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getAuthorities()).thenReturn((Collection) List.of(
                (GrantedAuthority) () -> "ROLE_ADMIN"
        ));
        SecurityContextHolder.getContext().setAuthentication(auth);

        CheckRole checkRole = mock(CheckRole.class);
        when(checkRole.allowedRoles()).thenReturn(new Role[]{Role.ADMIN});

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);

        aspect.check(joinPoint, checkRole);
        verify(joinPoint).proceed();
    }

    @Test
    void deniesIfNoRole() throws Throwable {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);

        when(auth.getAuthorities()).thenReturn((Collection) List.of(
                (GrantedAuthority) () -> "ROLE_ADMIN"
        ));

        CheckRole checkRole = mock(CheckRole.class);
        when(checkRole.allowedRoles()).thenReturn(new Role[]{Role.ADMIN});

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);

        assertThrows(AccessDeniedException.class, () -> aspect.check(joinPoint, checkRole));
        verify(joinPoint, never()).proceed();
    }

    @Test
    void deniesIfNotAuthenticated() throws Throwable {
        // mock authentication
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(false);
        SecurityContextHolder.getContext().setAuthentication(auth);

        CheckRole checkRole = mock(CheckRole.class);
        when(checkRole.allowedRoles()).thenReturn(new Role[]{Role.ADMIN});

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);

        assertThrows(AccessDeniedException.class, () -> aspect.check(joinPoint, checkRole));
        verify(joinPoint, never()).proceed();
    }

    @Test
    void deniesIfNoAuthenticationInContext() throws Throwable {
        SecurityContextHolder.clearContext();
        CheckRole checkRole = mock(CheckRole.class);
        when(checkRole.allowedRoles()).thenReturn(new Role[]{Role.ADMIN});

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);

        assertThrows(AccessDeniedException.class, () -> aspect.check(joinPoint, checkRole));
        verify(joinPoint, never()).proceed();
    }
}