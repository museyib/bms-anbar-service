/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.model.v2.Response;
import az.inci.bmsanbar.model.v3.CheckShipmentResponse;
import az.inci.bmsanbar.model.v3.ShipmentRequest;
import az.inci.bmsanbar.services.v3.ShipmentServiceV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author User
 */
@RequestMapping("/v3/shipment")
@RestController
@Slf4j
public class ShipmentControllerV3
{
    private ShipmentServiceV3 service;

    @Autowired
    public void setService(ShipmentServiceV3 service)
    {
        this.service = service;
    }

    @GetMapping("/check-shipment")
    @ResponseBody
    public ResponseEntity<Response> checkShipment(@RequestParam("trx-no") String trxNo,
                                                  @RequestParam(value = "driver-code", required = false) String driverCode)
    {
        try
        {
            if(service.isValid(trxNo))
            {
                CheckShipmentResponse result = service.checkShipment(trxNo);
                if(!result.isShipped() &&
                   driverCode != null &&
                   service.isShippedForDriver(trxNo, driverCode))
                {
                    return ResponseEntity.ok(Response.getUserErrorResponse("Eyni sənəd bir sürücü üçün yalnız bir dəfə yükləməyə verilə bilər."));
                }
                return ResponseEntity.ok(Response.getResultResponse(result));
            }
            else
                return ResponseEntity.ok(Response.getUserErrorResponse("Yükləmə üçün keçərli sənəd deyil."));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping("/shipped-for-driver")
    @ResponseBody
    public ResponseEntity<Response> isShippedForDriver(@RequestParam("trx-no") String trxNo,
                                                       @RequestParam("driver-code") String driverCode)
    {
        try
        {
            boolean result = service.isShippedForDriver(trxNo, driverCode);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @PostMapping(value = "/create-shipment")
    @ResponseBody
    public ResponseEntity<Response> insertShipDetails(@RequestBody ShipmentRequest shipmentRequest)
    {
        try
        {
            service.insertShipDetails(shipmentRequest);
            return ResponseEntity.ok(Response.getSuccessResponse());
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/is-valid")
    @ResponseBody
    public ResponseEntity<Response> isValid(@RequestParam("trx-no") String trxNo)
    {
        try
        {
            boolean result = service.isValid(trxNo);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }
}
