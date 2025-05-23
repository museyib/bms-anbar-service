package az.inci.wms.security.config;

import az.inci.wms.model.v4.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final AuthenticationEntryPoint defaultEntryPoint = new Http403ForbiddenEntryPoint();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (!request.getRequestURI().startsWith("/v4")) {
            Response<Void> apiResponse = Response.getUserErrorResponse("API versiyası uyğun deyil. Proqramı yeniləyin!");
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
            response.getWriter().flush();
            response.getWriter().close();
        } else
            defaultEntryPoint.commence(request, response, authException);
    }
}
