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

    public User findUserById(long id) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx1 = session.beginTransaction();
            User user = session.get(User.class, id);
            tx1.commit();
            return user;
        }
    }

    public User findUserByLogin(String login) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx1 = session.beginTransaction();
            Query<User> query = session.createQuery("FROM User WHERE login = :login", User.class);
            query.setParameter("login", login);
            User user = query.uniqueResult();
            tx1.commit();
            return user;
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

    public void deleteUser(User user) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx1 = session.beginTransaction();
            session.delete(user);
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

    public Shot findShotById(long id) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx1 = session.beginTransaction();
            Shot shot = session.get(Shot.class, id);
            tx1.commit();
            return shot;
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


    private static String tableUserCheck =
            "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'users')";

    private static String tableShotsCheck =
            "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'shots')";

    private static String seqUserCheck =
            "SELECT EXISTS (SELECT FROM information_schema.sequences WHERE sequence_schema = 'public' AND sequence_name = 'seq_user')";

    private static String seqShotCheck =
            "SELECT EXISTS (SELECT FROM information_schema.sequences WHERE sequence_schema = 'public' AND sequence_name = 'seq_shot')";


    private static String tableUsers =
            "CREATE TABLE users (" +
                    "    user_id SERIAL PRIMARY KEY, " +
                    "    login VARCHAR(255) NOT NULL UNIQUE, " +
                    "    password VARCHAR(255) NOT NULL" +
                    ")";

    private static String tableShots =
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


    private static String seqUser =
            "CREATE SEQUENCE IF NOT EXISTS seq_user START 1 INCREMENT 1";

    private static String seqShot =
            "CREATE SEQUENCE IF NOT EXISTS seq_shot START 1 INCREMENT 1";

    private void createAllParts() {
        try {
            createTableUser();
            createTableShot();
            createSeqUser();
            createSeqShot();
        } catch (Exception ex) {
            log.error("Error creating database schema", ex);
        }
    }

    private void createSeqUser() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx1 = session.beginTransaction();
            try {
                Query<Boolean> query = session.createNativeQuery(seqUserCheck, Boolean.class);
                Boolean exists = query.getSingleResult();
                if (exists != null && !exists) {
                    session.createNativeQuery(seqUser).executeUpdate();
                    log.info("Sequence seq_user created");
                }
                tx1.commit();
            } catch (Exception e) {
                if (tx1 != null) tx1.rollback();
                // Если запрос не сработал, пробуем создать последовательность напрямую
                try {
                    session.createNativeQuery(seqUser).executeUpdate();
                    log.info("Sequence seq_user created (fallback)");
                } catch (Exception ex) {
                    log.warn("Could not create seq_user sequence: " + ex.getMessage());
                }
            }
        }
    }

    private void createSeqShot() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx1 = session.beginTransaction();
            try {
                Query<Boolean> query = session.createNativeQuery(seqShotCheck, Boolean.class);
                Boolean exists = query.getSingleResult();
                if (exists != null && !exists) {
                    session.createNativeQuery(seqShot).executeUpdate();
                    log.info("Sequence seq_shot created");
                }
                tx1.commit();
            } catch (Exception e) {
                if (tx1 != null) tx1.rollback();
                try {
                    session.createNativeQuery(seqShot).executeUpdate();
                    log.info("Sequence seq_shot created (fallback)");
                } catch (Exception ex) {
                    log.warn("Could not create seq_shot sequence: " + ex.getMessage());
                }
            }
        }
    }

    private void createTableUser() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx1 = session.beginTransaction();
            try {
                Query<Boolean> query = session.createNativeQuery(tableUserCheck, Boolean.class);
                Boolean exists = query.getSingleResult();
                if (exists != null && !exists) {
                    session.createNativeQuery(tableUsers).executeUpdate();
                    log.info("Table users created");
                }
                tx1.commit();
            } catch (Exception e) {
                if (tx1 != null) tx1.rollback();
                try {
                    session.createNativeQuery(tableUsers).executeUpdate();
                    log.info("Table users created (fallback)");
                } catch (Exception ex) {
                    log.warn("Could not create users table: " + ex.getMessage());
                }
            }
        }
    }

    private void createTableShot() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx1 = session.beginTransaction();
            try {
                Query<Boolean> query = session.createNativeQuery(tableShotsCheck, Boolean.class);
                Boolean exists = query.getSingleResult();
                if (exists != null && !exists) {
                    session.createNativeQuery(tableShots).executeUpdate();
                    log.info("Table shots created");
                }
                tx1.commit();
            } catch (Exception e) {
                if (tx1 != null) tx1.rollback();
                try {
                    session.createNativeQuery(tableShots).executeUpdate();
                    log.info("Table shots created (fallback)");
                } catch (Exception ex) {
                    log.warn("Could not create shots table: " + ex.getMessage());
                }
            }
        }
    }
}