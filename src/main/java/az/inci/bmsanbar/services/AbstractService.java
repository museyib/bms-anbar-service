package az.inci.bmsanbar.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractService {
    protected EntityManagerFactory emf;
    protected EntityManager em;

    @Autowired
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Autowired
    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }
}
