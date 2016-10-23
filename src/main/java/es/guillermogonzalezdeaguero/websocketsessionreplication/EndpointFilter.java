package es.guillermogonzalezdeaguero.websocketsessionreplication;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Guillermo González de Agüero
 */
@WebFilter(SampleEndpoint.PATH)
public class EndpointFilter implements Filter {

    public static final String COOKIE_WEBSOCKET_SESSION_UUID = "WSSessionUUID";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        chain.doFilter(request, response);

        boolean cookieSet = false;
        if (httpRequest.getCookies() != null) {
            for (Cookie cookie : httpRequest.getCookies()) {
                if (COOKIE_WEBSOCKET_SESSION_UUID.equals(cookie.getName())) {
                    cookieSet = true;
                    break;
                }
            }
        }

        if (!cookieSet) {
            String uuid = httpResponse.getHeader(COOKIE_WEBSOCKET_SESSION_UUID);
            if (uuid != null) {
                httpResponse.addCookie(new Cookie(COOKIE_WEBSOCKET_SESSION_UUID, uuid));
            }
        }
    }

    @Override
    public void destroy() {
    }

}
