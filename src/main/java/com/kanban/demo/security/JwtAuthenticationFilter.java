package com.kanban.demo.security;

import com.kanban.demo.domain.User;
import com.kanban.demo.services.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static com.kanban.demo.security.SecurityConstants.HEADER_STRING;
import static com.kanban.demo.security.SecurityConstants.TOKEN_PREFIX;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try{
            String jwt = getJWTFromRequest(request);

            if(StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)){
                Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);
                User userDetails = customUserDetailService.loadUserById(userId);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, Collections.emptyList());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception ex){
            logger.error("Could not set user authentication in security context",ex);
        }
        filterChain.doFilter(request,response);
    }

    private String getJWTFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader(HEADER_STRING);

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)){
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
