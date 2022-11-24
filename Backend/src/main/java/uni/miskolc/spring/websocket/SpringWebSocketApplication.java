package uni.miskolc.spring.websocket;

import uni.miskolc.spring.websocket.database.Words;
import uni.miskolc.spring.websocket.database.WordsRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringWebSocketApplication {

	@Autowired
	WordsRepository wordsRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringWebSocketApplication.class, args);
 	}

	@Bean
	InitializingBean sendDatabase() {
		return () -> {
			wordsRepository.save(new Words("Olló"));
			wordsRepository.save(new Words("Autó"));
			wordsRepository.save(new Words("Egér"));
			wordsRepository.save(new Words("Asztal"));
			wordsRepository.save(new Words("Laptop"));
			wordsRepository.save(new Words("Ablak"));
			wordsRepository.save(new Words("Függöny"));
			wordsRepository.save(new Words("Ágy"));
			wordsRepository.save(new Words("Párna"));
			wordsRepository.save(new Words("Pizza"));
			wordsRepository.save(new Words("Mező"));
			wordsRepository.save(new Words("Szín"));
			wordsRepository.save(new Words("Üveg"));
			wordsRepository.save(new Words("Víz"));
			wordsRepository.save(new Words("Pohár"));
			wordsRepository.save(new Words("Kés"));
			wordsRepository.save(new Words("Kanál"));
			wordsRepository.save(new Words("Szivárvány"));
			wordsRepository.save(new Words("Magyarország"));
			wordsRepository.save(new Words("Labda"));
			wordsRepository.save(new Words("Üvegház"));
			wordsRepository.save(new Words("Ferrari"));
		};
	}

}

