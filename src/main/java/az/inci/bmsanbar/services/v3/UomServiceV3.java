package az.inci.bmsanbar.services.v3;

import az.inci.bmsanbar.model.Uom;
import az.inci.bmsanbar.services.AbstractService;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UomServiceV3 extends AbstractService
{
    @SuppressWarnings("unchecked")
    public List<Uom> getUomList()
    {
        List<Uom> uomList = new ArrayList<>();
        Query query = em.createNativeQuery("SELECT UOM, UNIT_NAME FROM UOM");

        List<Object[]> resultList = query.getResultList();

        resultList.stream().map(result->
                                {
                                    Uom uom = new Uom();
                                    uom.setUomCode(String.valueOf(result[0]));
                                    uom.setUomName(String.valueOf(result[1]));
                                    return uom;
                                }).forEachOrdered(uomList::add);

        em.close();

        return uomList;
    }
}
