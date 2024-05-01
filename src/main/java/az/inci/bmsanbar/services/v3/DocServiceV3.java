/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.inci.bmsanbar.services.v3;

import az.inci.bmsanbar.model.PickDoc;
import az.inci.bmsanbar.services.AbstractService;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.ParameterMode.IN;

/**
 * @author User
 */

@SuppressWarnings("unchecked")
@Service
public class DocServiceV3 extends AbstractService
{
    public List<PickDoc> getPackDocList(String userId)
    {
        StoredProcedureQuery q = em.createStoredProcedureQuery("SP_TERMINAL_GET_PACK_DOC_ALL");
        q.registerStoredProcedureParameter("USER_ID", String.class, IN);
        q.setParameter("USER_ID", userId);
        List<PickDoc> docList = new ArrayList<>();
        List<Object[]> resultList = q.getResultList();
        resultList.stream().map((result)->
                                {
                                    PickDoc doc = new PickDoc();
                                    doc.setTrxNo(String.valueOf(result[0]));
                                    doc.setTrxDate(String.valueOf(result[1]));
                                    doc.setDescription(String.valueOf(result[2]));
                                    doc.setBpCode(String.valueOf(result[3]));
                                    doc.setBpName(String.valueOf(result[4]));
                                    doc.setSbeCode(String.valueOf(result[5]));
                                    doc.setSbeName(String.valueOf(result[6]));
                                    doc.setPrevTrxNo(String.valueOf(result[7]));
                                    doc.setNotes(String.valueOf(result[8]));
                                    doc.setItemCount(Integer.parseInt(String.valueOf(result[9])));
                                    doc.setPickedItemCount(Integer.parseInt(String.valueOf(result[10])));
                                    return doc;
                                }).forEachOrdered(docList::add);

        em.close();

        return docList;
    }

    public boolean isTaxed(String trxNo)
    {
        Query q = em.createNativeQuery("SELECT TAXED_FLAG FROM ACC_DOC WHERE TRX_NO = :TRX_NO");
        q.setParameter("TRX_NO", trxNo);
        List<Object> resultList = q.getResultList();
        if(resultList.size() > 0)
            return Boolean.parseBoolean(String.valueOf(resultList.get(0)));

        em.close();

        return false;
    }
}