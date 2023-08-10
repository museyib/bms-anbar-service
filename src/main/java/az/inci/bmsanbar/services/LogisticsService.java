package az.inci.bmsanbar.services;

import az.inci.bmsanbar.model.ShipDoc;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"SqlResolve", "unchecked", "SqlNoDataSourceInspection"})
@Service
public class LogisticsService extends AbstractService
{
    public String[] getDocInfoForConfirmByTrxNo(String trxNo)
    {
        String[] result = null;

        Query query = em.createNativeQuery(
                "SELECT SD.DRIVER_CODE,PM.PER_NAME,SD.VEHICLE_CODE,ST.DELIVER_NOTES,ST.SHIP_STATUS " +
                "FROM SHIP_TRX ST " +
                "JOIN SHIP_DOC SD ON ST.TRX_NO = SD.TRX_NO " +
                "JOIN PER_MASTER PM ON SD.DRIVER_CODE = PM.PER_CODE " +
                "WHERE ST.SHIP_STATUS IN ('AC', 'YC', 'MG') AND " +
                "(ST.SRC_TRX_NO = ? OR ST.SRC_TRX_NO = dbo.FN_GET_RELATED_TRX_NO(?))");

        query.setParameter(1, trxNo);
        query.setParameter(2, trxNo);

        List<Object[]> resultList = query.getResultList();

        if(!resultList.isEmpty())
        {
            result = new String[resultList.get(0).length];

            for(int i = 0; i < result.length; i++)
            {
                result[i] = String.valueOf(resultList.get(0)[i]);
            }
        }

        return result;
    }

    public String[] getDocInfoForSendingByTrxNo(String trxNo)
    {
        String[] result = null;

        Query query = em.createNativeQuery(
                "SELECT SD.DRIVER_CODE,PM.PER_NAME,SD.VEHICLE_CODE,ST.DELIVER_NOTES,ST.SHIP_STATUS " +
                "FROM SHIP_TRX ST " +
                "JOIN SHIP_DOC SD ON ST.TRX_NO = SD.TRX_NO " +
                "JOIN PER_MASTER PM ON SD.DRIVER_CODE = PM.PER_CODE " +
                "WHERE ST.SHIP_STATUS IN ('AC','PL') AND " +
                "(ST.SRC_TRX_NO = ? OR ST.SRC_TRX_NO = dbo.FN_GET_RELATED_TRX_NO(?))");

        query.setParameter(1, trxNo);
        query.setParameter(2, trxNo);

        List<Object[]> resultList = query.getResultList();

        if(!resultList.isEmpty())
        {
            result = new String[resultList.get(0).length];

            for(int i = 0; i < result.length; i++)
            {
                result[i] = String.valueOf(resultList.get(0)[i]);
            }
        }

        em.close();

        return result;
    }

    public String[] getDocInfoForReturnByTrxNo(String trxNo)
    {
        String[] result = null;

        Query query = em.createNativeQuery(
                "SELECT SD.DRIVER_CODE,PM.PER_NAME,SD.VEHICLE_CODE,ST.DELIVER_NOTES,ST.SHIP_STATUS " +
                "FROM SHIP_TRX ST " +
                "JOIN SHIP_DOC SD ON ST.TRX_NO = SD.TRX_NO " +
                "JOIN PER_MASTER PM ON SD.DRIVER_CODE = PM.PER_CODE " +
                "WHERE ST.SHIP_STATUS = 'YC' AND" +
                "(ST.SRC_TRX_NO = ? OR ST.SRC_TRX_NO = dbo.FN_GET_RELATED_TRX_NO(?))");

        query.setParameter(1, trxNo);
        query.setParameter(2, trxNo);

        List<Object[]> resultList = query.getResultList();

        if(!resultList.isEmpty())
        {
            result = new String[resultList.get(0).length];

            for(int i = 0; i < result.length; i++)
            {
                result[i] = String.valueOf(resultList.get(0)[i]);
            }
        }

        em.close();

        return result;
    }

    public String[] getDocInfoForDeliveryByTrxNo(String trxNo)
    {
        String[] result = null;

        Query query = em.createNativeQuery(
                "SELECT SD.DRIVER_CODE,PM.PER_NAME,SD.VEHICLE_CODE,ST.DELIVER_NOTES,ST.SHIP_STATUS," +
                "ISNULL(AD.BP_CODE, ID.TRG_WHS_CODE) AS BP_CODE,ISNULL(AD.BP_NAME, WM.WHS_NAME) AS BP_NAME," +
                "ISNULL(BMC.LONGITUDE, '') AS LONGITUDE,ISNULL(BMC.LATITUDE, '') AS LATITUDE " +
                "FROM SHIP_TRX ST " +
                "JOIN SHIP_DOC SD ON ST.TRX_NO = SD.TRX_NO " +
                "JOIN PER_MASTER PM ON SD.DRIVER_CODE = PM.PER_CODE " +
                "LEFT JOIN INV_DOC ID ON ST.SRC_TRX_NO=ID.TRX_NO " +
                "LEFT JOIN ACC_DOC AD ON ST.SRC_TRX_NO=AD.TRX_NO " +
                "LEFT JOIN BP_MASTER_COORDINATE BMC ON (AD.BP_CODE = BMC.BP_CODE or ID.TRG_WHS_CODE=BMC.BP_CODE) " +
                "LEFT JOIN WHS_MASTER WM ON ID.TRG_WHS_CODE = WM.WHS_CODE " +
                "WHERE ST.SHIP_STATUS IN ('YC') AND" +
                "(ST.SRC_TRX_NO = ? OR ST.SRC_TRX_NO = dbo.FN_GET_RELATED_TRX_NO(?))");

        query.setParameter(1, trxNo);
        query.setParameter(2, trxNo);

        List<Object[]> resultList = query.getResultList();

        if(!resultList.isEmpty())
        {
            result = new String[resultList.get(0).length];

            for(int i = 0; i < result.length; i++)
            {
                result[i] = String.valueOf(resultList.get(0)[i]);
            }
        }

        em.close();

        return result;
    }

    public String[] getDocInfoForStatusByTrxNo(String trxNo)
    {
        String[] result = null;

        Query query = em.createNativeQuery(
                "SELECT SD.DRIVER_CODE,PM.PER_NAME,SD.VEHICLE_CODE,ST.DELIVER_NOTES,ST.SHIP_STATUS," +
                "ST.DELIVER_TIME FROM SHIP_TRX ST " +
                "JOIN SHIP_DOC SD ON ST.TRX_NO = SD.TRX_NO " +
                "JOIN PER_MASTER PM ON SD.DRIVER_CODE = PM.PER_CODE " +
                "WHERE ST.SRC_TRX_NO = ? OR ST.SRC_TRX_NO = dbo.FN_GET_RELATED_TRX_NO(?)");

        query.setParameter(1, trxNo);
        query.setParameter(2, trxNo);

        List<Object[]> resultList = query.getResultList();

        if(!resultList.isEmpty())
        {
            result = new String[resultList.get(0).length];

            for(int i = 0; i < result.length; i++)
            {
                result[i] = String.valueOf(resultList.get(0)[i]);
            }
        }

        em.close();

        return result;
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
                "SD.DRIVER_CODE," +
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
                                    doc.setDocNo(String.valueOf(result[0]));
                                    doc.setTrxNo(String.valueOf(result[1]));
                                    doc.setTrxDate(String.valueOf(result[2]));
                                    doc.setBpCode(String.valueOf(result[3]));
                                    doc.setBpName(String.valueOf(result[4]));
                                    doc.setSbeCode(String.valueOf(result[5]));
                                    doc.setSbeName(String.valueOf(result[6]));
                                    doc.setDriverCode(String.valueOf(result[7]));
                                    doc.setDriverName(String.valueOf(result[8]));
                                    doc.setShipStatus(String.valueOf(result[9]));
                                    return doc;
                                }).forEachOrdered(docList::add);

        em.close();

        return docList;
    }

    @Transactional
    public boolean changeDocStatus(String trxNo, String status, String note, String deliverPerson)
    {
        int transitionFlag = status.equals("MD") ? 1 : 0;
        int deliverFlag = (status.equals("MC") || status.equals("MD")) ? 1 : 0;
        String sql = """
                UPDATE SHIP_TRX
                SET SHIP_STATUS = :SHIP_STATUS,
                    DELIVER_NOTES = :DELIVER_NOTES,
                    DELIVER_PERSON = :DELIVER_PERSON,
                    DELIVER_TIME = CONVERT(DATETIME2(0),GETDATE()),
                    DELIVER_FLAG = :DELIVER_FLAG,
                    TRANSITION_FLAG = :TRANSITION_FLAG
                WHERE SRC_TRX_NO = :TRX_NO; EXEC DBO.SP_UPDATE_SHIP_DOC_STATUS :TRX_NO""";
        Query query = em.createNativeQuery(sql);

        query.setParameter("SHIP_STATUS", status);
        query.setParameter("DELIVER_NOTES", note);
        query.setParameter("DELIVER_PERSON", deliverPerson);
        query.setParameter("DELIVER_FLAG", deliverFlag);
        query.setParameter("TRANSITION_FLAG", transitionFlag);
        query.setParameter("TRX_NO", trxNo);

        try
        {
            query.executeUpdate();
        }
        catch(Exception e)
        {
            return false;
        }

        em.close();

        return true;
    }

    @Transactional
    public boolean updateDocLocation(String lat, String apt, String address, String userId)
    {
        try
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
            query.setParameter(1, lat);
            query.setParameter(2, apt);
            query.setParameter(3, URLDecoder.decode(address, StandardCharsets.UTF_8));
            query.setParameter(4, userId);
            query.executeUpdate();
        }
        catch(Exception e)
        {
            return false;
        }

        em.close();

        return true;
    }
}
