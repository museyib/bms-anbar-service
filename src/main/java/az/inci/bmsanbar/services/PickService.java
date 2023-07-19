package az.inci.bmsanbar.services;

import az.inci.bmsanbar.model.Doc;
import az.inci.bmsanbar.model.Trx;
import jakarta.persistence.Query;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
@Service
public class PickService extends AbstractService
{

    @Transactional
    public Doc getPickDoc(String pickUser, int mode)
    {
        List<Trx> trxList = new ArrayList<>();
        Query q = em.createNativeQuery("EXEC DBO.SP_TERMINAL_GET_PICK_ITEMS ?,?,null,null,null");
        q.setParameter(1, pickUser);
        q.setParameter(2, mode);
        List<Object[]> resultList;
        try
        {
            resultList = q.getResultList();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new Doc();
        }

        resultList.stream().map((result)->
                                {
                                    Trx trx = new Trx();
                                    trx.setTrxNo(String.valueOf(result[0]));
                                    trx.setTrxDate(String.valueOf(result[1]));
                                    trx.setTrxId(Integer.parseInt(String.valueOf(result[2])));
                                    trx.setInvCode(String.valueOf(result[3]));
                                    trx.setInvName(String.valueOf(result[4]));
                                    trx.setInvBrand(String.valueOf(result[5]));
                                    trx.setBpName(String.valueOf(result[6]));
                                    trx.setSbeName(String.valueOf(result[7]));
                                    trx.setWhsCode(String.valueOf(result[8]));
                                    trx.setQty(Double.parseDouble(String.valueOf(result[9])));
                                    trx.setUom(String.valueOf(result[10]));
                                    trx.setUomFactor(Double.parseDouble(String.valueOf(result[11])));
                                    trx.setPickArea(String.valueOf(result[12]));
                                    trx.setBarcode(String.valueOf(result[13]));
                                    trx.setPrevTrxNo(String.valueOf(result[14]));
                                    trx.setNotes(String.valueOf(result[15]));
                                    trx.setPriority(Integer.parseInt(String.valueOf(result[16])));
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

    @Transactional
    public boolean collectTrx(String data)
    {
        Query q1 = em.createNativeQuery("EXEC DBO.SP_TERMINAL_COLLECT ?,?,?");
        try
        {
            JSONArray jsonArray = new JSONArray(data);
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int trxId = jsonObject.getInt("trxId");
                double qty = jsonObject.getDouble("qty");
                String pickStatus = jsonObject.getString("pickStatus");
                q1.setParameter(1, trxId);
                q1.setParameter(2, qty);
                q1.setParameter(3, pickStatus);

                try
                {
                    q1.executeUpdate();
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                    return false;
                }
            }
        }
        catch(JSONException ex)
        {
            ex.printStackTrace();
            return false;
        }

        em.close();

        return true;
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
}
