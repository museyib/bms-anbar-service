/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.inci.bmsanbar.controller.v2;

import az.inci.bmsanbar.model.v4.Response;
import az.inci.bmsanbar.model.v2.ShipDocInfo;
import az.inci.bmsanbar.model.v2.ShipmentRequest;
import az.inci.bmsanbar.services.v2.ShipmentServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author User
 */
@RequestMapping("/v2/shipment")
@RestController
public class ShipmentControllerV2
{
    private ShipmentServiceV2 service;

    @Autowired
    public void setService(ShipmentServiceV2 service)
    {
        this.service = service;
    }

    @GetMapping("/check-shipment")
    public ResponseEntity<Response> checkShipment(@RequestParam("trx-no") String trxNo)
    {
        try
        {
            if(service.isValid(trxNo))
            {
                ShipDocInfo docInfo = service.checkShipment(trxNo);
                return ResponseEntity.ok(Response.builder()
                                                 .statusCode(0)
                                                 .data(docInfo)
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

    @GetMapping("/shipped-for-driver")
    public ResponseEntity<Response> isShippedForDriver(@RequestParam("trx-no") String trxNo,
                                                       @RequestParam("driver-code") String driverCode)
    {
        try
        {
            boolean result = service.isShippedForDriver(trxNo, driverCode);
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

    @PostMapping(value = "/create-shipment")
    public ResponseEntity<Response> insertShipDetails(@RequestParam("user-id") String userId,
                                                      @RequestBody List<ShipmentRequest> shipmentRequest)
    {
        try
        {
            service.insertShipDetails(userId, shipmentRequest);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .developerMessage("Uğurlu əməliyyat")
                                             .build());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }

    @GetMapping(value = "/is-valid")
    public ResponseEntity<Response> isValid(@RequestParam("trx-no") String trxNo)
    {
        try
        {
            boolean result = service.isValid(trxNo);
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
}
