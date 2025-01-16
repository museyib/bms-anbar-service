package az.inci.bmsanbar.services.v3;

import az.inci.bmsanbar.model.v3.PurchaseDoc;
import az.inci.bmsanbar.model.v3.PurchaseTrx;
import az.inci.bmsanbar.model.v3.UpdatePurchaseTrxRequest;
import az.inci.bmsanbar.services.AbstractService;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseServiceV3 extends AbstractService {

    public List<PurchaseDoc> getDocList(String userId)
    {
        List<PurchaseDoc> docList = new ArrayList<>();

        Query query = em.createNativeQuery("""
                SELECT
                    TRX_NO,
                    dbo.fnFormatDate(TRX_DATE, 'dd-MM-yyyy'),
                    DOC_DESC,
                    BP_CODE,
                    BP_NAME,
                    SBE_CODE,
                    SBE_NAME,
                    TRX_TYPE_ID,
                    AMOUNT_OPR,
                    OD.WHS_CODE
                FROM ORD_DOC OD
                JOIN BMS_USER_WHS BUW ON OD.WHS_CODE = BUW.WHS_CODE AND BUW.USER_ID = :USER_ID
                WHERE
                    TRX_TYPE_ID = 10 AND
                    REC_STATUS = 3 AND
                    TERMINAL_FLAG = 1 AND
                    SBE_CODE = '100'
                ORDER BY TRX_NO DESC""");
        query.setParameter("USER_ID", userId);
        List<Object[]> resultList = query.getResultList();
        for (Object[] result : resultList)
        {
            PurchaseDoc doc = getPurchaseDoc(result);
            docList.add(doc);
        }
        return docList;
    }

    private PurchaseDoc getPurchaseDoc(Object[] result) {
        PurchaseDoc doc = new PurchaseDoc();
        doc.setTrxNo((String) result[0]);
        doc.setTrxDate(String.valueOf(result[1]));
        doc.setDescription((String) result[2]);
        doc.setBpCode((String) result[3]);
        doc.setBpName((String) result[4]);
        doc.setSbeCode((String) result[5]);
        doc.setSbeName((String) result[6]);
        doc.setTrxTypeId(Integer.parseInt(String.valueOf(result[7])));
        doc.setAmount(Double.parseDouble(String.valueOf(result[8])));
        doc.setWhsCode((String) result[9]);
        return doc;
    }

    public List<PurchaseTrx> getTrxList(String trxNo)
    {
        List<PurchaseTrx> trxList = new ArrayList<>();

        Query query = em.createNativeQuery("""
                SELECT
                    OT.TRX_ID,
                    OT.INV_CODE,
                    OT.INV_NAME,
                    OT.QTY - OT.POSTED_QTY,
                    OT.UOM,
                    OT.UOM_FACTOR,
                    OT.UNIT_PRICE_OPR,
                    OT.AMOUNT_OPR,
                    IB.BAR_CODE,
                    OT.WHS_CODE,
                    OT.PICKED_QTY
                FROM OINV_TRX OT
                LEFT JOIN INV_BARCODE IB ON OT.INV_CODE = IB.INV_CODE
                WHERE OT.QTY - OT.POSTED_QTY > 0 AND OT.TRX_NO = :TRX_NO
                ORDER BY OT.TRX_ID""");

        query.setParameter("TRX_NO", trxNo);
        List<Object[]> resultList = query.getResultList();
        for (Object[] result : resultList)
        {
            PurchaseTrx trx = new PurchaseTrx();
            trx.setTrxNo(trxNo);
            trx.setTrxId((Integer) result[0]);
            trx.setInvCode((String) result[1]);
            trx.setInvName((String) result[2]);
            trx.setQty(Double.parseDouble(String.valueOf(result[3])));
            trx.setUom((String) result[4]);
            trx.setUomFactor(Double.parseDouble(String.valueOf(result[5])));
            trx.setAmount(Double.parseDouble(String.valueOf(result[6])));
            trx.setWhsCode((String) result[9]);
            trx.setCountedQty(Double.parseDouble(String.valueOf(result[10])));
            if (trxList.contains(trx))
                trxList.get(trxList.indexOf(trx)).getBarcodes().add(String.valueOf(result[8]));
            else {
                List<String> barcodes = new ArrayList<>();
                barcodes.add(String.valueOf(result[8]));
                trx.setBarcodes(barcodes);
                trxList.add(trx);
            }
        }

        em.close();

        return trxList;
    }

    @Transactional
    public void updatePurchaseQty(UpdatePurchaseTrxRequest request)
    {
        Query query = em.createNativeQuery("""
                UPDATE OINV_TRX SET PICKED_QTY = :QTY WHERE TRX_ID = :TRX_ID""");
        query.setParameter("QTY", request.getQty());
        query.setParameter("TRX_ID", request.getTrxId());
        query.executeUpdate();
        em.close();
    }
}
