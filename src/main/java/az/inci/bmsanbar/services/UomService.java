package az.inci.bmsanbar.services;

import az.inci.bmsanbar.model.Uom;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UomService extends AbstractService
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
