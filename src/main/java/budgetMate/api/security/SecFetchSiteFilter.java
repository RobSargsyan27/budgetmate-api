package budgetMate.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecFetchSiteFilter extends OncePerRequestFilter {
    private final RequestMatcher skipMatcher = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/v2/auth/**"),
            new AntPathRequestMatcher("/api/v1/contact")
    );

    /**
     * <h2>Passing filter conditions.</h2>
     * @param request {HttpServletRequest}
     * @return {boolean}
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())){
            return true;
        }

        return skipMatcher.matches(request);
    }

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

        if (secFetchSite == null || "cross-site".equalsIgnoreCase(secFetchSite) || "none".equalsIgnoreCase(secFetchSite)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }

        filterChain.doFilter(request, response);
    }
}