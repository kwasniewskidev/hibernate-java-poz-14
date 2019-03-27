import model.City;
import org.hibernate.Session;
import utils.HibernateUtil;

public class HibernateApplication {

    public static void main(String... args) {
        Session session = HibernateUtil.createSession();
        if (session != null) {
            System.out.println("Uzyskano połączenie");
        }
        else {
            System.out.println("Nie udało się uzyskać połączenia");
        }

        session.beginTransaction();
        City city = (City) session.load(City.class,1L);
        System.out.println(city.getName());
        System.exit(0);
    }
}