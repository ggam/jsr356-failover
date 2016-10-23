package es.guillermogonzalezdeaguero.websocketsessionreplication;

import static es.guillermogonzalezdeaguero.websocketsessionreplication.EndpointFilter.COOKIE_WEBSOCKET_SESSION_UUID;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 *
 * @author Guillermo González de Agüero
 */
public class EndpointConfig extends ServerEndpointConfig.Configurator {

    private static final String HTTP_SESSION_WEBSOCKET_SESSION_PROPERTIES_ATTR = "es.guillermogonzalezdeaguero.wesocket_session_properties";

    public static final String DISTRIBUTABLE_PROPERTIES_KEY = "es.guillermogonzalezdeaguero.websocket_distributable_properties";

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        List<String> cookieHeaders = request.getHeaders().getOrDefault("cookie", new ArrayList<>());

        String uuid = null;
        boolean cookieFound = false;
        if (!cookieHeaders.isEmpty()) {
            List<HttpCookie> parsedCookies = HttpCookie.parse(cookieHeaders.get(0));

            for (HttpCookie parsedCookie : parsedCookies) {
                if (COOKIE_WEBSOCKET_SESSION_UUID.equals(parsedCookie.getName())) {
                    uuid = parsedCookie.getValue();
                    cookieFound = true;
                    break;
                }
            }
        }

        if (!cookieFound) {
            uuid = UUID.randomUUID().toString();

            List<String> uuidHeader = new ArrayList<>();
            uuidHeader.add(uuid);

            response.getHeaders().put(COOKIE_WEBSOCKET_SESSION_UUID, uuidHeader);
        }

        HttpSession httpSession = (HttpSession) request.getHttpSession();

        Map<String, Map<String, Object>> attributes = (Map<String, Map<String, Object>>) httpSession.getAttribute(HTTP_SESSION_WEBSOCKET_SESSION_PROPERTIES_ATTR);
        if (attributes == null) {
            attributes = new HashMap<>();
            httpSession.setAttribute(HTTP_SESSION_WEBSOCKET_SESSION_PROPERTIES_ATTR, attributes);
        }

        Map<String, Object> distributablePropertiesForThisHandshake = attributes.get(uuid);
        if (distributablePropertiesForThisHandshake == null) {
            distributablePropertiesForThisHandshake = new HashMap<>();
            attributes.put(uuid, distributablePropertiesForThisHandshake);
        }

        sec.getUserProperties().put(DISTRIBUTABLE_PROPERTIES_KEY, distributablePropertiesForThisHandshake);
    }

}
