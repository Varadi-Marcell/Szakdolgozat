package uni.miskolc.spring.websocket.handshakeHandler;

import uni.miskolc.spring.websocket.service.GameService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
public class SessionConnectedEventListener implements ApplicationListener<SessionConnectedEvent> {
    private final GameService gameService;

    public SessionConnectedEventListener(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        gameService.saveSession(event);
    }

}
