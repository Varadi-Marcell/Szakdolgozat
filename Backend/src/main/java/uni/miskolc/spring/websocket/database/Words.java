package uni.miskolc.spring.websocket.database;

import javax.persistence.*;

@Entity
@Table(name = "words")
public class Words {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String word;

    public Words(String word) {
        this.word = word;
    }

    public Words() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
