package uni.miskolc.spring.websocket.model;

public class Round {
    private int Round;
    private int Counter;

    public Round(int round, int counter) {
        Round = round;
        Counter = counter;
    }

    public void resetGame(){
        this.Round = 1;
        this.Counter = 0;
    }
    public int getRound() {
        return Round;
    }

    public void setRound(int round) {
        Round = round;
    }

    public void increaseRound(){
        this.Round = this.Round+1;
    }

    public int getCounter() {
        return Counter;
    }

    public void setCounter(int counter) {
        Counter = counter;
    }
    public void increaseCounter(){
        this.Counter = this.Counter+1;
    }
}
