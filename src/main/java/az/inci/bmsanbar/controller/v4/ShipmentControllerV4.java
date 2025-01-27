package az.inci.bmsanbar.controller.v4;

import az.inci.bmsanbar.model.v4.Response;
import az.inci.bmsanbar.model.v4.CheckShipmentResponse;
import az.inci.bmsanbar.model.v4.ShipmentRequest;
import az.inci.bmsanbar.services.v4.ShipmentServiceV4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static az.inci.bmsanbar.model.v4.Response.NOT_VALID_SHIPMENT_DOC;

@RequestMapping("/v4/shipment")
@RestController
public class ShipmentControllerV4
{
    private ShipmentServiceV4 service;

    @Autowired
    public void setService(ShipmentServiceV4 service)
    {
        this.service = service;
    }

    @GetMapping("/check-shipment")
    public ResponseEntity<Response<CheckShipmentResponse>> checkShipment(@RequestParam("trx-no") String trxNo,
                                                  @RequestParam(value = "driver-code", required = false) String driverCode)
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
            return ResponseEntity.ok(Response.getUserErrorResponse(NOT_VALID_SHIPMENT_DOC));
    }

    @GetMapping("/shipped-for-driver")
    public ResponseEntity<Response<Boolean>> isShippedForDriver(@RequestParam("trx-no") String trxNo,
                                                       @RequestParam("driver-code") String driverCode)
    {
        boolean result = service.isShippedForDriver(trxNo, driverCode);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @PostMapping(value = "/create-shipment")
    public ResponseEntity<Response<Void>> insertShipDetails(@RequestBody ShipmentRequest shipmentRequest)
    {
        service.insertShipDetails(shipmentRequest);
        return ResponseEntity.ok(Response.getSuccessResponse());
    }

    @GetMapping(value = "/is-valid")
    public ResponseEntity<Response<Boolean>> isValid(@RequestParam("trx-no") String trxNo)
    {
        boolean result = service.isValid(trxNo);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }
}
