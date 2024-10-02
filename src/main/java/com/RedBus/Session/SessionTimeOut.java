package com.RedBus.Session;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SessionTimeOut extends OncePerRequestFilter {

    private static final int PAYMENT_API_TIMEOUT = 10 * 60;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (request.getRequestURL().equals("/api/payment/Intent")) {
            if (session == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Session expired. Please log in again.");
                return;

            } else {
                session.setMaxInactiveInterval(PAYMENT_API_TIMEOUT);

                if (isSessionExpired(session)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Session expired. Please log in again.");
                    return;

                }
            }
        }
        filterChain.doFilter(request, response);
    }
    private boolean isSessionExpired(HttpSession session) {
        // Check if the session has expired by comparing last access time with the current time
        return session.getLastAccessedTime() + (session.getMaxInactiveInterval() * 1000L) < System.currentTimeMillis();
    }
}