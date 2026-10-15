package pl.lodz.p.info;

import jakarta.persistence.EntityManager;
import pl.lodz.p.info.infrastructure.adapter.DatabaseConnection;

public class Main {
    public static void main(String[] args) {
        EntityManager em = DatabaseConnection.getEntityManager();

        System.out.println("Połączono z bazą danych PostgreSQL!");
        em.close();
        DatabaseConnection.close();
    }
}