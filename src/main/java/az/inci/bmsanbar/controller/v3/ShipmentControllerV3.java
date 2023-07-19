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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author User
 */
@RequestMapping("/v3/shipment")
@RestController
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
    public ResponseEntity<Response> checkShipment(@RequestParam("trx-no") String trxNo)
    {
        try
        {
            if(service.isValid(trxNo))
            {
                CheckShipmentResponse shipmentResponse = service.checkShipment(trxNo);
                return ResponseEntity.ok(Response.builder()
                                                 .statusCode(0)
                                                 .data(shipmentResponse)
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
    @ResponseBody
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
    @ResponseBody
    public ResponseEntity<Response> insertShipDetails(@RequestBody ShipmentRequest shipmentRequest)
    {
        try
        {
            service.insertShipDetails(shipmentRequest);
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
    @ResponseBody
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
