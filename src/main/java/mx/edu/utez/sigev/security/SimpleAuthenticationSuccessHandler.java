package mx.edu.utez.sigev.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class SimpleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        boolean hasAdministradorRole = false;
        boolean hasEnlaceRole = false;
        boolean hasMiembroRole = false;
        boolean hasPresidenteRole = false;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ROL_ADMINISTRADOR")) {
                hasAdministradorRole = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("ROL_ENLACE")) {
                hasEnlaceRole = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("ROL_MIEMBRO")) {
                hasMiembroRole = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("ROL_PRESIDENTE")) {
                hasPresidenteRole = true;
                break;
            }
        }

        if (hasAdministradorRole) {
            redirectStrategy.sendRedirect(request, response, "/administrador/dashboard");
        } else if (hasEnlaceRole) {
            redirectStrategy.sendRedirect(request, response, "/enlace/dashboard");
        } else if (hasMiembroRole) {
            redirectStrategy.sendRedirect(request, response, "/miembro/dashboard");
        } else if (hasPresidenteRole) {
            redirectStrategy.sendRedirect(request, response, "/presidente/dashboard");
        } else {
            redirectStrategy.sendRedirect(request, response, "/login");
        }

    }

}
