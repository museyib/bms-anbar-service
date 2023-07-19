package az.inci.bmsanbar.services.v2;

import az.inci.bmsanbar.model.Doc;
import az.inci.bmsanbar.model.Trx;
import az.inci.bmsanbar.model.v2.CollectTrxRequest;
import az.inci.bmsanbar.services.AbstractService;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
@Service
public class PickServiceV2 extends AbstractService
{

    @Transactional
    public Doc getPickDoc(String pickUser, int mode)
    {
        List<Trx> trxList = new ArrayList<>();
        Query q = em.createNativeQuery("EXEC DBO.SP_TERMINAL_GET_PICK_ITEMS ?,?,null,null,null");
        q.setParameter(1, pickUser);
        q.setParameter(2, mode);
        List<Object[]> resultList = q.getResultList();

        resultList.stream().map((result)->
                                {
                                    Trx trx = new Trx();
                                    trx.setTrxNo((String) result[0]);
                                    trx.setTrxDate((String) result[1]);
                                    trx.setTrxId((int) result[2]);
                                    trx.setInvCode((String) result[3]);
                                    trx.setInvName((String) result[4]);
                                    trx.setInvBrand((String) result[5]);
                                    trx.setBpName((String) result[6]);
                                    trx.setSbeName((String) result[7]);
                                    trx.setWhsCode((String) result[8]);
                                    trx.setQty(Double.parseDouble(String.valueOf(result[9])));
                                    trx.setUom((String) result[10]);
                                    trx.setUomFactor(Double.parseDouble(String.valueOf(result[11])));
                                    trx.setPickArea((String) result[12]);
                                    trx.setBarcode((String) result[13]);
                                    trx.setPrevTrxNo((String) result[14]);
                                    trx.setNotes((String) result[15]);
                                    trx.setPriority((int) result[16]);
                                    trx.setPickUser(pickUser);
                                    return trx;
                                }).forEachOrdered(trxList::add);

        em.close();

        Doc doc = null;

        if(!trxList.isEmpty())
        {
            String trxNo = trxList.get(0).getTrxNo();
            doc = getPickDocByTrxNo(trxNo, pickUser);
            doc.setTrxList(trxList);
        }

        return doc;
    }

    @Transactional
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
            doc.setTrxNo((String) result[0]);
            doc.setTrxDate((String) result[1]);
            doc.setPickArea((String) result[2]);
            doc.setDescription((String) result[3]);
            doc.setItemCount((int) result[4]);
            doc.setPrevTrxNo((String) result[5]);
            doc.setPickUser((String) result[6]);
            doc.setWhsCode((String) result[7]);
        }

        em.close();

        return doc;
    }


    @Transactional
    public void collectTrx(List<CollectTrxRequest> dataList)
    {
        Query query = em.createNativeQuery("EXEC DBO.SP_TERMINAL_COLLECT_WITH_TIME ?,?,?,?");

        for(CollectTrxRequest data : dataList)
        {
            query.setParameter(1, data.getTrxId());
            query.setParameter(2, data.getQty());
            query.setParameter(3, data.getSeconds());
            query.setParameter(4, data.getPickStatus());
            query.executeUpdate();
        }

        em.close();
    }

    @Transactional
    public boolean resetPickDoc(String trxNo, String userId)
    {
        if(!resetAllowed(userId))
            return false;

        Query q = em.createNativeQuery("EXEC DBO.SP_TERMINAL_PICK_RESET ?,?");
        q.setParameter(1, trxNo);
        q.setParameter(2, userId);
        q.executeUpdate();

        em.close();

        return true;
    }

    public boolean resetAllowed(String id)
    {
        Query q = em.createNativeQuery(
                "SELECT dbo.FN_GET_DYN_SETTING ('ALLOW_PICK_RESET', ?)");
        q.setParameter(1, id);
        List<Object> result = q.getResultList();

        em.close();

        return result.size() >= 1 && String.valueOf(result.get(0)).equals("1");
    }
}
