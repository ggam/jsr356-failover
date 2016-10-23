package es.guillermogonzalezdeaguero.websocketsessionreplication;

import java.io.IOException;
import java.util.Map;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Guillermo González de Agüero
 */
@ServerEndpoint(value = SampleEndpoint.PATH, configurator = EndpointConfig.class)
public class SampleEndpoint {

    public static final String PATH = "/sample-endpoint";

    private static final String CONNECTION_TIME = "connection_time";

    @OnOpen
    public void onOpen(Session session) throws IOException {
        Map<String, Object> distributableProperties = (Map<String, Object>) session.getUserProperties().get(EndpointConfig.DISTRIBUTABLE_PROPERTIES_KEY);

        distributableProperties.putIfAbsent(CONNECTION_TIME, System.currentTimeMillis());

        session.getBasicRemote().sendText("Connection opened at " + distributableProperties.get(CONNECTION_TIME));
    }

    @OnMessage
    public void onMessage(String message, Session session) {

    }
}
