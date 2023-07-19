package az.inci.bmsanbar.services.v3;

import az.inci.bmsanbar.model.Doc;
import az.inci.bmsanbar.model.Trx;
import az.inci.bmsanbar.model.v2.CollectTrxRequest;
import az.inci.bmsanbar.services.AbstractService;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.ParameterMode.IN;

@SuppressWarnings("unchecked")
@Service
public class PackServiceV3 extends AbstractService
{

    @Transactional
    public Doc getPackDoc(String approveUser, int mode)
    {
        List<Trx> trxList = new ArrayList<>();
        StoredProcedureQuery query = em.createStoredProcedureQuery("SP_TERMINAL_GET_PACK_ITEMS");
        query.registerStoredProcedureParameter("USER_ID", String.class, IN);
        query.registerStoredProcedureParameter("MODE", Integer.class, IN);
        query.registerStoredProcedureParameter("CC", String.class, IN);
        query.registerStoredProcedureParameter("BRANCH_CODE", String.class, IN);
        query.registerStoredProcedureParameter("TRX", String.class, IN);
        query.setParameter("USER_ID", approveUser);
        query.setParameter("MODE", mode);
        query.setParameter("CC", null);
        query.setParameter("BRANCH_CODE", null);
        query.setParameter("TRX", null);
        List<Object[]> resultList = query.getResultList();

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

        Doc doc = null;

        if(!trxList.isEmpty())
        {
            String trxNo = trxList.get(0).getTrxNo();
            doc = getPackDocByTrxNo(trxNo);
            doc.setTrxList(trxList);
        }

        return doc;
    }

    @Transactional
    public Doc getPackDocByTrxNo(String trxNo)
    {
        StoredProcedureQuery query = em.createStoredProcedureQuery("SP_TERMINAL_GET_PACK_DOC");
        query.registerStoredProcedureParameter("TRX_NO", String.class, IN);
        query.setParameter("TRX_NO", trxNo);
        Doc doc = new Doc();
        List<Object[]> resultList = query.getResultList();
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
        for(CollectTrxRequest data : dataList)
        {
            StoredProcedureQuery query = em.createStoredProcedureQuery("SP_TERMINAL_COLLECT_WITH_TIME");
            query.registerStoredProcedureParameter("TRX_ID", Integer.class, IN);
            query.registerStoredProcedureParameter("QTY", Double.class, IN);
            query.registerStoredProcedureParameter("SECONDS", Integer.class, IN);
            query.registerStoredProcedureParameter("PICK_STATUS", String.class, IN);
            query.setParameter("TRX_ID", data.getTrxId());
            query.setParameter("QTY", data.getQty());
            query.setParameter("SECONDS", data.getSeconds());
            query.setParameter("PICK_STATUS", null);
            query.execute();

            query = em.createStoredProcedureQuery("SP_TERMINAL_SET_PICK_STATUS");
            query.registerStoredProcedureParameter("TRX_NO", String.class, IN);
            query.setParameter("TRX_NO", trxNo);
            query.execute();
        }

        em.close();
    }

    public List<Trx> getWaitingPackItems(String trxNo)
    {
        List<Trx> trxList = new ArrayList<>();
        Query query = em.createNativeQuery("""
                           SELECT IPT.INV_CODE,
                               UPPER(ISNULL(IPT.INV_NAME, IM.INV_NAME)) AS INV_NAME,
                               IM.INV_BRAND_CODE,
                               IPT.RELEASE_QTY,
                               IPT.QTY,
                               dbo.UTO_ENG(IPT.UOM) AS UOM,
                               IPT.UOM_FACTOR,
                               IPT.PICK_USER_ID + ' - ' + BM.USER_NAME PICK_USER,
                               IM.BARCODE,
                               DBO.INV_ATTRIB_TO_STRING(IM.INV_CODE) AS NOTES,
                               IPT.PICKER_GROUP,
                               IPT.PICK_STATUS,
                               IPT.WHS_CODE
                           FROM PICK_DOC PD
                           INNER JOIN INV_PICK_TRX IPT ON IPT.TRX_NO = PD.TRX_NO
                           INNER JOIN INV_MASTER IM ON IM.INV_CODE = IPT.INV_CODE
                           LEFT JOIN BMS_USER BM ON IPT.PICK_USER_ID = BM.USER_ID
                           WHERE IPT.TRX_NO = :TRX_NO
                           ORDER BY IPT.INV_CODE;""");
        query.setParameter("TRX_NO", trxNo);
        List<Object[]> resultList = query.getResultList();

        resultList.stream().map((result)->
                                {
                                    Trx trx = new Trx();
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
                                    trx.setPickStatus(String.valueOf(result[11]));
                                    trx.setWhsCode((String) result[12]);
                                    return trx;
                                }).forEachOrdered(trxList::add);

        em.close();

        return trxList;
    }

    public List<Doc> getPackDocList(String userId)
    {
        StoredProcedureQuery query = em.createStoredProcedureQuery("SP_TERMINAL_GET_PACK_DOC_ALL");
        query.registerStoredProcedureParameter("USER_ID", String.class, IN);
        query.setParameter("USER_ID", userId);
        List<Doc> docList = new ArrayList<>();
        List<Object[]> resultList = query.getResultList();
        resultList.stream().map((result)->
                                {
                                    Doc doc = new Doc();
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
