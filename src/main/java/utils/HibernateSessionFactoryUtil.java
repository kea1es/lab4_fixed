package utils;

import beansLab.entities.Shot;
import beansLab.entities.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;
    private static final String CONFIG_FILE = "hibernate.cfg.xml";

    private HibernateSessionFactoryUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                configuration.configure(CONFIG_FILE);

                // Add annotated classes
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Shot.class);

                // Apply properties
                configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
                configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

                // Build session factory
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);

                System.out.println("Hibernate SessionFactory created successfully");
            } catch (Exception e) {
                System.err.println("Error creating Hibernate SessionFactory: " + e.getMessage());
                e.printStackTrace();
                throw new ExceptionInInitializerError(e);
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("Hibernate SessionFactory closed");
        }
    }
}