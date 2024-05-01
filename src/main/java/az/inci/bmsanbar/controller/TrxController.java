/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.inci.bmsanbar.controller;

import az.inci.bmsanbar.model.Response;
import az.inci.bmsanbar.model.PickTrx;
import az.inci.bmsanbar.services.TrxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author User
 */
@RequestMapping("/trx")
@RestController
public class TrxController
{
    private TrxService service;

    @Autowired
    public void setService(TrxService service)
    {
        this.service = service;
    }

    @RequestMapping(value = "/pick", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<PickTrx>> pickTrx(@RequestParam("pick-user") String pickUser,
                                                 @RequestParam("mode") int mode)
    {
        List<PickTrx> trxList = service.getPickItems(pickUser, mode);
        return new ResponseEntity<>(trxList, HttpStatus.OK);
    }

    @RequestMapping(value = "/pack", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<PickTrx>> packTrx(@RequestParam("approve-user") String approveUser,
                                                 @RequestParam("mode") int mode)
    {
        List<PickTrx> trxList = service.getPackItems(approveUser, mode);
        return new ResponseEntity<>(trxList, HttpStatus.OK);
    }

    @RequestMapping(value = "/collect", method = RequestMethod.POST)
    public ResponseEntity<Boolean> collectTrx(@RequestBody String data,
                                              @RequestParam("trx-no") String trxNo)
    {
        boolean result = service.collectTrx(data, trxNo);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping("/shipped")
    public ResponseEntity<Boolean> isShipped(@RequestParam("trx-no") String trxNo)
    {
        boolean isShipped = service.isShipped(trxNo);
        return new ResponseEntity<>(isShipped, HttpStatus.OK);
    }

    @RequestMapping(value = "/ship/insert-details",
            method = RequestMethod.POST)
    public ResponseEntity<Boolean> insertShipDetails(@RequestParam("region-code") String regionCode,
                                                     @RequestParam("driver-code") String driverCode,
                                                     @RequestParam("src-trx-no") String srcTrxNo,
                                                     @RequestParam("vehicle-code") String vehicleCode,
                                                     @RequestParam("user-id") String userID,
                                                     @RequestParam(value = "ship-status", required = false) String shipStatus)
    {
        boolean result = service.insertShipDetails(regionCode, driverCode, srcTrxNo, vehicleCode, userID, shipStatus);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/ship/create-doc", method = RequestMethod.POST)
    public ResponseEntity<Boolean> createShipDoc(@RequestParam("user-id") String userID)
    {
        boolean result = service.createShipDoc(userID);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/pack-waiting", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<PickTrx>> waitingPackTrx(@RequestParam("trx-no") String trxNo)
    {
        List<PickTrx> trxList = service.getWaitingPackItems(trxNo);
        return new ResponseEntity<>(trxList, HttpStatus.OK);
    }

    @RequestMapping(value = "/split-trx", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<PickTrx>> getSplitTrxList(@RequestParam("bp-code") String bpCode,
                                                         @RequestParam("inv-code") String invCode,
                                                         @RequestParam("qty") double qty)
    {
        List<PickTrx> trxList = service.getSplitTrxList(bpCode, invCode, qty);
        return new ResponseEntity<>(trxList, HttpStatus.OK);
    }

    @RequestMapping(value = "/create-transfer", method = RequestMethod.POST)
    public ResponseEntity<Boolean> createTransfer(@RequestBody List<PickTrx> trxList,
                                                  @RequestParam("src-whs") String srcWhsCode,
                                                  @RequestParam("trg-whs") String trgWhsCode,
                                                  @RequestParam("user-id") String userId)
    {
        boolean result = service.createTransfer(trxList, srcWhsCode, trgWhsCode, userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/approve-prd/insert", consumes = "application/json;charset=UTF-8")
    public ResponseEntity<Boolean> insertProductApproveData(
            @RequestBody List<PickTrx> trxList,
            @RequestParam("user-id") String userId,
            @RequestParam(value = "notes", required = false) String notes,
            @RequestParam("status") int status)
    {
        boolean result;
        result = service.insertProductApproveData(trxList, userId, URLDecoder.decode(notes, StandardCharsets.UTF_8), status);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/approve-prd/list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<PickTrx>> getApproveDocList()
    {
        List<PickTrx> trxList = service.getApproveTrxList();
        return new ResponseEntity<>(trxList, HttpStatus.OK);
    }

    @PostMapping(value = "/create-internal-use", consumes = "application/json;charset=UTF-8")
    public ResponseEntity<Response> createInternalUseDoc(
            @RequestBody List<PickTrx> trxList,
            @RequestParam("user-id") String userId,
            @RequestParam("whs-code") String whsCode,
            @RequestParam("exp-center-code") String expCenterCode,
            @RequestParam(value = "notes", required = false) String notes)
    {
        Response result;
        result = service.createInternalUseDoc(trxList, userId, whsCode, expCenterCode,
                                              URLDecoder.decode(notes, StandardCharsets.UTF_8));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
