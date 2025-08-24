package budgetMate.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.csrf.CsrfToken;

import java.io.IOException;

@Component
public class CsrfCookieFilter extends OncePerRequestFilter {

    /**
     * <h2>Define CSRF token in response.</h2>
     * @param request     {HttpServletRequest}
     * @param response    {HttpServletResponse}
     * @param filterChain {FilterChain}
     */
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException
    {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (token != null) {
            token.getToken();
            response.setHeader("X-CSRF-TOKEN", token.getToken());
        }

        filterChain.doFilter(request, response);
    }
}
