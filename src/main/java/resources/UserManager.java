package resources;

import beansLab.entities.Shot;
import beansLab.entities.User;
import dao.UserDao;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Startup
@Singleton
@AccessTimeout(value = 30, unit = TimeUnit.SECONDS)
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class UserManager {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserManager.class);

    private UserDao usersDao;
    private final HashMap<String, UserInf> activeSessions = new HashMap<>();

    @PostConstruct
    public void init() {
        usersDao = new UserDao();
        log.info("UserManager initialized");
    }

    @Lock(LockType.READ)
    public int checkUser(String login, String password) {
        User user = usersDao.findUserByLogin(login);
        if (user == null) {
            return 1;
        }

        User userWithPassword = usersDao.findUserByLoginAndPassword(login, password);
        return userWithPassword != null ? 0 : 2;
    }

    @Lock(LockType.WRITE)
    public UserInf loginUser(String login, String password, HttpSession session) {
        try {
            User user = usersDao.findUserByLoginAndPassword(login, password);
            if (user == null) {
                log.warn("Login failed for user: {}", login);
                return null;
            }

            User userWithShots = usersDao.findUserWithShots(login);
            if (userWithShots == null) {
                userWithShots = user;
            }

            session.setAttribute("User", userWithShots);
            UserInf userInf = new UserInf(userWithShots, session.getId());
            activeSessions.put(session.getId(), userInf);

            log.info("User {} logged in. Active sessions: {}", login, activeSessions.size());
            return userInf;
        } catch (Exception e) {
            log.error("Login error for user {}: {}", login, e.getMessage());
            return null;
        }
    }

    @Lock(LockType.WRITE)
    public void endSession(HttpSession session) {
        String sessionId = session.getId();
        UserInf userInf = activeSessions.get(sessionId);
        if (userInf != null) {
            log.info("Ending session for user: {}", userInf.getUser().getLogin());
        }
        activeSessions.remove(sessionId);
        session.removeAttribute("User");
        session.invalidate();
        log.debug("Session ended. Active sessions: {}", activeSessions.size());
    }

    @Lock(LockType.WRITE)
    public boolean addUser(User user) {
        boolean result = usersDao.saveUserIfNotExists(user);
        if (result) {
            log.info("New user registered: {}", user.getLogin());
        } else {
            log.warn("Registration failed: user {} already exists", user.getLogin());
        }
        return result;
    }

    @Lock(LockType.WRITE)
    public void addShotToUser(User user, Shot shot) {
        usersDao.addShotToUser(user.getLogin(), shot);
        log.debug("Shot added to user: {}", user.getLogin());
    }

    @Lock(LockType.READ)
    public boolean hasSession(HttpSession session, Cookie tokenCookie) {
        if (session == null || tokenCookie == null) {
            return false;
        }

        String sessionId = session.getId();
        UserInf userInf = activeSessions.get(sessionId);

        if (userInf == null) {
            log.warn("No session found for ID: {}", sessionId);
            return false;
        }

        try {
            int cookieToken = Integer.parseInt(tokenCookie.getValue());
            boolean isValid = userInf.getToken() == cookieToken;

            if (!isValid) {
                log.warn("Invalid token for session: {}", sessionId);
            }

            return isValid;
        } catch (NumberFormatException e) {
            log.warn("Invalid token format for session: {}", sessionId);
            return false;
        }
    }
}