package com.nagam.example.ratelimiter.filter;

import com.nagam.example.ratelimiter.limiter.RedisRateLimiter;
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
    private static final Set<String> LIMITED_PATHS = Set.of("/hello", "/data"); // Configure the endpoints with rate limit

    public RateLimitFilter(RedisRateLimiter redisRateLimiter) {
        this.redisRateLimiter = redisRateLimiter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if (LIMITED_PATHS.contains(path)) {
            String ip = request.getRemoteAddr();
            String key = "rate-limit:" + ip + ":" + path;

            if (!redisRateLimiter.isAllowed(key)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("Rate limit exceeded. Please try again after 10 seconds.");
                response.getWriter().flush();
                response.getWriter().close();
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
