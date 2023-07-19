package az.inci.bmsanbar.services.v2;

import az.inci.bmsanbar.model.Doc;
import az.inci.bmsanbar.model.Trx;
import az.inci.bmsanbar.model.v2.ProductApproveRequest;
import az.inci.bmsanbar.model.v2.ProductApproveRequestItem;
import az.inci.bmsanbar.model.v2.TransferRequest;
import az.inci.bmsanbar.model.v2.TransferRequestItem;
import az.inci.bmsanbar.services.AbstractService;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
@Service
public class InvMoveServiceV2 extends AbstractService
{

    public List<Trx> getSplitTrxList(String bpCode, String invCode, double qty)
    {
        List<Trx> trxList = new ArrayList<>();
        Query q = em.createNativeQuery("EXEC DBO.SP_TRX_FOR_RETURN ?,?,?");
        q.setParameter(1, bpCode);
        q.setParameter(2, invCode);
        q.setParameter(3, qty);
        List<Object[]> resultList = q.getResultList();

        resultList.stream().map((result)->
                                {
                                    Trx trx = new Trx();
                                    trx.setPrevTrxId(Integer.parseInt((String) result[1]));
                                    trx.setPrevTrxNo((String) result[2]);
                                    trx.setTrxDate((String) result[3]);
                                    trx.setQty(Double.parseDouble(String.valueOf(result[4])));
                                    trx.setPrice(Double.parseDouble(String.valueOf(result[5])));
                                    trx.setDiscountRatio(Double.parseDouble(String.valueOf(result[6])));
                                    return trx;
                                }).forEachOrdered(trxList::add);

        em.close();

        return trxList;
    }

    @Transactional
    public void createTransfer(TransferRequest request)
    {
        Query q = em.createNativeQuery("INSERT INTO ANDROID_WHS_TRANSFER"
                                       + " (WHS_CODE_FROM, WHS_CODE_TO, INV_CODE, QUANTITY, USER_ID)"
                                       + " VALUES(?,?,?,?,?)");
        for(TransferRequestItem requestItem : request.getRequestItems())
        {
            q.setParameter(1, request.getSrcWhsCode());
            q.setParameter(2, request.getTrgWhsCode());
            q.setParameter(3, requestItem.getInvCode());
            q.setParameter(4, requestItem.getQty());
            q.setParameter(5, request.getUserId());
            q.executeUpdate();
        }

        q = em.createNativeQuery("EXEC DBO.SP_CREATE_RINV_DOC_FROM_ANDROID_WHS_TRANSFER ?");
        q.executeUpdate();
        em.close();
    }

    @Transactional
    public void insertProductApproveData(ProductApproveRequest request)
    {
        Query query = em.createNativeQuery(
                "INSERT INTO TERMINAL_APPROVE("
                + "SYSTEM_NO,SYSTEM_DATE,INV_CODE,INV_QTY,NOTES,"
                + "USER_ID, INV_BRAND_CODE,INV_NAME,BARCODE,INTERNAL_COUNT,STATUS)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?)");

        for(ProductApproveRequestItem item : request.getRequestItems())
        {
            query.setParameter(1, request.getTrxNo());
            query.setParameter(2, request.getTrxDate());
            query.setParameter(3, item.getInvCode());
            query.setParameter(4, item.getQty());
            query.setParameter(5, request.getNotes());
            query.setParameter(6, request.getUserId());
            query.setParameter(7, item.getInvBrand());
            query.setParameter(8, item.getInvName());
            query.setParameter(9, item.getBarcode());
            query.setParameter(10, request.getNotes());
            query.setParameter(11, request.getStatus());
            query.executeUpdate();
        }
        if(request.getStatus() == 2)
        {
            query = em.createNativeQuery("EXEC DBO.SP_CREATE_MAL_QEBULU ?");
            query.setParameter(1, request.getUserId());
            query.getResultList();
        }

        em.close();
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

        q.executeUpdate();

        em.close();

        return docList;
    }

    public List<Trx> getApproveTrxList()
    {
        Query q = em.createNativeQuery(
                "SELECT SYSTEM_NO,INV_CODE,INV_NAME,BARCODE,INV_QTY,"
                + "INTERNAL_COUNT,INV_BRAND_CODE FROM TERMINAL_APPROVE "
                + " WHERE STATUS=0");
        List<Trx> trxList = new ArrayList<>();
        List<Object[]> resultList = q.getResultList();
        resultList.stream().map((result)->
                                {
                                    Trx trx = new Trx();
                                    trx.setTrxNo(String.valueOf(result[0]));
                                    trx.setInvCode(String.valueOf(result[1]));
                                    trx.setInvName(String.valueOf(result[2]));
                                    trx.setBarcode(String.valueOf(result[3]));
                                    trx.setQty(Double.parseDouble(String.valueOf(result[4])));
                                    trx.setNotes(String.valueOf(result[5]));
                                    trx.setInvBrand(String.valueOf(result[6]));
                                    return trx;
                                }).forEachOrdered(trxList::add);

        em.close();

        return trxList;
    }

    @Transactional
    public void createInternalUseDoc(List<Trx> trxList,
                                     String userId,
                                     String whsCode,
                                     String expCenterCode,
                                     String notes)
    {
        Query query = em.createNativeQuery(
                "INSERT INTO TERMINAL_INV_ISSUE("
                + "SYSTEM_NO,SYSTEM_DATE,INV_CODE,INV_QTY,NOTES,"
                + "USER_ID, INV_BRAND_CODE,INV_NAME,BARCODE,INTERNAL_COUNT,WHS_CODE,EXP_CENTER_CODE)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");

        for(Trx trx : trxList)
        {
            query.setParameter(1, trx.getTrxNo());
            query.setParameter(2, new Date(System.currentTimeMillis()));
            query.setParameter(3, trx.getInvCode());
            query.setParameter(4, trx.getQty());
            query.setParameter(5, notes);
            query.setParameter(6, userId);
            query.setParameter(7, trx.getInvBrand());
            query.setParameter(8, trx.getInvName());
            query.setParameter(9, trx.getBarcode());
            query.setParameter(10, trx.getNotes());
            query.setParameter(11, whsCode);
            query.setParameter(12, expCenterCode);
            query.executeUpdate();
        }
        StoredProcedureQuery procedureQuery = em.createStoredProcedureQuery("SP_CREATE_INTERNAL_USE_ORDER");
        procedureQuery.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        procedureQuery.registerStoredProcedureParameter(2, Integer.class, ParameterMode.OUT);
        procedureQuery.registerStoredProcedureParameter(3, String.class, ParameterMode.OUT);
        procedureQuery.setParameter(1, userId);
        procedureQuery.execute();
        em.close();
    }
}
