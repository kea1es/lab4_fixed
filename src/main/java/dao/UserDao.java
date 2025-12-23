package dao;

import beansLab.entities.User;
import beansLab.entities.Shot;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utils.HibernateSessionFactoryUtil;

import java.util.List;

public class UserDao {

    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserDao.class);

    public UserDao() {
        createAllParts();
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            session.clear();
        }
    }

    public void saveUser(User user) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx1 = session.beginTransaction();
            session.save(user);
            tx1.commit();
        }
    }

    public void updateUser(User user) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx1 = session.beginTransaction();
            session.update(user);
            tx1.commit();
        }
    }

    public void deleteShots(User user) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx1 = session.beginTransaction();
            user.getShots().clear();
            updateUser(user);
            tx1.commit();
        }
    }

    public List<User> findAllUsers() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx1 = session.beginTransaction();
            List<User> users = session.createQuery("FROM User", User.class).list();
            tx1.commit();
            return users;
        }
    }


    private static String TABLE_USER_CHECK =
            "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'users')";

    private static String TABLE_SHOTS_CHECK =
            "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'shots')";

    private static String SEQ_USER_CHECK =
            "SELECT EXISTS (SELECT FROM information_schema.sequences WHERE sequence_schema = 'public' AND sequence_name = 'seq_user')";

    private static String SEQ_SHOTS_CHECK =
            "SELECT EXISTS (SELECT FROM information_schema.sequences WHERE sequence_schema = 'public' AND sequence_name = 'seq_shot')";


    private static String TABLE_USERS =
            "CREATE TABLE users (" +
                    "    user_id SERIAL PRIMARY KEY, " +
                    "    login VARCHAR(255) NOT NULL UNIQUE, " +
                    "    password VARCHAR(255) NOT NULL" +
                    ")";

    private static String TABLE_SHOTS =
            "CREATE TABLE shots (" +
                    "    shot_id SERIAL PRIMARY KEY, " +
                    "    x REAL NOT NULL, " +
                    "    y REAL NOT NULL, " +
                    "    r REAL NOT NULL, " +
                    "    rg BOOLEAN NOT NULL, " +
                    "    start_time VARCHAR(20) NOT NULL, " +
                    "    script_time BIGINT NOT NULL, " +
                    "    user_id INTEGER NOT NULL, " +
                    "    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE" +
                    ")";


    private static String SEQ_USER =
            "CREATE SEQUENCE IF NOT EXISTS seq_user START 1 INCREMENT 1";

    private static String SEQ_SHOT =
            "CREATE SEQUENCE IF NOT EXISTS seq_shot START 1 INCREMENT 1";

    private void createAllParts() {
        try {
            createTableUser();
            createTableShot();
            createSeqUser();
            createSeqShot();
        } catch (Exception execption) {
            log.error("Ошибка во время создания структуры БД", execption);
        }
    }

    private void createSeqUser() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx1 = session.beginTransaction();
            try {
                // Проверяем существование последовательности
                Query<Boolean> query = session.createNativeQuery(SEQ_USER_CHECK, Boolean.class);
                Boolean exists = query.getSingleResult();
                if (exists != null && !exists) {
                    session.createNativeQuery(SEQ_USER).executeUpdate();
                    log.info("Последовательность seq_user успешно создана");
                }
                tx1.commit();
            } catch (Exception e) {
                if (tx1 != null) tx1.rollback();
                // Если запрос не сработал, пробуем создать последовательность напрямую
                try {
                    session.createNativeQuery(SEQ_USER).executeUpdate();
                    log.info("Последовательность seq_user создана (fallback)");
                } catch (Exception ex) {
                    log.warn("Не получилось создать последовательность seq_user: " + ex.getMessage());
                }
            }
        }
    }

    private void createSeqShot() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx1 = session.beginTransaction();
            try {
                Query<Boolean> query = session.createNativeQuery(SEQ_SHOTS_CHECK, Boolean.class);
                Boolean exists = query.getSingleResult();
                if (exists != null && !exists) {
                    session.createNativeQuery(SEQ_SHOT).executeUpdate();
                    log.info("Последовательность seq_shot успешно создана");
                }
                tx1.commit();
            } catch (Exception e) {
                if (tx1 != null) tx1.rollback();
                try {
                    session.createNativeQuery(SEQ_SHOT).executeUpdate();
                    log.info("Последовательность seq_shot создана (fallback)");
                } catch (Exception ex) {
                    log.warn("Не получилось создать последовательность seq_shot: " + ex.getMessage());
                }
            }
        }
    }

    private void createTableUser() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx1 = session.beginTransaction();
            try {
                Query<Boolean> query = session.createNativeQuery(TABLE_USER_CHECK, Boolean.class);
                Boolean exists = query.getSingleResult();
                if (exists != null && !exists) {
                    session.createNativeQuery(TABLE_USERS).executeUpdate();
                    log.info("Таблица users успешно создана");
                }
                tx1.commit();
            } catch (Exception e) {
                if (tx1 != null) tx1.rollback();
                try {
                    session.createNativeQuery(TABLE_USERS).executeUpdate();
                    log.info("Таблица users создана (fallback)");
                } catch (Exception ex) {
                    log.warn("Не получилось создать таблицу users: " + ex.getMessage());
                }
            }
        }
    }

    private void createTableShot() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx1 = session.beginTransaction();
            try {
                Query<Boolean> query = session.createNativeQuery(TABLE_SHOTS_CHECK, Boolean.class);
                Boolean exists = query.getSingleResult();
                if (exists != null && !exists) {
                    session.createNativeQuery(TABLE_SHOTS).executeUpdate();
                    log.info("Таблица shots успешно создана");
                }
                tx1.commit();
            } catch (Exception e) {
                if (tx1 != null) tx1.rollback();
                try {
                    session.createNativeQuery(TABLE_SHOTS).executeUpdate();
                    log.info("Таблица shots создана (fallback)");
                } catch (Exception ex) {
                    log.warn("Не получилось создать таблицу shots: " + ex.getMessage());
                }
            }
        }
    }
}