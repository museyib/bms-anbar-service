package az.inci.bmsanbar.controller.v2;

import az.inci.bmsanbar.model.ShipDoc;
import az.inci.bmsanbar.model.v2.*;
import az.inci.bmsanbar.services.v2.LogisticsServiceV2;
import az.inci.bmsanbar.services.v2.ShipmentServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v2/logistics")
@RestController
public class LogisticsControllerV2
{
    private LogisticsServiceV2 logisticsService;
    private ShipmentServiceV2 shipmentService;

    @Autowired
    public void setLogisticsService(LogisticsServiceV2 logisticsService)
    {
        this.logisticsService = logisticsService;
    }

    @Autowired
    public void setShipmentService(ShipmentServiceV2 shipmentService)
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
                    return ResponseEntity.ok(Response.builder()
                                                     .statusCode(0)
                                                     .data(result)
                                                     .build());
                else
                    return ResponseEntity.ok(Response.builder()
                                                     .statusCode(2)
                                                     .developerMessage("Bu sənəd üzrə keçərli yükləmə tapılmadı!")
                                                     .build());
            }
            else
                return ResponseEntity.ok(Response.builder()
                                                 .statusCode(2)
                                                 .developerMessage("Yükləmə üçün keçərli sənəd deyil.")
                                                 .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
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
                    return ResponseEntity.ok(Response.builder()
                                                     .statusCode(0)
                                                     .data(result)
                                                     .build());
                else
                    return ResponseEntity.ok(Response.builder()
                                                     .statusCode(2)
                                                     .developerMessage("Bu sənəd üzrə keçərli yükləmə tapılmadı!")
                                                     .build());
            }
            else
                return ResponseEntity.ok(Response.builder()
                                                 .statusCode(2)
                                                 .developerMessage("Yükləmə üçün keçərli sənəd deyil.")
                                                 .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
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
                    return ResponseEntity.ok(Response.builder()
                                                     .statusCode(0)
                                                     .data(result)
                                                     .build());
                else
                    return ResponseEntity.ok(Response.builder()
                                                     .statusCode(2)
                                                     .developerMessage("Bu sənəd üzrə keçərli yükləmə tapılmadı!")
                                                     .build());
            }
            else
                return ResponseEntity.ok(Response.builder()
                                                 .statusCode(2)
                                                 .developerMessage("Yükləmə üçün keçərli sənəd deyil.")
                                                 .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
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
                    return ResponseEntity.ok(Response.builder()
                                                     .statusCode(0)
                                                     .data(result)
                                                     .build());
                else
                    return ResponseEntity.ok(Response.builder()
                                                     .statusCode(2)
                                                     .developerMessage("Bu sənəd üzrə keçərli yükləmə tapılmadı!")
                                                     .build());
            }
            else
                return ResponseEntity.ok(Response.builder()
                                                 .statusCode(2)
                                                 .developerMessage("Yükləmə üçün keçərli sənəd deyil.")
                                                 .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
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
                    return ResponseEntity.ok(Response.builder()
                                                     .statusCode(0)
                                                     .data(result)
                                                     .build());
                else
                    return ResponseEntity.ok(Response.builder()
                                                     .statusCode(2)
                                                     .developerMessage("Bu sənəd üzrə keçərli yükləmə tapılmadı!")
                                                     .build());
            }
            else
                return ResponseEntity.ok(Response.builder()
                                                 .statusCode(2)
                                                 .developerMessage("Yükləmə üçün keçərli sənəd deyil.")
                                                 .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }

    @GetMapping(value = "/doc-list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getDocList(@RequestParam("start-date") String startDate,
                                               @RequestParam("end-date") String endDate)
    {
        try
        {
            List<ShipDoc> result = logisticsService.getDocList(startDate, endDate);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(result)
                                             .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }

    @PostMapping(value = "/change-doc-status", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> changeDocStatus(@RequestBody UpdateDeliveryRequest request)
    {
        try
        {
            logisticsService.changeDocStatus(request);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .developerMessage("Uğurlu əməliyyat")
                                             .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }

    @PostMapping(value = "/confirm-shipment", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> confirmShipment(@RequestBody List<UpdateDeliveryRequestItem> requestList)
    {
        try
        {
            logisticsService.confirmShipment(requestList);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .developerMessage("Uğurlu əməliyyat")
                                             .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }

    @PostMapping(value = "/update-location")
    public ResponseEntity<Response> updateDocLocation(@RequestBody UpdateDocLocationRequest request)
    {
        try
        {
            logisticsService.updateDocLocation(request);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }
}
