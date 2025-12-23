package resources;
import beansLab.entities.User;

public class UserInf {
    private String login;
    private long userId;
    private int token;
    private String sessionId;

    public String getLogin() {
        return login;
    }

    public String getSessionId() {
        return sessionId;
    }

    public int getToken() {
        return token;
    }

    public long getUserId() {
        return userId;
    }

    public UserInf(User user, String sessionId) {
        this.login = user.getLogin();
        this.userId = user.getId();
        this.sessionId = sessionId;
        this.token = (sessionId + user.getId()).hashCode();
    }
}