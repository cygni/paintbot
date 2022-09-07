package se.cygni.paintbot.socketserver.security;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final static String AUTHORIZATION = "Authorization";
    private final static String BEARER = "Bearer";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> authorizationHeaderToken = extractTokenFromAuthorizationHeader(request);
        Optional<String> queryParameterToken = extractTokenFromQueryParameter(request);

        validateToken(request, authorizationHeaderToken, queryParameterToken);
        filterChain.doFilter(request, response);
    }

    private Optional<String> extractTokenFromQueryParameter(HttpServletRequest request) {
        String leToken = request.getParameter(AUTHORIZATION);
        if(leToken != null) {
            return Optional.of(leToken);
        } else {
            return Optional.empty();
        }
    }

    private Optional<String> extractTokenFromAuthorizationHeader(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader(AUTHORIZATION);
        if (StringUtils.startsWith(requestTokenHeader, BEARER)) {
            return Optional.of(requestTokenHeader.substring(7));
        } else {
            return Optional.empty();
        }
    }


    private void validateToken(HttpServletRequest request, Optional<String> jwtToken, Optional<String> otherJwtToken) {
        try {
            // Either use token in Authorization header or in Query Parameter
            if(jwtToken.isEmpty() && otherJwtToken.isEmpty()) {
                logger.error("No JWT token present");
                throw new IllegalArgumentException();
            }
            String jwt = jwtToken.orElseGet(otherJwtToken::get);

            String username = jwtTokenUtil.getUsernameFromToken(jwt);
            if (StringUtils.isNotEmpty(username) && null == SecurityContextHolder.getContext().getAuthentication()) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        } catch (IllegalArgumentException e) {
            logger.error("Unable to fetch JWT Token");
        } catch (ExpiredJwtException e) {
            logger.error("JWT Token is expired");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
