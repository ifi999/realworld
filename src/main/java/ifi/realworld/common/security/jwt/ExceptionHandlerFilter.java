package ifi.realworld.common.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import ifi.realworld.common.exception.ApiError;
import ifi.realworld.common.exception.ErrorResponse;
import ifi.realworld.common.exception.api.UserAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }
        catch (UserAuthenticationException e) {
            setErrorResponse(e, response, HttpStatus.UNAUTHORIZED.value());
        }
    }

    private void setErrorResponse(Exception ex, HttpServletResponse response, int statusValue) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(statusValue);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiError error = new ApiError(ex.getMessage());
        try{
            response.getWriter().write(objectMapper.writeValueAsString(new ErrorResponse(error)));
        }
        catch (IOException e) {
            log.error("setErrorResponse - IOException");
        }
    }
}
