package uni.miskolc.spring.websocket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import uni.miskolc.spring.websocket.model.*;
import uni.miskolc.spring.websocket.database.Words;
import uni.miskolc.spring.websocket.database.WordsRepository;
import uni.miskolc.spring.websocket.exceptions.DrawPermissionException;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import uni.miskolc.spring.websocket.model.GlobalSessionId;

import javax.naming.NoPermissionException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final WordsRepository wordsRepository;
    private final List<Words> wordsList = new ArrayList<>();
    private final Round round = new Round(1,0);
    private String chosenWord;
    public Set<DrawPrincipal> usersHashSet = new HashSet<>();

    @Autowired
    public GameService(SimpMessagingTemplate simpMessagingTemplate, WordsRepository wordsRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.wordsRepository = wordsRepository;
    }

    public String drawWords(){
        wordsList.clear();
        Random rand = new Random();

        for (int i = 0; i < 3; i++) {
            Words word = this.wordsRepository.findAll().get(rand.nextInt(this.wordsRepository.findAll().size()));
            wordsList.add(word);
            this.wordsRepository.delete(word);
        }

        return wordsList.stream().map(s -> s.getWord().toLowerCase()).collect(Collectors.joining(" "));
    }

    public Set<DrawPrincipal> setName(DrawPrincipal principal, String payload) {
        String username = JsonPath.read(payload, "$.name");

        principal.setUsername(username);
        usersHashSet.add(principal);

        return this.usersHashSet;
    }

    public void resultOfTheGame(){
        GameResult game = new GameResult();
        ObjectMapper mapper = new ObjectMapper();
        game.setUsers(this.usersHashSet);

        int max = Collections.max(this.usersHashSet.stream().map(DrawPrincipal::getPoints).collect(Collectors.toList()));

       if(this.usersHashSet.stream().filter(principal -> principal.getPoints() == max).count()>=2){
           game.setResult("Döntetlen");
           String response = "";
           try {
               response = mapper.writeValueAsString(game);
           } catch (Exception e) {
               e.printStackTrace();
           }
           simpMessagingTemplate.convertAndSend("/topic/game-over", response);
       } else {
           String winningplayer = this.usersHashSet.stream().max(Comparator.comparing(DrawPrincipal::getPoints)).get().getUsername();
           game.setResult(winningplayer);
           String response = "";
           try {
               response = mapper.writeValueAsString(game);
           } catch (Exception e) {
               e.printStackTrace();
           }
           simpMessagingTemplate.convertAndSend("/topic/game-over", response);
       }

        round.resetGame();
    }

    public void initRound(){
        round.setCounter(0);
        chosenWord = null;
        //Körök beállítása
        if (round.getRound() == 3){
            resultOfTheGame();
            return;
        }

        this.usersHashSet.forEach(DrawPrincipal::enableUser);

        DrawPrincipal temp = this.usersHashSet.stream()
                .filter(a -> a.getDrawCounter()< round.getRound())
                .findFirst().get();

        GlobalSessionId.setGlobalSession(temp.getId());
        simpMessagingTemplate.convertAndSend("/topic/disable-button", true);
        simpMessagingTemplate.convertAndSend("/topic/next-round", "");
        simpMessagingTemplate.convertAndSendToUser(temp.getId(), "/topic/words", drawWords());
        simpMessagingTemplate.convertAndSend("/topic/drawpermission", temp.getUsername()+" szót választ");

        synchronized (this){
            while (chosenWord == null){
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        simpMessagingTemplate.convertAndSend("/topic/hint-help", chosenWord.length());


        simpMessagingTemplate.convertAndSend("/topic/drawpermission", "Indul!");

        this.usersHashSet.stream().filter(principal -> principal.getId().equals(temp.getId())).findFirst().get().increaseDrawCounter();

        increaseRound();

    }

    public void increaseRound() {
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0L;

        while (elapsedTime < 40*1000) {
            elapsedTime = (new Date()).getTime() - startTime;

            if (round.getCounter() == this.usersHashSet.size()-1){
                break;
            }
        }
            boolean temp = usersHashSet.stream().allMatch(principal -> principal.getDrawCounter() == round.getRound());
            if (temp) {
                round.increaseRound();

            }
            initRound();
    }

    public Hint sendHints(DrawPrincipal user, Hint message) throws Exception {
        if(GlobalSessionId.getGlobalSession()!=null) {
            if (GlobalSessionId.getGlobalSession().equals(user.getId())) {
                simpMessagingTemplate.convertAndSendToUser(user.getId(), "/topic/private", " Rajzolás közben nem használhatod a chatet!");
                 throw new Exception("Rajzolás közben nem használhatod a chatet!");
            }

            if (!GlobalSessionId.getGlobalSession().equals(user.getId()) && user.isDisabled()) {
                throw new NoPermissionException("Várj a következő körig!");
            }
        }

        if (message.getHint().toLowerCase().equals(chosenWord)) {
            simpMessagingTemplate.convertAndSendToUser(user.getId(), "/topic/private", " Kitaláltad a szót!");
            user.increasePoints();
            user.disableUser();
            simpMessagingTemplate.convertAndSend("/topic/users", this.usersHashSet);

            round.increaseCounter();
            throw new Exception("Kitalálta a user");
        }

        return new Hint(message.getHint());
    }

    public Coordinates sendCoordinates(Coordinates coordinates, DrawPrincipal user) throws Exception {
        if (!GlobalSessionId.getGlobalSession().equals(user.getId())) {
            throw new DrawPermissionException("Nincs joga rajzolni");
        } else {
            return coordinates;
        }
    }

    public void setWord(Word msg) {
        synchronized (this){
            chosenWord = msg.getWord().toLowerCase();
            notifyAll();
        }

    }

    public void removeSession(AbstractSubProtocolEvent abstractSubProtocolEvent) {
        this.usersHashSet.remove(abstractSubProtocolEvent.getUser());
        simpMessagingTemplate.convertAndSend("/topic/users", this.usersHashSet);
    }

    public void saveSession(SessionConnectedEvent event) {
        this.usersHashSet.add((DrawPrincipal) event.getUser());
    }


}
