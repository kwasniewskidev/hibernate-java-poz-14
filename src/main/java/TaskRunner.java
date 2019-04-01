import model.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

public class TaskRunner {

    /**
     * Zamodeluj klasę będą odpowiednikiem encji Country, a następnie pobierz i wyświetl nazwę pobranego państwa
     */
    public static void printCountry(Session session) {
        Country country = (Country) session.load(Country.class, "ABW");
        System.out.println(country.getName());
    }

    /**
     * utwórz obiekt Employee, a następnie wstaw go do bazy za pomocą metod dostępnych w hibernate
     */
    public static void addEmployee(Session session) {
        HumanEmployee humanEmployee = new HumanEmployee();
        humanEmployee.setFirstname("Marcin");
        humanEmployee.setLastname("Kowalski");
        session.persist(humanEmployee);
    }

    /**
     * aktualizacja nazwiska pracownika
     */
    public static void updateEmployee(Session session) {
        HumanEmployee humanEmployee = (HumanEmployee) session.load(HumanEmployee.class, 1L);
        humanEmployee.setLastname("Nowak");
        session.merge(humanEmployee);
    }

    /**
     * pobieranie wszystkich pracowników
     */
    public static void getAllEmployess(Session session) {
        List<HumanEmployee> employees = session.createCriteria(HumanEmployee.class)
                .list();
        for (HumanEmployee employee : employees) {
            System.out.println(employee.getFirstname());
        }
    }

    /**
     * Pobieranie polskich miast z bazy
     */
    public static void getOnlyPolishCitis(Session session) {
        List<City> cities = session.createCriteria(City.class)
                .add(Restrictions.eq("countryCode", "POL"))
                .list();
        for (City city : cities) {
            System.out.println(city.getName());
        }
    }

    /**
     * pobieranie miast z populacją pomiędzy 100, a 300 tysięcy.
     */
    public static void getCitisBetween(Session session) {
        List<City> cities = session.createCriteria(City.class)
                .add(Restrictions.between("population", "100000", "300000"))
                .list();
        for (City city : cities) {
            System.out.println(city.getName());
        }
    }

    public static void getCityWithMaxPopulation(Session session) {
        City city = (City) session.createCriteria(City.class)
                .addOrder(Order.desc("population")).setMaxResults(1).uniqueResult();
        System.out.println(city.getName());
    }

    /**
     * wstawianie miasta za pomocą natywnego SQLa
     */
    public static void selectCityByHsdql(Session session) {
        String templateSql = "insert into city (Name, CountryCode, District, Population) values (:name, :countryCode, :district, :population)";
        Query qry = session.createSQLQuery(templateSql);
        qry.setParameter("name", "Gniezno");
        qry.setParameter("countryCode", "POL");
        qry.setParameter("district", "Wielkopolskie");
        qry.setParameter("population", "52000");
        int res = qry.executeUpdate();
    }

    /**
     * update miast z użyciem HQL
     */
    public static void updateCity(Session session, String name, Long id, String population) {
        String templateSql = "update model.City set name = :name, population = :population where id=:id";
        Query qry = session.createQuery(templateSql);
        qry.setParameter("name", name);
        qry.setParameter("population", population);
        qry.setParameter("id", id);
        int res = qry.executeUpdate();
    }

    /**
     * pobieranie miasta z użyciem HQL
     */
    public static void selectCityByHql(Session session) {
        String hql = "FROM City where name = :name";
        Query query = session.createQuery(hql);
        query.setParameter("name", "Kabul");
        City city = (City) query.list().get(0);
        System.out.println(city.getName());
    }

    public static void deleteEmployee(Session session) {
        HumanEmployee humanEmployee = (HumanEmployee) session.load(HumanEmployee.class, 1L);
        session.delete(humanEmployee);
    }


    /**
     * Zamodelowanie relacji One-To-One,
     * jedna tabela pracownika z podstawowymi informacjami.
     * Druga tabela ze szczegółami pracownika.
     * Utworzenie pracownika wraz z jego szczegółami z użyciem klas modelowych (odpowiedników encji bazodanowych)
     * oraz insert do bazy danych.
     */
    public static void insertEmployeeWithDetails(Session session) {

        HumanEmployeeDetails humanEmployeeDetails = new HumanEmployeeDetails();
        humanEmployeeDetails.setAgreementExpiration(new Date());
        humanEmployeeDetails.setBirthday(new Date());
        humanEmployeeDetails.setSalary(6800L);
        humanEmployeeDetails.setDepartment("WR");

        HumanEmployee humanEmployee = new HumanEmployee();
        humanEmployee.setFirstname("Wojciecj");
        humanEmployee.setLastname("Kowalski");
        session.save(humanEmployee);

        humanEmployee.setDetails(humanEmployeeDetails);
        session.save(humanEmployeeDetails);
    }

    /**
     *
     * Utworzenie szczegółów do istniejącego już pracownika, pobranie go z bazy oraz przypisanie jego szczegółów.
     * Update bazy danych
     */
    public static void insertEmployeeDetails(Session session) {
        HumanEmployeeDetails humanEmployeeDetails = new HumanEmployeeDetails();
        humanEmployeeDetails.setAgreementExpiration(new Date());
        humanEmployeeDetails.setBirthday(new Date());
        humanEmployeeDetails.setSalary(6000L);
        humanEmployeeDetails.setDepartment("HR");

        HumanEmployee humanEmployee = (HumanEmployee) session.load(HumanEmployee.class, 3L);
        humanEmployee.setDetails(humanEmployeeDetails);
        session.save(humanEmployeeDetails);
    }

    /** Relacja One-To-One
     * Pobranie pracownika wraz z jego szczegółami (zwróć uwagę na mapowanie encji)
     */
    public static void getEmployeeWithDetails(Session session) {
        HumanEmployee humanEmployee = (HumanEmployee) session.load(HumanEmployee.class, 1L);
        System.out.println(humanEmployee.getDetails().getSalary());
        System.out.println(humanEmployee.getDetails().getBirthday());
        System.out.println(humanEmployee.getDetails().getAgreementExpiration());
    }


    /**
     * Pobranie informacji z bazy danych oraz utworzenie obiektów nowej klasy dzięki HQL
     */
    public static void selectCityInfo(Session session) {
        String hql = "select new model.CityInfo( c.name, c.population)FROM City c where id = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id", 1L);
        CityInfo cityInfo = (CityInfo) query.list().get(0);
        System.out.println(cityInfo.getName());
        System.out.println(cityInfo.getPopulation());
    }


    /**
     * Select z zastosowaniem join'a
     */
    public static void selectEmployee(Session session) {
        /* Tak wyglłądałby select za pomocą zwykłego SQLa
          select d.salary from humanemployee e
          join humanemployeedetails d
          on e.employee_details_id=d.id
          where d.department = :name
         */

        // dzięki HQL możemy zrobić to prościej, powiązaniem id zajmują się klasy modelowe, a nie samo zapytanie
        String hql = "select d.salary FROM HumanEmployee e join e.details d where d.department = :name";

        Query query = session.createQuery(hql);
        query.setParameter("name", "HR");
        Long salary = (Long) query.list().get(0);
        System.out.println(salary);
    }
}
