package az.inci.bmsanbar.services.v2;

import az.inci.bmsanbar.model.ShipDoc;
import az.inci.bmsanbar.model.v2.ShipDocInfo;
import az.inci.bmsanbar.model.v2.UpdateDeliveryRequest;
import az.inci.bmsanbar.model.v2.UpdateDeliveryRequestItem;
import az.inci.bmsanbar.model.v2.UpdateDocLocationRequest;
import az.inci.bmsanbar.services.AbstractService;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"SqlResolve", "unchecked", "SqlNoDataSourceInspection"})
@Service
public class LogisticsServiceV2 extends AbstractService
{
    public ShipDocInfo getDocInfoByTrxNo(String trxNo)
    {
        ShipDocInfo shipDocInfo = null;

        Query query = em.createNativeQuery(
                "SELECT TOP 1 SD.DRIVER_CODE," +
                "PM.PER_NAME," +
                "SD.VEHICLE_CODE," +
                "ISNULL(ST.DELIVER_NOTES, '') AS DELIVER_NOTES," +
                "ST.SHIP_STATUS, " +
                "SS.SHIP_STATUS_DESC " +
                "FROM SHIP_TRX ST " +
                "JOIN SHIP_DOC SD ON ST.TRX_NO = SD.TRX_NO " +
                "JOIN PER_MASTER PM ON SD.DRIVER_CODE = PM.PER_CODE " +
                "JOIN SHIP_STATUS SS ON ST.SHIP_STATUS = SS.SHIP_STATUS_CODE " +
                "WHERE ST.SRC_TRX_NO = ? " +
                "ORDER BY ST.TRX_ID DESC");

        query.setParameter(1, trxNo);

        List<Object[]> resultList = query.getResultList();

        if(!resultList.isEmpty())
        {
            shipDocInfo = new ShipDocInfo();
            Object[] result = resultList.get(0);
            shipDocInfo.setDriverCode((String) result[0]);
            shipDocInfo.setDriverName((String) result[1]);
            shipDocInfo.setVehicleCode((String) result[2]);
            shipDocInfo.setDeliverNotes((String) result[3]);
            shipDocInfo.setShipStatus((String) result[4]);
            shipDocInfo.setShipStatusDescription((String) result[5]);
        }

        em.close();

        return shipDocInfo;
    }

    public ShipDocInfo getDocInfoForConfirmByTrxNo(String trxNo)
    {
        ShipDocInfo shipDocInfo = null;

        Query query = em.createNativeQuery(
                "SELECT TOP 1 SD.DRIVER_CODE," +
                "PM.PER_NAME," +
                "SD.VEHICLE_CODE," +
                "ISNULL(ST.DELIVER_NOTES, '') AS DELIVER_NOTES," +
                "ST.SHIP_STATUS " +
                "FROM SHIP_TRX ST " +
                "JOIN SHIP_DOC SD ON ST.TRX_NO = SD.TRX_NO " +
                "JOIN PER_MASTER PM ON SD.DRIVER_CODE = PM.PER_CODE " +
                "WHERE ST.SHIP_STATUS IN ('AC', 'YC', 'MG') AND " +
                "ST.SRC_TRX_NO = ? " +
                "ORDER BY ST.TRX_ID DESC");

        query.setParameter(1, trxNo);

        List<Object[]> resultList = query.getResultList();

        if(!resultList.isEmpty())
        {
            shipDocInfo = new ShipDocInfo();
            Object[] result = resultList.get(0);
            shipDocInfo.setDriverCode((String) result[0]);
            shipDocInfo.setDriverName((String) result[1]);
            shipDocInfo.setVehicleCode((String) result[2]);
            shipDocInfo.setDeliverNotes((String) result[3]);
            shipDocInfo.setShipStatus((String) result[4]);
        }

        em.close();

        return shipDocInfo;
    }

    public ShipDocInfo getDocInfoForSendingByTrxNo(String trxNo)
    {
        ShipDocInfo shipDocInfo = null;

        Query query = em.createNativeQuery(
                "SELECT TOP 1 SD.DRIVER_CODE," +
                "PM.PER_NAME," +
                "SD.VEHICLE_CODE," +
                "ISNULL(ST.DELIVER_NOTES, '') AS DELIVER_NOTES," +
                "ST.SHIP_STATUS " +
                "FROM SHIP_TRX ST " +
                "JOIN SHIP_DOC SD ON ST.TRX_NO = SD.TRX_NO " +
                "JOIN PER_MASTER PM ON SD.DRIVER_CODE = PM.PER_CODE " +
                "WHERE ST.SHIP_STATUS IN ('AC','PL','MG') AND " +
                "ST.SRC_TRX_NO = ? " +
                "ORDER BY ST.TRX_ID DESC");

        query.setParameter(1, trxNo);

        List<Object[]> resultList = query.getResultList();

        if(!resultList.isEmpty())
        {
            shipDocInfo = new ShipDocInfo();
            Object[] result = resultList.get(0);
            shipDocInfo.setDriverCode((String) result[0]);
            shipDocInfo.setDriverName((String) result[1]);
            shipDocInfo.setVehicleCode((String) result[2]);
            shipDocInfo.setDeliverNotes((String) result[3]);
            shipDocInfo.setShipStatus((String) result[4]);
        }

        em.close();

        return shipDocInfo;
    }

    public ShipDocInfo getDocInfoForReturnByTrxNo(String trxNo)
    {
        ShipDocInfo shipDocInfo = null;

        Query query = em.createNativeQuery(
                "SELECT TOP 1 SD.DRIVER_CODE," +
                "PM.PER_NAME," +
                "SD.VEHICLE_CODE," +
                "ISNULL(ST.DELIVER_NOTES, '') AS DELIVER_NOTES," +
                "ST.SHIP_STATUS " +
                "FROM SHIP_TRX ST " +
                "JOIN SHIP_DOC SD ON ST.TRX_NO = SD.TRX_NO " +
                "JOIN PER_MASTER PM ON SD.DRIVER_CODE = PM.PER_CODE " +
                "WHERE ST.SHIP_STATUS IN ('YC', 'AC', 'PL', 'MG') AND " +
                "ST.SRC_TRX_NO = ? " +
                "ORDER BY ST.TRX_ID DESC");

        query.setParameter(1, trxNo);

        List<Object[]> resultList = query.getResultList();

        if(!resultList.isEmpty())
        {
            shipDocInfo = new ShipDocInfo();
            Object[] result = resultList.get(0);
            shipDocInfo.setDriverCode((String) result[0]);
            shipDocInfo.setDriverName((String) result[1]);
            shipDocInfo.setVehicleCode((String) result[2]);
            shipDocInfo.setDeliverNotes((String) result[3]);
            shipDocInfo.setShipStatus((String) result[4]);
        }

        em.close();

        return shipDocInfo;
    }

    public ShipDocInfo getDocInfoForDeliveryByTrxNo(String trxNo)
    {
        ShipDocInfo shipDocInfo = null;

        Query query = em.createNativeQuery(
                "SELECT TOP 1 SD.DRIVER_CODE," +
                "PM.PER_NAME," +
                "SD.VEHICLE_CODE," +
                "ISNULL(ST.DELIVER_NOTES, '') AS DELIVER_NOTES," +
                "ST.SHIP_STATUS," +
                "ISNULL(AD.BP_CODE, ID.TRG_WHS_CODE) AS BP_CODE," +
                "ISNULL(AD.BP_NAME, WM.WHS_NAME) AS BP_NAME," +
                "ISNULL(BMC.LONGITUDE, 0) AS LONGITUDE," +
                "ISNULL(BMC.LATITUDE, 0) AS LATITUDE " +
                "FROM SHIP_TRX ST " +
                "JOIN SHIP_DOC SD ON ST.TRX_NO = SD.TRX_NO " +
                "JOIN PER_MASTER PM ON SD.DRIVER_CODE = PM.PER_CODE " +
                "LEFT JOIN INV_DOC ID ON ST.SRC_TRX_NO=ID.TRX_NO " +
                "LEFT JOIN ACC_DOC AD ON ST.SRC_TRX_NO=AD.TRX_NO " +
                "LEFT JOIN BP_MASTER_COORDINATE BMC ON (AD.BP_CODE = BMC.BP_CODE or ID.TRG_WHS_CODE=BMC.BP_CODE) " +
                "LEFT JOIN WHS_MASTER WM ON ID.TRG_WHS_CODE = WM.WHS_CODE " +
                "WHERE ST.SHIP_STATUS IN ('YC') AND " +
                "ST.SRC_TRX_NO = ? " +
                "ORDER BY ST.TRX_ID DESC");

        query.setParameter(1, trxNo);

        List<Object[]> resultList = query.getResultList();

        if(!resultList.isEmpty())
        {
            shipDocInfo = new ShipDocInfo();
            Object[] result = resultList.get(0);
            shipDocInfo.setDriverCode((String) result[0]);
            shipDocInfo.setDriverName((String) result[1]);
            shipDocInfo.setVehicleCode((String) result[2]);
            shipDocInfo.setDeliverNotes((String) result[3]);
            shipDocInfo.setShipStatus((String) result[4]);
            shipDocInfo.setTargetCode((String) result[5]);
            shipDocInfo.setTargetName((String) result[6]);
            shipDocInfo.setLongitude(Double.parseDouble((String) result[7]));
            shipDocInfo.setLatitude(Double.parseDouble((String) result[8]));
        }

        em.close();

        return shipDocInfo;
    }

    public List<ShipDoc> getDocList(String startDate, String endDate)
    {
        List<ShipDoc> docList = new ArrayList<>();

        Query query = em.createNativeQuery(
                "SELECT ST.TRX_NO,ST.SRC_TRX_NO,dbo.fnFormatDate(ST.TRX_DATE, 'yyyy-MM-dd')," +
                "ISNULL(AD.BP_CODE, ISNULL(ID.TRG_WHS_CODE, ET.EXP_CENTER_CODE)) AS CUST_CODE," +
                "ISNULL(AD.BP_NAME, ISNULL(WM.WHS_NAME, EC.EXP_CENTER_DESC)) AS CUST_NAME," +
                "ISNULL(AD.SBE_CODE, '') AS SBE_CODE," +
                "ISNULL(AD.SBE_NAME, '') AS SBE_NAME," +
                "ISNULL(ST.DELIVER_NOTES, '') AS DELIVER_NOTES," +
                "PM.PER_NAME," +
                "ST.SHIP_STATUS " +
                "FROM SHIP_TRX ST " +
                "LEFT JOIN SHIP_DOC SD ON ST.TRX_NO = SD.TRX_NO " +
                "LEFT JOIN INV_DOC ID ON ST.SRC_TRX_NO = ID.TRX_NO " +
                "LEFT JOIN ACC_DOC AD ON ST.SRC_TRX_NO = AD.TRX_NO " +
                "LEFT JOIN EXP_TRX ET ON ST.SRC_TRX_NO = ET.TRX_NO " +
                "LEFT JOIN EXP_CENTER EC ON ET.EXP_CENTER_CODE = EC.EXP_CENTER_CODE " +
                "LEFT JOIN WHS_MASTER WM ON ID.TRG_WHS_CODE = WM.WHS_CODE " +
                "LEFT JOIN PER_MASTER PM ON SD.DRIVER_CODE = PM.PER_CODE " +
                "WHERE ST.TRX_DATE BETWEEN ? AND ? " +
                "ORDER BY ST.TRX_NO DESC");

        query.setParameter(1, startDate);
        query.setParameter(2, endDate);

        List<Object[]> resultList = query.getResultList();

        resultList.stream().map((result)->
                                {
                                    ShipDoc doc = new ShipDoc();
                                    doc.setDocNo((String) result[0]);
                                    doc.setTrxNo((String) result[1]);
                                    doc.setTrxDate((String) result[2]);
                                    doc.setBpCode((String) result[3]);
                                    doc.setBpName((String) result[4]);
                                    doc.setSbeCode((String) result[5]);
                                    doc.setSbeName((String) result[6]);
                                    doc.setDriverCode((String) result[7]);
                                    doc.setDriverName((String) result[8]);
                                    doc.setShipStatus((String) result[9]);
                                    return doc;
                                }).forEachOrdered(docList::add);

        em.close();

        return docList;
    }

    @Transactional
    public void changeDocStatus(UpdateDeliveryRequest request)
    {
        String sql = "EXEC DBO.SP_CHANGE_SHIP_STATUS ?,?,?,?,?";
        Query query = em.createNativeQuery(sql);

        for(UpdateDeliveryRequestItem requestItem : request.getRequestItems())
        {
            query.setParameter(1, requestItem.getTrxNo());
            query.setParameter(2, request.getStatus());
            query.setParameter(3, requestItem.getDriverCode());
            query.setParameter(4, requestItem.getNote());
            query.setParameter(5, requestItem.getDeliverPerson());

            query.executeUpdate();
        }

        em.close();
    }

    @Transactional
    public void confirmShipment(List<UpdateDeliveryRequestItem> requestList)
    {
        String sql = "EXEC DBO.SP_CONFIRM_SHIPMENT ?,?,?,?";
        Query query = em.createNativeQuery(sql);

        for(UpdateDeliveryRequestItem request : requestList)
        {
            query.setParameter(1, request.getTrxNo());
            query.setParameter(2, request.getDriverCode());
            query.setParameter(3, request.getNote());
            query.setParameter(4, request.getDeliverPerson());

            query.executeUpdate();
        }

        em.close();
    }

    @Transactional
    public void updateDocLocation(UpdateDocLocationRequest request)
    {
        Query query = em.createNativeQuery("UPDATE SHIP_TRX"
                                           + "  SET LATITUDE = ?,"
                                           + "      LONGITUDE = ?,"
                                           + "      ADDRESS = ?"
                                           + " FROM SHIP_DOC SD"
                                           + "     JOIN SHIP_TRX ST ON SD.TRX_NO = ST.TRX_NO"
                                           + " WHERE ST.SHIP_STATUS = 'YC'"
                                           + "      AND SD.DRIVER_CODE ="
                                           + "(SELECT PER_CODE"
                                           + "    FROM PER_MASTER"
                                           + "    WHERE USER_ID = ?);");
        query.setParameter(1, request.getLatitude());
        query.setParameter(2, request.getLongitude());
        query.setParameter(3, URLDecoder.decode(request.getAddress(), StandardCharsets.UTF_8));
        query.setParameter(4, request.getUserId());
        query.executeUpdate();

        em.close();
    }
}
