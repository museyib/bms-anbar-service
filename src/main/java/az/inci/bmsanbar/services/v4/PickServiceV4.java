package az.inci.bmsanbar.services.v4;

import az.inci.bmsanbar.model.v4.CollectTrxRequest;
import az.inci.bmsanbar.model.v4.PickDoc;
import az.inci.bmsanbar.model.v4.PickTrx;
import az.inci.bmsanbar.services.AbstractService;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.ParameterMode.IN;

@Service
public class PickServiceV4 extends AbstractService {

    @Transactional
    public PickDoc getPickDoc(String pickUser, int mode) {
        List<PickTrx> trxList = new ArrayList<>();
        StoredProcedureQuery query = em.createStoredProcedureQuery("SP_TERMINAL_GET_PICK_ITEMS");
        query.registerStoredProcedureParameter("USER_ID", String.class, IN);
        query.registerStoredProcedureParameter("MODE", Integer.class, IN);
        query.registerStoredProcedureParameter("CC", String.class, IN);
        query.registerStoredProcedureParameter("BRANCH_CODE", String.class, IN);
        query.registerStoredProcedureParameter("TRX", String.class, IN);
        query.setParameter("USER_ID", pickUser);
        query.setParameter("MODE", mode);
        query.setParameter("CC", null);
        query.setParameter("BRANCH_CODE", null);
        query.setParameter("TRX", null);
        List<Object[]> resultList = query.getResultList();

        resultList.stream().map((result) ->
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

        PickDoc doc = null;

        if (!trxList.isEmpty()) {
            String trxNo = trxList.get(0).getTrxNo();
            doc = getPickDocByTrxNo(trxNo, pickUser);
            doc.setTrxList(trxList);
        }

        return doc;
    }

    @Transactional
    public PickDoc getPickDocByTrxNo(String trxNo, String pickUser) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("SP_TERMINAL_GET_PICK_DOC");
        query.registerStoredProcedureParameter("TRX_NO", String.class, IN);
        query.registerStoredProcedureParameter("USER_ID", String.class, IN);
        query.setParameter("TRX_NO", trxNo);
        query.setParameter("USER_ID", pickUser);
        PickDoc doc = new PickDoc();
        List<Object[]> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
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
    public void collectTrx(List<CollectTrxRequest> dataList) {

        for (CollectTrxRequest data : dataList) {
            StoredProcedureQuery query = em.createStoredProcedureQuery("SP_TERMINAL_COLLECT_WITH_TIME");
            query.registerStoredProcedureParameter("TRX_ID", Integer.class, IN);
            query.registerStoredProcedureParameter("QTY", Double.class, IN);
            query.registerStoredProcedureParameter("SECONDS", Integer.class, IN);
            query.registerStoredProcedureParameter("PICK_STATUS", String.class, IN);
            query.registerStoredProcedureParameter("NOT_PICKED_REASON_ID", String.class, IN);
            query.registerStoredProcedureParameter("DEVICE_ID", String.class, IN);
            query.setParameter("TRX_ID", data.getTrxId());
            query.setParameter("QTY", data.getQty());
            query.setParameter("SECONDS", data.getSeconds());
            query.setParameter("PICK_STATUS", data.getPickStatus());
            query.setParameter("NOT_PICKED_REASON_ID", data.getNotPickedReasonId());
            query.setParameter("DEVICE_ID", data.getDeviceId());
            query.execute();
        }

        em.close();
    }

    @Transactional
    public boolean resetPickDoc(String trxNo, String userId) {
        if (!resetAllowed(userId))
            return false;

        StoredProcedureQuery query = em.createStoredProcedureQuery("SP_TERMINAL_PICK_RESET");
        query.registerStoredProcedureParameter("TRX_NO", String.class, IN);
        query.registerStoredProcedureParameter("USER_ID", String.class, IN);
        query.setParameter("TRX_NO", trxNo);
        query.setParameter("USER_ID", userId);
        query.execute();

        em.close();

        return true;
    }

    public boolean resetAllowed(String id) {
        Query q = em.createNativeQuery("SELECT dbo.FN_GET_DYN_SETTING ('ALLOW_PICK_RESET', :USER_ID)");
        q.setParameter("USER_ID", id);
        List<Object> result = q.getResultList();

        em.close();

        return !result.isEmpty() && String.valueOf(result.get(0)).equals("1");
    }

    public Integer getReport(String startDate, String endDate, String pickUser) {
        Query q = em.createNativeQuery("""
                SELECT count(*) FROM INV_PICK_TRX IPT JOIN PICK_DOC PD
                ON IPT.TRX_NO=PD.TRX_NO AND PD.REC_STATUS=6
                WHERE IPT.TRX_DATE BETWEEN :START_DATE AND :END_DATE
                AND IPT.PICK_USER_ID = :USER_ID
                AND IPT.PICK_STATUS IN ('P', 'Q')""");
        q.setParameter("START_DATE", startDate);
        q.setParameter("END_DATE", endDate);
        q.setParameter("USER_ID", pickUser);
        List<Integer> resultList = q.getResultList();
        Integer qty = 0;
        if (!resultList.isEmpty())
            qty = resultList.get(0);

        em.close();

        return qty;
    }

    public Integer getReportActual(String startDate, String endDate, String pickUser) {
        Query q = em.createNativeQuery("""
                SELECT count(*) FROM INV_PICK_TRX
                WHERE CAST(PICK_END AS DATE) BETWEEN :START_DATE AND :END_DATE
                AND PICK_USER_ID = :USER_ID
                AND PICK_STATUS IN ('P', 'Q')""");
        q.setParameter("START_DATE", startDate);
        q.setParameter("END_DATE", endDate);
        q.setParameter("USER_ID", pickUser);
        List<Integer> resultList = q.getResultList();
        Integer qty = 0;
        if (!resultList.isEmpty())
            qty = resultList.get(0);

        em.close();

        return qty;
    }
}
