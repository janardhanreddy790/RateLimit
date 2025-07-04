package com.nagam.example.ratelimiter.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagam.example.ratelimiter.limiter.RedisRateLimiter;
import com.nagam.example.ratelimiter.model.RateLimitErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RedisRateLimiter redisRateLimiter;
    private final ObjectMapper objectMapper;

    // Define which paths should be rate limited
    private static final Set<String> LIMITED_PATHS = Set.of("/hello", "/data"); // Configure the endpoints with rate limit

    public RateLimitFilter(RedisRateLimiter redisRateLimiter) {
        this.redisRateLimiter = redisRateLimiter;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Apply rate limiting only to selected paths
        if (LIMITED_PATHS.contains(path)) {
            String ip = request.getRemoteAddr();
            String key = "rate-limit:" + ip + ":" + path;

            if (!redisRateLimiter.isAllowed(key)) {
                RateLimitErrorResponse errorResponse = new RateLimitErrorResponse(
                        HttpStatus.TOO_MANY_REQUESTS.value(),
                        "Rate limit exceeded. Please try again after 10 seconds."
                );

                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                return;
            }
        }

        // Continue normally for non-limited or allowed requests
        filterChain.doFilter(request, response);
    }
}
