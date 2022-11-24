package uni.miskolc.spring.websocket.handshakeHandler;

import uni.miskolc.spring.websocket.service.GameService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;

@Component
public class SessionDisconnectEventListener implements ApplicationListener<AbstractSubProtocolEvent> {

    private GameService gameService;

    public SessionDisconnectEventListener(GameService gameService) {
        this.gameService = gameService;
    }


    @Override
    public void onApplicationEvent(AbstractSubProtocolEvent abstractSubProtocolEvent) {
        gameService.removeSession(abstractSubProtocolEvent);

    }
}
