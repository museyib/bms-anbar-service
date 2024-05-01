/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.inci.bmsanbar.services;

import az.inci.bmsanbar.model.Response;
import az.inci.bmsanbar.model.PickTrx;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author User
 */

@SuppressWarnings({"unchecked", "SqlResolve", "SqlNoDataSourceInspection"})
@Service
public class TrxService extends AbstractService
{

    @Transactional
    public List<PickTrx> getPickItems(String pickUser, int mode)
    {
        List<PickTrx> trxList = new ArrayList<>();
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
            return new ArrayList<>();
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

        return trxList;
    }

    @Transactional
    public boolean collectTrx(String data, String trxNo)
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
                if(!pickStatus.equals("null"))
                    q1.setParameter(3, pickStatus);
                else
                    q1.setParameter(3, null);

                try
                {
                    q1.executeUpdate();
                }
                catch(Exception exception)
                {
                    return false;
                }
                if(!trxNo.equals("null"))
                {
                    Query q2 = em.createNativeQuery("EXEC DBO.SP_TERMINAL_SET_PICK_STATUS ?");
                    q2.setParameter(1, trxNo);
                    try
                    {
                        q2.executeUpdate();
                    }
                    catch(Exception e)
                    {
                        return false;
                    }
                }
            }
        }
        catch(JSONException ex)
        {
            return false;
        }

        em.close();

        return true;
    }

    @Transactional
    public List<PickTrx> getPackItems(String approveUser, int mode)
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
            return new ArrayList<>();
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

        return trxList;
    }

    public boolean isShipped(String trxNo)
    {
        Query q = em.createNativeQuery("SELECT * FROM SHIP_TRX WHERE SRC_TRX_NO=? AND SHIP_STATUS != 'MD'");
        q.setParameter(1, trxNo);
        List<Object[]> resultList = q.getResultList();

        em.close();

        return !resultList.isEmpty();
    }

    @Transactional
    public boolean insertShipDetails(String regionCode,
                                     String driverCode,
                                     String srcTrxNo,
                                     String vehicleCode,
                                     String userID,
                                     String shipStatus)
    {
        Query q = em.createNativeQuery("""
                               INSERT INTO TERMINAL_SHIPMENT(
                                   SHIP_REGION_CODE,
                                   DRIVER_CODE,
                                   SRC_TRX_NO,
                                   VEHICLE_CODE,
                                   USER_ID,
                                   SHIP_STATUS,
                                   TRANSITION_FLAG)
                               VALUES (
                                   :SHIP_REGION_CODE,
                                   :DRIVER_CODE,
                                   :SRC_TRX_NO,
                                   :VEHICLE_CODE,
                                   :USER_ID,
                                   :SHIP_STATUS,
                                   :TRANSITION_FLAG
                                   )""");
        q.setParameter("SHIP_REGION_CODE", regionCode);
        q.setParameter("DRIVER_CODE", driverCode);
        q.setParameter("SRC_TRX_NO", srcTrxNo);
        q.setParameter("VEHICLE_CODE", vehicleCode);
        q.setParameter("USER_ID", userID);
        if(shipStatus == null)
            shipStatus = "AC";
        int transitionFlag = shipStatus.equals("MG") ? 1 : 0;
        q.setParameter("SHIP_STATUS", shipStatus);
        q.setParameter("TRANSITION_FLAG", transitionFlag);
        try
        {
            q.executeUpdate();
        }
        catch(Exception exception)
        {
            return false;
        }

        em.close();

        return true;
    }

    @Transactional
    public boolean createShipDoc(String userId)
    {
        Query q = em.createNativeQuery("EXEC DBO.SP_TERMINAL_CREAT_SHIPMENT_DOC ?");
        q.setParameter(1, userId);
        try
        {
            q.getResultList();
        }
        catch(Exception exception)
        {
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

    public List<PickTrx> getSplitTrxList(String bpCode, String invCode, double qty)
    {
        List<PickTrx> trxList = new ArrayList<>();
        Query q = em.createNativeQuery("EXEC DBO.SP_TRX_FOR_RETURN ?,?,?");
        q.setParameter(1, bpCode);
        q.setParameter(2, invCode);
        q.setParameter(3, qty);
        List<Object[]> resultList;
        try
        {
            resultList = q.getResultList();
        }
        catch(Exception e)
        {
            return new ArrayList<>();
        }

        resultList.stream().map((result)->
                                {
                                    PickTrx trx = new PickTrx();
                                    trx.setPrevTrxId(Integer.parseInt(String.valueOf(result[0])));
                                    trx.setPrevTrxNo(String.valueOf(result[1]));
                                    trx.setTrxDate(String.valueOf(result[2]));
                                    trx.setQty(Double.parseDouble(String.valueOf(result[3])));
                                    trx.setPrice(Double.parseDouble(String.valueOf(result[4])));
                                    trx.setDiscountRatio(Double.parseDouble(String.valueOf(result[5])));
                                    trx.setPrevQtySum(Double.parseDouble(String.valueOf(result[6])));
                                    return trx;
                                }).forEachOrdered(trxList::add);

        em.close();

        return trxList;
    }

    @Transactional
    public boolean createTransfer(List<PickTrx> trxList, String srcWhsCode, String trgWhsCode, String userId)
    {
        Query q = em.createNativeQuery("INSERT INTO ANDROID_WHS_TRANSFER"
                                       + " (WHS_CODE_FROM, WHS_CODE_TO, INV_CODE, QUANTITY, USER_ID)"
                                       + " VALUES(?,?,?,?,?)");
        try
        {
            for(PickTrx trx : trxList)
            {
                q.setParameter(1, srcWhsCode);
                q.setParameter(2, trgWhsCode);
                q.setParameter(3, trx.getInvCode());
                q.setParameter(4, trx.getQty());
                q.setParameter(5, userId);
                q.executeUpdate();
            }
        }
        catch(Exception e)
        {
            return false;
        }

        q = em.createNativeQuery("EXEC DBO.SP_CREATE_RINV_DOC_FROM_ANDROID_WHS_TRANSFER ?");
        q.setParameter(1, userId);
        try
        {
            q.getResultList();
        }
        catch(Exception e)
        {
            return false;
        }
        em.close();
        return true;
    }

    @Transactional
    public boolean insertProductApproveData(List<PickTrx> trxList, String userId, String notes, int status)
    {
        Query query = em.createNativeQuery(
                "INSERT INTO TERMINAL_APPROVE("
                + "SYSTEM_NO,SYSTEM_DATE,INV_CODE,INV_QTY,NOTES,"
                + "USER_ID, INV_BRAND_CODE,INV_NAME,BARCODE,INTERNAL_COUNT,STATUS)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?)");

        try
        {
            for(PickTrx trx : trxList)
            {
                query.setParameter(1, trx.getTrxNo());
                query.setParameter(2, trx.getTrxDate());
                query.setParameter(3, trx.getInvCode());
                query.setParameter(4, trx.getQty());
                query.setParameter(5, notes);
                query.setParameter(6, userId);
                query.setParameter(7, trx.getInvBrand());
                query.setParameter(8, trx.getInvName());
                query.setParameter(9, trx.getBarcode());
                query.setParameter(10, trx.getNotes());
                query.setParameter(11, status);
                query.executeUpdate();
            }
            if(status == 2)
            {
                query = em.createNativeQuery("EXEC DBO.SP_CREATE_MAL_QEBULU ?");
                query.setParameter(1, userId);
                query.getResultList();
            }
        }
        catch(Exception e)
        {
            return false;
        }

        em.close();

        return true;
    }

    public List<PickTrx> getApproveTrxList()
    {
        Query q = em.createNativeQuery(
                "SELECT SYSTEM_NO,INV_CODE,INV_NAME,BARCODE,INV_QTY,"
                + "INTERNAL_COUNT,INV_BRAND_CODE FROM TERMINAL_APPROVE "
                + " WHERE STATUS=0");
        List<PickTrx> trxList = new ArrayList<>();
        List<Object[]> resultList = q.getResultList();
        resultList.stream().map((result)->
                                {
                                    PickTrx trx = new PickTrx();
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
    public Response createInternalUseDoc(List<PickTrx> trxList,
                                         String userId,
                                         String whsCode,
                                         String expCenterCode,
                                         String notes)
    {
        Response response = new Response();
        Query query = em.createNativeQuery(
                "INSERT INTO TERMINAL_INV_ISSUE("
                + "SYSTEM_NO,SYSTEM_DATE,INV_CODE,INV_QTY,NOTES,"
                + "USER_ID, INV_BRAND_CODE,INV_NAME,BARCODE,INTERNAL_COUNT,WHS_CODE,EXP_CENTER_CODE)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");

        try
        {
            for(PickTrx trx : trxList)
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
            response.setStatus((Integer) procedureQuery.getOutputParameterValue(2));
            response.setMessage((String) procedureQuery.getOutputParameterValue(3));
        }
        catch(Exception e)
        {
            response.setStatus(-1);
            response.setMessage(e.toString());
            return response;
        }

        em.close();

        return response;
    }
}
