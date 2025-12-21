package resources;

import beansLab.entities.Shot;
import beansLab.entities.User;
import dao.UserDao;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Startup
@Singleton
@AccessTimeout(value = 30, unit = TimeUnit.SECONDS)
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER) // Управление потоками делегировано контейнеру
public class UserManager {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserManager.class);

    private UserDao usersDao;
    private List<User> users;
    private final HashMap<String, UserInf> goingSessions = new HashMap<>();

    public UserManager() {}

    @PostConstruct
    public void init() {
        usersDao = new UserDao();
        users = usersDao.findAllUsers(); // Загружаем пользователей из БД при старте
        log.info("Initialised user manager. Total users: {}", users.size());
    }

    /**
     * Проверка существования пользователя и корректности пароля.
     * LockType.READ позволяет нескольким потокам читать список одновременно.
     */
    @Lock(LockType.READ)
    public int checkUser(String login, String password) {
        boolean userExists = users.stream()
                .anyMatch(user -> login.equals(user.getLogin()));

        if (!userExists) return 1;

        boolean passwordMatches = users.stream()
                .anyMatch(user -> login.equals(user.getLogin()) && password.equals(user.getPassword()));

        return passwordMatches ? 0 : 2;
    }

    /**
     * Регистрация сессии пользователя.
     * LockType.WRITE гарантирует атомарность обновления карты сессий.
     */
    @Lock(LockType.WRITE)
    public UserInf loginUser(String login, String password, HttpSession session) {
        try {
            User user = getUserInternal(login, password);
            if (user == null) return null;

            session.setAttribute("User", user);
            UserInf userInf = new UserInf(user, session.getId());
            goingSessions.put(session.getId(), userInf);

            log.info("User {} logged in. Active sessions: {}", login, goingSessions.size());
            return userInf;
        } catch (Exception e) {
            log.error("Login error for user {}: {}", login, e.getMessage());
            return null;
        }
    }

    /**
     * Завершение сессии.
     * Удаляет данные из оперативной памяти и инвалидирует сессию.
     */
    @Lock(LockType.WRITE)
    public void endSession(HttpSession session) {
        Object userObj = session.getAttribute("User");
        if (userObj instanceof User) {
            log.info("Ending session for user: {}", ((User) userObj).getLogin());
        }
        goingSessions.remove(session.getId());
        session.removeAttribute("User");
        session.invalidate();
    }

    /**
     * Вспомогательный метод поиска.
     */
    private User getUserInternal(String login, String password) {
        return users.stream()
                .filter(user -> login.equals(user.getLogin()) && password.equals(user.getPassword()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Добавление нового пользователя.
     * Блокирует чтение и запись до завершения транзакции в БД.
     */
    @Lock(LockType.WRITE)
    public boolean addUser(User user) {
        boolean exists = users.stream()
                .anyMatch(u -> user.getLogin().equals(u.getLogin()));

        if (exists) {
            log.warn("Registration failed: user {} already exists", user.getLogin());
            return false;
        }

        users.add(user);
        usersDao.saveUser(user); // Сохранение через Hibernate
        log.info("New user registered: {}", user.getLogin());
        return true;
    }

    /**
     * Привязка нового выстрела к пользователю.
     */
    @Lock(LockType.WRITE)
    public void addShotToUser(User user, Shot shot) {
        user.addShot(shot);
        usersDao.updateUser(user);
        log.debug("Shot added to user: {}", user.getLogin());
    }

    /**
     * Валидация сессии по ID и токену из куки.
     */
    @Lock(LockType.READ)
    public boolean hasSession(HttpSession session, Cookie tokenCookie) {
        String sessionId = session.getId();
        UserInf userInf = goingSessions.get(sessionId);

        if (userInf == null) {
            log.warn("No session found for ID: {}", sessionId);
            return false;
        }

        try {
            int cookieToken = Integer.parseInt(tokenCookie.getValue());
            return userInf.getToken() == cookieToken;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Удаление всех выстрелов пользователя.
     */
    @Lock(LockType.WRITE)
    public void clearUser(User user) {
        usersDao.deleteShots(user);
        user.getShots().clear(); // Очистка коллекции в памяти для синхронизации с БД
        log.info("All shots cleared for user: {}", user.getLogin());
    }

    /**
     * Получение количества активных сессий.
     */
    @Lock(LockType.READ)
    public int getActiveSessionsCount() {
        return goingSessions.size();
    }
}