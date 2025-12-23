package resources;

import beansLab.entities.User;

public class UserInf {

    private String login;
    private int token;
    private String sessionId;

    public String getLogin() {
        return login;
    }

    public String getSessionId() {
        return sessionId;
    }

    public int getToken(){
        return token;
    }

    public UserInf(User userIn, String sessionIdIn){
        this.login = userIn.getLogin();
        this.sessionId = sessionIdIn;
        this.token = (sessionIdIn + userIn.getId()).hashCode();
    }
}