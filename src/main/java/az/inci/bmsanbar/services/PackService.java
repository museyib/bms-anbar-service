package az.inci.bmsanbar.services;

import az.inci.bmsanbar.model.PickDoc;
import az.inci.bmsanbar.model.PickTrx;
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
public class PackService extends AbstractService
{

    @Transactional
    public PickDoc getPackDoc(String approveUser, int mode)
    {
        List<PickTrx> trxList = new ArrayList<>();
        Query q = em.createNativeQuery("EXEC DBO.SP_TERMINAL_GET_PACK_ITEMS ?,?,null,null,null");
        q.setParameter(1, approveUser);
        q.setParameter(2, mode);
        List<Object[]> resultList;
        try
        {
            resultList = q.getResultList();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new PickDoc();
        }

        resultList.stream().map((result)->
                                {
                                    PickTrx trx = new PickTrx();
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
                                    trx.setPickedQty(Double.parseDouble(String.valueOf(result[10])));
                                    trx.setUom(String.valueOf(result[11]));
                                    trx.setUomFactor(Double.parseDouble(String.valueOf(result[12])));
                                    trx.setPickArea(String.valueOf(result[13]));
                                    trx.setPickUser(String.valueOf(result[14]));
                                    trx.setBarcode(String.valueOf(result[15]));
                                    trx.setPrevTrxNo(String.valueOf(result[16]));
                                    trx.setNotes(String.valueOf(result[17]));
                                    trx.setApproveUser(approveUser);
                                    return trx;
                                }).forEachOrdered(trxList::add);

        em.close();

        PickDoc doc = null;

        if(!trxList.isEmpty())
        {
            String trxNo = trxList.get(0).getTrxNo();
            doc = getPackDocByTrxNo(trxNo);
            doc.setTrxList(trxList);
        }

        em.close();

        return doc;
    }

    @Transactional
    public PickDoc getPackDocByTrxNo(String trxNo)
    {
        Query q = em.createNativeQuery("EXEC DBO.SP_TERMINAL_GET_PACK_DOC ?");
        q.setParameter(1, trxNo);
        PickDoc doc = new PickDoc();
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

    @Transactional
    public boolean collectTrx(String data, String trxNo)
    {
        Query q1 = em.createNativeQuery("EXEC DBO.SP_TERMINAL_COLLECT ?,?,NULL");
        try
        {
            JSONArray jsonArray = new JSONArray(data);
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int trxId = jsonObject.getInt("trxId");
                double qty = jsonObject.getDouble("qty");
                q1.setParameter(1, trxId);
                q1.setParameter(2, qty);

                try
                {
                    q1.executeUpdate();
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                    return false;
                }

                Query q2 = em.createNativeQuery("EXEC DBO.SP_TERMINAL_SET_PICK_STATUS ?");
                q2.setParameter(1, trxNo);
                try
                {
                    q2.executeUpdate();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
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

    public List<PickTrx> getWaitingPackItems(String trxNo)
    {
        List<PickTrx> trxList = new ArrayList<>();
        Query q = em.createNativeQuery("SELECT IPT.INV_CODE, " +
                                       "       UPPER(ISNULL(IPT.INV_NAME, IM.INV_NAME)) AS INV_NAME, " +
                                       "       IM.INV_BRAND_CODE, " +
                                       "       IPT.RELEASE_QTY, " +
                                       "       IPT.QTY, " +
                                       "       dbo.UTO_ENG(IPT.UOM) AS UOM, " +
                                       "       IPT.UOM_FACTOR, " +
                                       "       IPT.PICK_USER_ID + ' - ' + BM.USER_NAME PICK_USER, " +
                                       "       IM.BARCODE, " +
                                       "       DBO.INV_ATTRIB_TO_STRING(IM.INV_CODE) AS NOTES, " +
                                       "       IPT.PICKER_GROUP, " +
                                       "       IPT.PICK_STATUS, " +
                                       "       IPT.WHS_CODE " +
                                       "FROM PICK_DOC PD" +
                                       "     INNER JOIN INV_PICK_TRX IPT ON IPT.TRX_NO = PD.TRX_NO" +
                                       "     INNER JOIN INV_MASTER IM ON IM.INV_CODE = IPT.INV_CODE" +
                                       "     LEFT JOIN BMS_USER BM ON IPT.PICK_USER_ID = BM.USER_ID " +
                                       "WHERE IPT.TRX_NO = ? " +
                                       "ORDER BY IPT.INV_CODE;");
        q.setParameter(1, trxNo);
        List<Object[]> resultList;
        try
        {
            resultList = q.getResultList();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }

        resultList.stream().map((result)->
                                {
                                    PickTrx trx = new PickTrx();
                                    trx.setInvCode(String.valueOf(result[0]));
                                    trx.setInvName(String.valueOf(result[1]));
                                    trx.setInvBrand(String.valueOf(result[2]));
                                    trx.setQty(Double.parseDouble(String.valueOf(result[3])));
                                    trx.setPickedQty(Double.parseDouble(String.valueOf(result[4])));
                                    trx.setUom(String.valueOf(result[5]));
                                    trx.setUomFactor(Double.parseDouble(String.valueOf(result[6])));
                                    trx.setPickUser(String.valueOf(result[7]));
                                    trx.setBarcode(String.valueOf(result[8]));
                                    trx.setNotes(String.valueOf(result[9]));
                                    trx.setPickGroup(String.valueOf(result[10]));
                                    trx.setPickStatus(String.valueOf(result[11]));
                                    trx.setWhsCode(String.valueOf(result[12]));
                                    return trx;
                                }).forEachOrdered(trxList::add);

        em.close();

        return trxList;
    }

    public List<PickDoc> getPackDocList(String userId)
    {
        Query q = em.createNativeQuery("EXEC DBO.SP_TERMINAL_GET_PACK_DOC_ALL ?");
        q.setParameter(1, userId);
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
}
