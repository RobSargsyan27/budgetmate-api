package budgetMate.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecFetchSiteFilter extends OncePerRequestFilter {

    /**
     * <h2>Check Sec-Fetch-Site header.</h2>
     * @param request     {HttpServletRequest}
     * @param response    {HttpServletResponse}
     * @param filterChain {FilterChain}
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException
    {
        String secFetchSite = request.getHeader("Sec-Fetch-Site");

        if ("cross-site".equalsIgnoreCase(secFetchSite) || "none".equalsIgnoreCase(secFetchSite)) {
            System.out.println("No header");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }

        filterChain.doFilter(request, response);
    }
}