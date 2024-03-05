package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.model.ShipDoc;
import az.inci.bmsanbar.model.v2.*;
import az.inci.bmsanbar.model.v3.ConfirmDeliveryRequest;
import az.inci.bmsanbar.services.v3.LogisticsServiceV3;
import az.inci.bmsanbar.services.v3.ShipmentServiceV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static az.inci.bmsanbar.model.v2.Response.NOT_FOUND_VALID_SHIPMENT;
import static az.inci.bmsanbar.model.v2.Response.NOT_VALID_SHIPMENT_DOC;

@RequestMapping("/v3/logistics")
@RestController
@Slf4j
public class LogisticsControllerV3
{
    private LogisticsServiceV3 logisticsService;
    private ShipmentServiceV3 shipmentService;

    @Autowired
    public void setLogisticsService(LogisticsServiceV3 logisticsService)
    {
        this.logisticsService = logisticsService;
    }

    @Autowired
    public void setShipmentService(ShipmentServiceV3 shipmentService)
    {
        this.shipmentService = shipmentService;
    }

    @GetMapping(value = "/doc-info", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getDocInfoByTrxNo(@RequestParam("trx-no") String trxNo)
    {
        try
        {
            if(shipmentService.isValid(trxNo))
            {
                ShipDocInfo result = logisticsService.getDocInfoByTrxNo(trxNo);
                if(result != null)
                    return ResponseEntity.ok(Response.getResultResponse(result));
                else
                    return ResponseEntity.ok(Response.getUserErrorResponse(NOT_FOUND_VALID_SHIPMENT));
            }
            else
                return ResponseEntity.ok(Response.getUserErrorResponse(NOT_VALID_SHIPMENT_DOC));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/doc-info-for-sending", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getDocInfoForSendingByTrxNo(@RequestParam("trx-no") String trxNo)
    {
        try
        {
            if(shipmentService.isValid(trxNo))
            {
                ShipDocInfo result = logisticsService.getDocInfoForSendingByTrxNo(trxNo);
                if(result != null)
                    return ResponseEntity.ok(Response.getResultResponse(result));
                else
                    return ResponseEntity.ok(Response.getUserErrorResponse(NOT_FOUND_VALID_SHIPMENT));
            }
            else
                return ResponseEntity.ok(Response.getUserErrorResponse(NOT_VALID_SHIPMENT_DOC));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/doc-info-for-return", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getDocInfoForReturn(@RequestParam("trx-no") String trxNo)
    {
        try
        {
            if(shipmentService.isValid(trxNo))
            {
                ShipDocInfo result = logisticsService.getDocInfoForReturnByTrxNo(trxNo);
                if(result != null)
                    return ResponseEntity.ok(Response.getResultResponse(result));
                else
                    return ResponseEntity.ok(Response.getUserErrorResponse(NOT_FOUND_VALID_SHIPMENT));
            }
            else
                return ResponseEntity.ok(Response.getUserErrorResponse(NOT_VALID_SHIPMENT_DOC));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/doc-info-for-delivery", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getDocInfoForDeliveryByTrxNo(@RequestParam("trx-no") String trxNo)
    {
        try
        {
            if(shipmentService.isValid(trxNo))
            {
                ShipDocInfo result = logisticsService.getDocInfoForDeliveryByTrxNo(trxNo);
                if(result != null)
                    return ResponseEntity.ok(Response.getResultResponse(result));
                else
                    return ResponseEntity.ok(Response.getUserErrorResponse(NOT_FOUND_VALID_SHIPMENT));
            }
            else
                return ResponseEntity.ok(Response.getUserErrorResponse(NOT_VALID_SHIPMENT_DOC));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/doc-info-for-confirm", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getDocInfoForConfirmByTrxNo(@RequestParam("trx-no") String trxNo)
    {
        try
        {
            if(shipmentService.isValid(trxNo))
            {
                ShipDocInfo result = logisticsService.getDocInfoForConfirmByTrxNo(trxNo);
                if(result != null)
                    return ResponseEntity.ok(Response.getResultResponse(result));
                else
                    return ResponseEntity.ok(Response.getUserErrorResponse(NOT_FOUND_VALID_SHIPMENT));
            }
            else
                return ResponseEntity.ok(Response.getUserErrorResponse(NOT_VALID_SHIPMENT_DOC));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/doc-list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getDocList(@RequestParam("start-date") String startDate,
                                               @RequestParam("end-date") String endDate)
    {
        try
        {
            List<ShipDoc> result = logisticsService.getDocList(startDate, endDate);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @PostMapping(value = "/change-doc-status", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> changeDocStatus(@RequestBody UpdateDeliveryRequest request)
    {
        try
        {
            logisticsService.changeDocStatus(request);
            return ResponseEntity.ok(Response.getSuccessResponse());
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @PostMapping(value = "/confirm-delivery", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> confirmDelivery(@RequestBody ConfirmDeliveryRequest request)
    {
        try
        {
            boolean isValid = logisticsService.checkDeliveryConfirmationCode(request.getTrxNo(),
                                                                             request.getConfirmatioinCode());
            if(isValid)
            {
                logisticsService.confirmDelivery(request);
                return ResponseEntity.ok(Response.getSuccessResponse());
            }
            else
                return ResponseEntity.ok(Response.getUserErrorResponse("Təsdiq kodu düzgün deyil!"));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @PostMapping(value = "/confirm-shipment", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> confirmShipment(@RequestBody List<UpdateDeliveryRequestItem> requestList)
    {
        try
        {
            logisticsService.confirmShipment(requestList);
            return ResponseEntity.ok(Response.getSuccessResponse());
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @PostMapping(value = "/update-location")
    public ResponseEntity<Response> updateDocLocation(@RequestBody UpdateDocLocationRequest request)
    {
        try
        {
            logisticsService.updateDocLocation(request);
            return ResponseEntity.ok(Response.getSuccessResponse());
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/not-confirmed-doc-list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getNotConfirmedDocList(@RequestParam("start-date") String startDate,
                                                           @RequestParam("end-date") String endDate,
                                                           @RequestParam("driver-code") String driverCode)
    {
        try
        {
            List<ShipDoc> result = logisticsService.getNotConfirmedDocList(startDate, endDate, driverCode);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }
}
