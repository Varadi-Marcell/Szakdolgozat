package uni.miskolc.spring.websocket.config;

import uni.miskolc.spring.websocket.handshakeHandler.UserHandshakeHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/uni");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry
		.addEndpoint("/stomp-endpoint")
		.setHandshakeHandler(new UserHandshakeHandler())
		.setAllowedOrigins("http://localhost:4200")
		.withSockJS();
	}


}
