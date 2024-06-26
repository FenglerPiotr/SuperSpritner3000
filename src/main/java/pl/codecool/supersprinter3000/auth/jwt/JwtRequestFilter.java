package pl.codecool.supersprinter3000.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer";

    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;

    public JwtRequestFilter(JwtTokenService jwtTokenService, UserDetailsService userDetailsService) {
        this.jwtTokenService = jwtTokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(BEARER)) {
            String jwtToken = authHeader.substring(BEARER.length() + 1);

            try {
                String userName = jwtTokenService.getUserNameFromToken(jwtToken);
                authenticateUser(request, jwtToken, userName);
            } catch (IllegalArgumentException ex) {
                log.warn("Unable to parse JWT token", ex);
            } catch (ExpiredJwtException ex) {
                log.warn("JWT token {} is expired", jwtToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUser(HttpServletRequest request, String jwtToken, String userName) {
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

            if (jwtTokenService.validateToken(jwtToken, userName)) {
                var springAuthToken = new UsernamePasswordAuthenticationToken(
                        userDetails, userDetails.getPassword(), userDetails.getAuthorities()
                );
                springAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(springAuthToken);
            }
        }
    }
}
