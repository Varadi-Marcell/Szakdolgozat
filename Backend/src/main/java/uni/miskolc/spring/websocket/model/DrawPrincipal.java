package uni.miskolc.spring.websocket.model;

import java.security.Principal;

public class DrawPrincipal implements Principal{

    private int points;

    private boolean disabled;

    private final String id;

    private String username;

    private int drawCounter = 0;

    public DrawPrincipal(String id) {
        this.points = 0;
        this.disabled = false;
        this.id = id;
    }

    public boolean isDisabled() {
        return disabled;
    }
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void increasePoints() {
        this.points++;
    }

    public void disableUser() {
        this.disabled = true;
    }

    public void enableUser() {
        this.disabled = false;
    }

    public String getId() {
        return id;
    }

    public int getDrawCounter() {
        return drawCounter;
    }

    public void increaseDrawCounter() {
        this.drawCounter = drawCounter+1;
    }

    @Override
    public String getName() {
        return this.id;
    }
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return this.username;
    }

    @Override
    public String toString() {
        return "{ \"id\" :" + "\""+id+ "\"" + "," + " \"username\": " +  "\"" + username +  "\"" + ","+ " \"points\" : " + points + "}";
    }
}
