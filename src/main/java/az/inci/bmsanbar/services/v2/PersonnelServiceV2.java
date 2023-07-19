package az.inci.bmsanbar.services.v2;

import az.inci.bmsanbar.services.AbstractService;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("unchecked")
@Service
public class PersonnelServiceV2 extends AbstractService
{

    public String getPersonnelName(String perCode)
    {
        String info = "";
        Query query = em.createNativeQuery("SELECT PER_NAME FROM PER_MASTER WHERE PER_CODE=?");
        query.setParameter(1, perCode);

        List<String> result = query.getResultList();
        if(result.size() > 0)
        {
            info = result.get(0);
        }

        em.close();

        return info;
    }
}
