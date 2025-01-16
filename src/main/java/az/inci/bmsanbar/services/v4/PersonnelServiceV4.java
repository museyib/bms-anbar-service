package az.inci.bmsanbar.services.v4;

import az.inci.bmsanbar.services.AbstractService;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonnelServiceV4 extends AbstractService
{
    public String getPersonnelName(String perCode)
    {
        String info = "";
        Query query = em.createNativeQuery("SELECT PER_NAME FROM PER_MASTER WHERE PER_CODE = :PER_CODE");
        query.setParameter("PER_CODE", perCode);

        List<String> result = query.getResultList();
        if(!result.isEmpty())
        {
            info = result.get(0);
        }

        em.close();

        return info;
    }
}
