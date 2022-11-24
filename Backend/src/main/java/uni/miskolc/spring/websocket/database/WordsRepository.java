package uni.miskolc.spring.websocket.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface WordsRepository extends JpaRepository<Words,Long> {
    List<Words> findAll();

}
