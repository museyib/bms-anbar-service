package az.inci.bmsanbar.controller.v4;

import az.inci.bmsanbar.model.v4.*;
import az.inci.bmsanbar.services.v4.LogisticsServiceV4;
import az.inci.bmsanbar.services.v4.ShipmentServiceV4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static az.inci.bmsanbar.model.v4.Response.NOT_FOUND_VALID_SHIPMENT;
import static az.inci.bmsanbar.model.v4.Response.NOT_VALID_SHIPMENT_DOC;

@RequestMapping("/v4/logistics")
@RestController
public class LogisticsControllerV4 {
    private LogisticsServiceV4 logisticsService;
    private ShipmentServiceV4 shipmentService;

    @Autowired
    public void setLogisticsService(LogisticsServiceV4 logisticsService) {
        this.logisticsService = logisticsService;
    }

    @Autowired
    public void setShipmentService(ShipmentServiceV4 shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping(value = "/doc-info", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<ShipDocInfo>> getDocInfoByTrxNo(@RequestParam("trx-no") String trxNo) {
        if (shipmentService.isValid(trxNo)) {
            ShipDocInfo result = logisticsService.getDocInfoByTrxNo(trxNo);
            if (result != null)
                return ResponseEntity.ok(Response.getResultResponse(result));
            else
                return ResponseEntity.ok(Response.getUserErrorResponse(NOT_FOUND_VALID_SHIPMENT));
        } else
            return ResponseEntity.ok(Response.getUserErrorResponse(NOT_VALID_SHIPMENT_DOC));
    }

    @GetMapping(value = "/doc-info-for-sending", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<ShipDocInfo>> getDocInfoForSendingByTrxNo(@RequestParam("trx-no") String trxNo) {
        if (shipmentService.isValid(trxNo)) {
            ShipDocInfo result = logisticsService.getDocInfoForSendingByTrxNo(trxNo);
            if (result != null)
                return ResponseEntity.ok(Response.getResultResponse(result));
            else
                return ResponseEntity.ok(Response.getUserErrorResponse(NOT_FOUND_VALID_SHIPMENT));
        } else
            return ResponseEntity.ok(Response.getUserErrorResponse(NOT_VALID_SHIPMENT_DOC));
    }

    @GetMapping(value = "/doc-info-for-return", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<ShipDocInfo>> getDocInfoForReturn(@RequestParam("trx-no") String trxNo) {
        if (shipmentService.isValid(trxNo)) {
            ShipDocInfo result = logisticsService.getDocInfoForReturnByTrxNo(trxNo);
            if (result != null)
                return ResponseEntity.ok(Response.getResultResponse(result));
            else
                return ResponseEntity.ok(Response.getUserErrorResponse(NOT_FOUND_VALID_SHIPMENT));
        } else
            return ResponseEntity.ok(Response.getUserErrorResponse(NOT_VALID_SHIPMENT_DOC));
    }

    @GetMapping(value = "/doc-info-for-delivery", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<ShipDocInfo>> getDocInfoForDeliveryByTrxNo(@RequestParam("trx-no") String trxNo) {
        if (shipmentService.isValid(trxNo)) {
            ShipDocInfo result = logisticsService.getDocInfoForDeliveryByTrxNo(trxNo);
            if (result != null)
                return ResponseEntity.ok(Response.getResultResponse(result));
            else
                return ResponseEntity.ok(Response.getUserErrorResponse(NOT_FOUND_VALID_SHIPMENT));
        } else
            return ResponseEntity.ok(Response.getUserErrorResponse(NOT_VALID_SHIPMENT_DOC));
    }

    @GetMapping(value = "/doc-info-for-confirm", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<ShipDocInfo>> getDocInfoForConfirmByTrxNo(@RequestParam("trx-no") String trxNo) {
        if (shipmentService.isValid(trxNo)) {
            ShipDocInfo result = logisticsService.getDocInfoForConfirmByTrxNo(trxNo);
            if (result != null) {
                return ResponseEntity.ok(Response.getResultResponse(result));
            } else
                return ResponseEntity.ok(Response.getUserErrorResponse(NOT_FOUND_VALID_SHIPMENT));
        } else
            return ResponseEntity.ok(Response.getUserErrorResponse(NOT_VALID_SHIPMENT_DOC));
    }

    @GetMapping(value = "/doc-info-for-archive", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<ShipDocInfo>> getDocInfoForArchiveByTrxNo(@RequestParam("trx-no") String trxNo) {
        if (shipmentService.isValid(trxNo)) {
            ShipDocInfo result = logisticsService.getDocInfoForArchiveByTrxNo(trxNo);
            if (result == null) {
                return ResponseEntity.ok(Response.getUserErrorResponse(NOT_FOUND_VALID_SHIPMENT));
            } else if (result.isArchiveFlag()) {
                return ResponseEntity.ok(Response.getUserErrorResponse("Bu sənəd artıq arxivləşdirilib."));
            } else
                return ResponseEntity.ok(Response.getResultResponse(result));
        } else
            return ResponseEntity.ok(Response.getUserErrorResponse(NOT_VALID_SHIPMENT_DOC));
    }

    @GetMapping(value = "/doc-list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<List<ShipDoc>>> getDocList(@RequestParam("start-date") String startDate,
                                                              @RequestParam("end-date") String endDate) {
        List<ShipDoc> result = logisticsService.getDocList(startDate, endDate);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @PostMapping(value = "/change-doc-status", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<Void>> changeDocStatus(@RequestBody UpdateDeliveryRequest request) {
        logisticsService.changeDocStatus(request);
        return ResponseEntity.ok(Response.getSuccessResponse());
    }

    @PostMapping(value = "/confirm-delivery", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<Boolean>> confirmDelivery(@RequestBody ConfirmDeliveryRequest request) {
        boolean isValid = logisticsService.checkDeliveryConfirmationCode(request.getTrxNo(),
                request.getConfirmationCode());
        if (isValid) {
            logisticsService.confirmDelivery(request);
            return ResponseEntity.ok(Response.getSuccessResponse());
        } else
            return ResponseEntity.ok(Response.getUserErrorResponse("Təsdiq kodu düzgün deyil!"));
    }

    @PostMapping(value = "/confirm-shipment", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<Void>> confirmShipment(@RequestBody List<UpdateDeliveryRequestItem> requestList) {
        logisticsService.confirmShipment(requestList);
        return ResponseEntity.ok(Response.getSuccessResponse());
    }

    @PostMapping(value = "/archive-delivery", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<Boolean>> archiveDelivery(@RequestBody List<ArchiveDeliveryRequest> requestList) {
        logisticsService.archiveDelivery(requestList);
        return ResponseEntity.ok(Response.getSuccessResponse());
    }

    @PostMapping(value = "/update-location")
    public ResponseEntity<Response<Void>> updateDocLocation(@RequestBody UpdateDocLocationRequest request) {
        logisticsService.updateDocLocation(request);
        return ResponseEntity.ok(Response.getSuccessResponse());
    }

    @GetMapping(value = "/not-confirmed-doc-list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<List<ShipDoc>>> getNotConfirmedDocList(@RequestParam("start-date") String startDate,
                                                                          @RequestParam("end-date") String endDate,
                                                                          @RequestParam("driver-code") String driverCode) {
        List<ShipDoc> result = logisticsService.getNotConfirmedDocList(startDate, endDate, driverCode);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @GetMapping(value = "/waiting-doc-list-to-ship", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<List<WaitingDocToShip>>> getWaitingDocListToShip(@RequestParam("driver-code") String driverCode) {
        List<WaitingDocToShip> result = logisticsService.getWaitingDocListToShip(driverCode);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }
}
