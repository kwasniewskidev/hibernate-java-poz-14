import org.hibernate.Session;
import utils.HibernateUtil;

public class HibernateApplication {

    public static void main(String... args) {
        Session session = estabilishConnection();
        session.beginTransaction();

        //tutaj wywyołujemy kolejne zadania
        TaskRunner.selectEmployee(session);

        session.getTransaction().commit();
        session.close();
        System.exit(0);
    }

    private static Session estabilishConnection() {
        Session session = HibernateUtil.createSession();
        if (session != null) {
            System.out.println("Uzyskano połączenie");
        } else {
            System.out.println("Nie udało się uzyskać połączenia");
        }
        return session;
    }
}
