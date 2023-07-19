/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.inci.bmsanbar.services;

import az.inci.bmsanbar.model.Doc;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author User
 */

@SuppressWarnings("unchecked")
@Service
public class DocService extends AbstractService
{
    public Doc getPickDocByTrxNo(String trxNo, String pickUser)
    {
        Query q = em.createNativeQuery("EXEC DBO.SP_TERMINAL_GET_PICK_DOC ?, ?");
        q.setParameter(1, trxNo);
        q.setParameter(2, pickUser);
        Doc doc = new Doc();
        List<Object[]> resultList = q.getResultList();
        if(resultList.size() > 0)
        {
            Object[] result = resultList.get(0);
            doc.setTrxNo(String.valueOf(result[0]));
            doc.setTrxDate(String.valueOf(result[1]));
            doc.setPickArea(String.valueOf(result[2]));
            doc.setDescription(String.valueOf(result[3]));
            doc.setItemCount(Integer.parseInt(String.valueOf(result[4])));
            doc.setPrevTrxNo(String.valueOf(result[5]));
            doc.setPickUser(String.valueOf(result[6]));
            doc.setWhsCode(String.valueOf(result[7]));
        }

        em.close();

        return doc;
    }

    public Doc getPackDocByTrxNo(String trxNo)
    {
        Query q = em.createNativeQuery("EXEC DBO.SP_TERMINAL_GET_PACK_DOC ?");
        q.setParameter(1, trxNo);
        Doc doc = new Doc();
        List<Object[]> resultList = q.getResultList();
        if(resultList.size() > 0)
        {
            Object[] result = resultList.get(0);
            doc.setTrxNo(String.valueOf(result[0]));
            doc.setTrxDate(String.valueOf(result[1]));
            doc.setDescription(String.valueOf(result[2]));
            doc.setBpCode(String.valueOf(result[3]));
            doc.setBpName(String.valueOf(result[4]));
            doc.setSbeCode(String.valueOf(result[5]));
            doc.setSbeName(String.valueOf(result[6]));
            doc.setPrevTrxNo(String.valueOf(result[7]));
            doc.setApproveUser(String.valueOf(result[8]));
            doc.setNotes(String.valueOf(result[9]));
            doc.setWhsCode(String.valueOf(result[10]));
        }

        em.close();

        return doc;
    }

    public List<Doc> getPackDocList(String userId)
    {
        Query q = em.createNativeQuery("EXEC DBO.SP_TERMINAL_GET_PACK_DOC_ALL ?");
        q.setParameter(1, userId);
        List<Doc> docList = new ArrayList<>();
        List<Object[]> resultList = q.getResultList();
        resultList.stream().map((result)->
                                {
                                    Doc doc = new Doc();
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

    @Transactional
    public List<Doc> getApproveDocList()
    {
        Query q = em.createNativeQuery(
                "SELECT SYSTEM_NO, dbo.fnFormatDate(SYSTEM_DATE, 'dd-mm-yyyy'),"
                + " NOTES FROM TERMINAL_APPROVE"
                + " WHERE STATUS=0 GROUP BY SYSTEM_NO, SYSTEM_DATE, NOTES");
        List<Doc> docList = new ArrayList<>();
        List<Object[]> resultList = q.getResultList();
        resultList.stream().map((result)->
                                {
                                    Doc doc = new Doc();
                                    doc.setTrxNo(String.valueOf(result[0]));
                                    doc.setTrxDate(String.valueOf(result[1]));
                                    doc.setNotes(String.valueOf(result[2]));
                                    return doc;
                                }).forEachOrdered(docList::add);

        q = em.createNativeQuery("UPDATE TERMINAL_APPROVE SET STATUS=1 WHERE STATUS=0");
        try
        {
            q.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        em.close();

        return docList;
    }

    @Transactional
    public boolean resetPickDoc(String trxNo, String userId)
    {
        if(!isResetPermitted(userId))
            return false;

        Query q = em.createNativeQuery(
                "UPDATE INV_PICK_TRX SET  PICK_STATUS = N'R', PICK_USER_ID = NULL WHERE TRX_NO=? AND PICK_USER_ID=?");
        q.setParameter(1, trxNo);
        q.setParameter(2, userId);

        try
        {
            q.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }

        em.close();

        return true;
    }

    public boolean isResetPermitted(String id)
    {
        Query q = em.createNativeQuery(
                "SELECT dbo.FN_GET_DYN_SETTING ('ALLOW_PICK_RESET', ?)");
        q.setParameter(1, id);
        boolean permitted = false;
        List<Object> result = q.getResultList();

        if(result.size() == 1)
        {
            if(String.valueOf(result.get(0)).equals("1"))
                permitted = true;
        }

        em.close();

        return permitted;
    }

    public boolean shipmentIsValid(String trxNo)
    {
        Query q = em.createNativeQuery("SELECT * FROM SHIPMENT_DOC_PREFIX WHERE TRX_NO=?");
        q.setParameter(1, trxNo.substring(0, 3));
        List<Object[]> resultList = q.getResultList();
        return resultList.size() > 0;
    }

    public boolean isTaxed(String trxNo)
    {
        Query q = em.createNativeQuery("SELECT TAXED_FLAG FROM ACC_DOC WHERE TRX_NO=?");
        q.setParameter(1, trxNo);
        List<Object> resultList = q.getResultList();
        if(resultList.size() > 0)
            return Boolean.parseBoolean(String.valueOf(resultList.get(0)));

        em.close();

        return false;
    }
}