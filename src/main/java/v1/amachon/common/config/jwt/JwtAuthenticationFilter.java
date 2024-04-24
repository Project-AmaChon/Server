package v1.amachon.common.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import v1.amachon.common.advice.ErrorResponse;
import v1.amachon.common.config.jwt.exception.*;
import v1.amachon.common.config.security.repository.LogoutAccessTokenRedisRepository;
import v1.amachon.common.config.security.CustomUserDetailService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailService customUserDetailService;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private static final String AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getToken(request);
        if (accessToken != null) {
            checkLogout(accessToken);
            validateAccessToken(accessToken);
            String username = jwtTokenUtil.getUsername(accessToken);
            UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
            equalsUsernameFromTokenAndUserDetails(userDetails.getUsername(), username);
            accessTokenIsExpired(accessToken);
            processSecurity(request, userDetails);
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String headerAuth = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(TOKEN_PREFIX)) {
            return headerAuth.substring(7);
        }
        return headerAuth;
    }

    private void checkLogout(String accessToken) {
        if (logoutAccessTokenRedisRepository.existsById(accessToken)) {
            throw new AlreadyLogoutException();
        }
    }

    private void equalsUsernameFromTokenAndUserDetails(String userDetailsUsername, String tokenUsername) {
        if (!userDetailsUsername.equals(tokenUsername)) {
            throw new InvalidTokenException();
        }
    }

    private void validateAccessToken(String accessToken) {
        if (!jwtTokenUtil.validateToken(accessToken)) {
            throw new InvalidTokenFormatException();
        }
    }

    private void accessTokenIsExpired(String accessToken) {
        if (jwtTokenUtil.isTokenExpired(accessToken)) {
            throw new ExpiredTokenException();
        }
    }

    private void processSecurity(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
