package az.inci.bmsanbar.services.v2;

import az.inci.bmsanbar.model.PickDoc;
import az.inci.bmsanbar.model.PickTrx;
import az.inci.bmsanbar.model.v2.CollectTrxRequest;
import az.inci.bmsanbar.services.AbstractService;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
@Service
public class PackServiceV2 extends AbstractService
{

    @Transactional
    public PickDoc getPackDoc(String approveUser, int mode)
    {
        List<PickTrx> trxList = new ArrayList<>();
        Query q = em.createNativeQuery("EXEC DBO.SP_TERMINAL_GET_PACK_ITEMS ?,?,null,null,null");
        q.setParameter(1, approveUser);
        q.setParameter(2, mode);
        List<Object[]> resultList = q.getResultList();

        resultList.stream().map((result)->
                                {
                                    PickTrx trx = new PickTrx();
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
            doc.setTrxNo((String) result[0]);
            doc.setTrxDate((String) result[1]);
            doc.setDescription((String) result[2]);
            doc.setBpCode((String) result[3]);
            doc.setBpName((String) result[4]);
            doc.setSbeCode((String) result[5]);
            doc.setSbeName((String) result[6]);
            doc.setPrevTrxNo((String) result[7]);
            doc.setApproveUser((String) result[8]);
            doc.setNotes((String) result[9]);
            doc.setWhsCode((String) result[10]);
        }

        em.close();

        return doc;
    }

    @Transactional
    public void collectTrx(List<CollectTrxRequest> dataList, String trxNo)
    {
        Query q1 = em.createNativeQuery("EXEC DBO.SP_TERMINAL_COLLECT_WITH_TIME ?,?,?,NULL");

        for(CollectTrxRequest data : dataList)
        {
            q1.setParameter(1, data.getTrxId());
            q1.setParameter(2, data.getQty());
            q1.setParameter(3, data.getSeconds());
            q1.executeUpdate();

            Query q2 = em.createNativeQuery("EXEC DBO.SP_TERMINAL_SET_PICK_STATUS ?");
            q2.setParameter(1, trxNo);
            q2.executeUpdate();
        }

        em.close();
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
        List<Object[]> resultList = q.getResultList();

        resultList.stream().map((result)->
                                {
                                    PickTrx trx = new PickTrx();
                                    trx.setInvCode((String) result[0]);
                                    trx.setInvName((String) result[1]);
                                    trx.setInvBrand((String) result[2]);
                                    trx.setQty(Double.parseDouble(String.valueOf(result[3])));
                                    trx.setPickedQty(Double.parseDouble(String.valueOf(result[4])));
                                    trx.setUom((String) result[5]);
                                    trx.setUomFactor(Double.parseDouble(String.valueOf(result[6])));
                                    trx.setPickUser((String) result[7]);
                                    trx.setBarcode((String) result[8]);
                                    trx.setNotes((String) result[9]);
                                    trx.setPickGroup((String) result[10]);
                                    trx.setPickStatus((String) result[11]);
                                    trx.setWhsCode((String) result[12]);
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
                                    doc.setTrxNo((String) result[0]);
                                    doc.setTrxDate((String) result[1]);
                                    doc.setDescription((String) result[2]);
                                    doc.setBpCode((String) result[3]);
                                    doc.setBpName((String) result[4]);
                                    doc.setSbeCode((String) result[5]);
                                    doc.setSbeName((String) result[6]);
                                    doc.setPrevTrxNo((String) result[7]);
                                    doc.setNotes((String) result[8]);
                                    doc.setItemCount((int) result[9]);
                                    doc.setPickedItemCount((int) result[10]);
                                    return doc;
                                }).forEachOrdered(docList::add);

        em.close();

        return docList;
    }
}
