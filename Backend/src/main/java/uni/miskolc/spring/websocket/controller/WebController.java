package uni.miskolc.spring.websocket.controller;

import uni.miskolc.spring.websocket.model.Coordinates;
import uni.miskolc.spring.websocket.model.DrawPrincipal;
import uni.miskolc.spring.websocket.model.Hint;
import uni.miskolc.spring.websocket.model.Word;
import uni.miskolc.spring.websocket.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Set;


@Controller
public class WebController {
	private final GameService gameService;

	@Autowired
	public WebController(GameService gameService){
		this.gameService = gameService;
	}

	@MessageMapping("/selected-word")
	public void setWord (Word msg){
		this.gameService.setWord(msg);
	}

	@MessageMapping("/start-game")
	public void setGame(){
		this.gameService.initRound();
	}

	@MessageMapping("/sendCoordinates")
	@SendTo("/topic/coordinates")
	public Coordinates sendCoordinates(Coordinates coordinates, DrawPrincipal user) throws Exception {
		return this.gameService.sendCoordinates(coordinates,user);

	}

	@MessageMapping("/recieve-hint")
	@SendTo("/topic/hint")
	public Hint sendMessageToChat(DrawPrincipal user, Hint message) throws Exception {
		return this.gameService.sendHints(user,message);
	}

	@MessageMapping(value = "/set-name")
	@SendTo("/topic/users")
	public Set<DrawPrincipal> setName(DrawPrincipal principal, String payload){
		return this.gameService.setName(principal,payload);
	}

}


