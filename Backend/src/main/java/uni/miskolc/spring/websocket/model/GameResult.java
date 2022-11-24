package uni.miskolc.spring.websocket.model;

import java.util.Set;

public class GameResult {
    private Set<DrawPrincipal> users;
    private String result;

    public Set<DrawPrincipal> getUsers() {
        return users;
    }

    public void setUsers(Set<DrawPrincipal> users) {
        this.users = users;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
