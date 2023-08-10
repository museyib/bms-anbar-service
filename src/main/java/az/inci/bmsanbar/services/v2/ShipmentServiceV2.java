/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.inci.bmsanbar.services.v2;

import az.inci.bmsanbar.model.v2.ShipDocInfo;
import az.inci.bmsanbar.model.v2.ShipmentRequest;
import az.inci.bmsanbar.services.AbstractService;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author User
 */

@SuppressWarnings({"unchecked", "SqlResolve", "SqlNoDataSourceInspection"})
@Service
public class ShipmentServiceV2 extends AbstractService
{
    public ShipDocInfo checkShipment(String trxNo)
    {
        ShipDocInfo shipDocInfo = null;
        Query q = em.createNativeQuery("SELECT SD.DRIVER_CODE, \n" +
                                       "                PM.PER_NAME" +
                                       "                FROM SHIP_TRX ST" +
                                       "                JOIN SHIP_DOC SD ON ST.TRX_NO = SD.TRX_NO" +
                                       "                JOIN PER_MASTER PM ON SD.DRIVER_CODE = PM.PER_CODE" +
                                       " WHERE ST.SRC_TRX_NO=? AND ST.SHIP_STATUS != 'MD'");
        q.setParameter(1, trxNo);
        List<Object[]> resultList = q.getResultList();
        if(!resultList.isEmpty())
        {
            shipDocInfo = new ShipDocInfo();
            Object[] result = resultList.get(0);
            shipDocInfo.setDriverCode((String) result[0]);
            shipDocInfo.setDriverName((String) result[1]);
        }

        em.close();

        return shipDocInfo;
    }

    public boolean isShippedForDriver(String trxNo, String driverCode)
    {
        String shippedDriverCode = null;

        Query q = em.createNativeQuery("""
                                               SELECT SD.DRIVER_CODE,
                                                      PM.PER_NAME,
                                                      dbo.fnFormatDate(SD.TRX_DATE, 'dd-MM-yyyy')
                                               FROM SHIP_TRX ST
                                                    JOIN SHIP_DOC SD ON ST.TRX_NO = SD.TRX_NO
                                                    JOIN PER_MASTER PM ON SD.DRIVER_CODE = PM.PER_CODE
                                               WHERE SRC_TRX_NO = ?
                                                     AND DRIVER_CODE = ?
                                                     AND ST.SHIP_STATUS IN ('AC', 'YC', 'MG')""");
        q.setParameter(1, trxNo);
        q.setParameter(2, driverCode);
        List<Object[]> resultList = q.getResultList();
        if(!resultList.isEmpty())
        {
            shippedDriverCode = (String) resultList.get(0)[0];
        }

        em.close();

        return driverCode.equals(shippedDriverCode);
    }

    @Transactional
    public void insertShipDetails(String userId, List<ShipmentRequest> shipmentRequest)
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
        for(ShipmentRequest request : shipmentRequest)
        {
            int transitionFlag = request.getShipStatus().equals("MG") ? 1 : 0;
            q.setParameter("SHIP_REGION_CODE", request.getRegionCode());
            q.setParameter("DRIVER_CODE", request.getDriverCode());
            q.setParameter("SRC_TRX_NO", request.getSrcTrxNo());
            q.setParameter("VEHICLE_CODE", request.getVehicleCode());
            q.setParameter("USER_ID", userId);
            q.setParameter("SHIP_STATUS", request.getShipStatus());
            q.setParameter("TRANSITION_FLAG", transitionFlag);

            q.executeUpdate();
        }

        em.close();

        createShipDoc(userId);
    }

    @Transactional
    public void createShipDoc(String userId)
    {
        Query q = em.createNativeQuery("EXEC DBO.SP_TERMINAL_CREAT_SHIPMENT_DOC :TRX_NO");
        q.setParameter("TRX_NO", userId);
        q.getResultList();
        em.close();
    }

    public boolean isValid(String trxNo)
    {
        Query q = em.createNativeQuery("SELECT * FROM SHIPMENT_DOC_PREFIX WHERE TRX_NO=?");
        q.setParameter(1, trxNo.substring(0, 3));
        List<Object[]> resultList = q.getResultList();

        em.close();

        return !resultList.isEmpty();
    }
}
