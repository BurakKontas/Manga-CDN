package com.aburakkontas.manga_cdn.infrastructure.security;

import com.aburakkontas.manga.common.auth.queries.GetUserDetailsFromTokenQuery;
import com.aburakkontas.manga.common.auth.queries.results.GetUserDetailsFromTokenQueryResult;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtAuthEntryPoint authEntryPoint;
    private final QueryGateway queryGateway;

    @Autowired
    public JWTAuthenticationFilter(JwtAuthEntryPoint authEntryPoint, QueryGateway queryGateway) {
        this.authEntryPoint = authEntryPoint;
        this.queryGateway = queryGateway;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var securityGuards = new SecurityGuards(request, response, authEntryPoint);

        if(securityGuards.hasTokenGuard()) {
            if(!securityGuards.isValidToken(queryGateway)) return;

            var query = GetUserDetailsFromTokenQuery.builder()
                    .token(securityGuards.extractToken())
                    .build();

            var user = queryGateway.query(query, GetUserDetailsFromTokenQueryResult.class).join();

            var username = user.getUsername();
            var permissions = user.getPermissions().stream().map(SimpleGrantedAuthority::new).toList();
            var credentials = user.getUserId();

            var authentication = new UsernamePasswordAuthenticationToken(username, credentials, permissions);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
