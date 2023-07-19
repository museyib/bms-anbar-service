/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.inci.bmsanbar.services.v3;

import az.inci.bmsanbar.model.v3.CheckShipmentResponse;
import az.inci.bmsanbar.model.v3.ShipmentRequest;
import az.inci.bmsanbar.model.v3.ShipmentRequestItem;
import az.inci.bmsanbar.services.AbstractService;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static jakarta.persistence.ParameterMode.IN;

/**
 * @author User
 */

@SuppressWarnings({"unchecked", "SqlResolve", "SqlNoDataSourceInspection"})
@Service
public class ShipmentServiceV3 extends AbstractService
{
    public CheckShipmentResponse checkShipment(String trxNo)
    {
        Query q = em.createNativeQuery("""
                                       SELECT
                                           SD.DRIVER_CODE,
                                           PM.PER_NAME
                                       FROM SHIP_TRX ST
                                       JOIN SHIP_DOC SD ON ST.TRX_NO = SD.TRX_NO
                                       JOIN PER_MASTER PM ON SD.DRIVER_CODE = PM.PER_CODE
                                       WHERE ST.SRC_TRX_NO = :SRC_TRX_NO AND ST.SHIP_STATUS != 'MD'""");
        q.setParameter("SRC_TRX_NO", trxNo);
        List<Object[]> resultList = q.getResultList();
        CheckShipmentResponse shipmentResponse = new CheckShipmentResponse();
        if(resultList.size() > 0)
        {
            Object[] result = resultList.get(0);
            shipmentResponse.setDriverCode((String) result[0]);
            shipmentResponse.setDriverName((String) result[1]);
            shipmentResponse.setShipped(true);

        }

        em.close();

        return shipmentResponse;
    }

    public boolean isShippedForDriver(String trxNo, String driverCode)
    {
        String shippedDriverCode = null;

        Query q = em.createNativeQuery("""
                SELECT
                    SD.DRIVER_CODE,
                    PM.PER_NAME,
                    dbo.fnFormatDate(SD.TRX_DATE, 'dd-MM-yyyy')
                FROM SHIP_TRX ST
                JOIN SHIP_DOC SD ON ST.TRX_NO = SD.TRX_NO
                JOIN PER_MASTER PM ON SD.DRIVER_CODE = PM.PER_CODE
                WHERE SRC_TRX_NO = :SRC_TRX_NO
                      AND DRIVER_CODE = :DRIVER_CODE
                      AND ST.SHIP_STATUS IN ('AC', 'YC', 'MG')""");
        q.setParameter("SRC_TRX_NO", trxNo);
        q.setParameter("DRIVER_CODE", driverCode);
        List<Object[]> resultList = q.getResultList();
        if(resultList.size() > 0)
        {
            shippedDriverCode = (String) resultList.get(0)[0];
        }

        em.close();

        return driverCode.equals(shippedDriverCode);
    }

    @Transactional
    public void insertShipDetails(ShipmentRequest request)
    {
        Query q = em.createNativeQuery("""                        
                INSERT INTO TERMINAL_SHIPMENT(
                    SHIP_REGION_CODE,
                    DRIVER_CODE,
                    SRC_TRX_NO,
                    VEHICLE_CODE,
                    USER_ID,
                    SHIP_STATUS)
                VALUES (
                    :SHIP_REGION_CODE,
                    :DRIVER_CODE,
                    :SRC_TRX_NO,
                    :VEHICLE_CODE,
                    :USER_ID,
                    :SHIP_STATUS)""");
        for(ShipmentRequestItem requestItem : request.getRequestItems())
        {
            q.setParameter("SHIP_REGION_CODE", request.getRegionCode());
            q.setParameter("DRIVER_CODE", request.getDriverCode());
            q.setParameter("SRC_TRX_NO", requestItem.getSrcTrxNo());
            q.setParameter("VEHICLE_CODE", request.getVehicleCode());
            q.setParameter("USER_ID", request.getUserId());
            q.setParameter("SHIP_STATUS", requestItem.getShipStatus());
            q.executeUpdate();
        }

        em.close();

        createShipDoc(request.getUserId());
    }

    @Transactional
    public void createShipDoc(String userId)
    {
        StoredProcedureQuery query = em.createStoredProcedureQuery("SP_TERMINAL_CREAT_SHIPMENT_DOC");
        query.registerStoredProcedureParameter("USER_ID", String.class, IN);
        query.setParameter("USER_ID", userId);
        query.execute();
        em.close();
    }

    public boolean isValid(String trxNo)
    {
        Query q = em.createNativeQuery("SELECT * FROM SHIPMENT_DOC_PREFIX WHERE TRX_NO = :TRX_NO");
        q.setParameter("TRX_NO", trxNo.substring(0, 3));
        List<Object[]> resultList = q.getResultList();

        em.close();

        return resultList.size() > 0;
    }
}
