package ru.skillbox.blog_engine.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import ru.skillbox.blog_engine.model.*;

public class HibernateSessionFactoryUtil {

    private static SessionFactory sessionFactory;

    private HibernateSessionFactoryUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
//                configuration.addAnnotatedClass(Post.class);
//                configuration.addAnnotatedClass(PostComment.class);
//                configuration.addAnnotatedClass(PostVote.class);
//                configuration.addAnnotatedClass(Tag.class);
//                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(GlobalSetting.class);

                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
