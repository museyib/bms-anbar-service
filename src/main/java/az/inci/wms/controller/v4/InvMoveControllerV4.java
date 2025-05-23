package az.inci.wms.controller.v4;

import az.inci.wms.model.v4.*;
import az.inci.wms.services.v4.InvMoveServiceV4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v4/inv-move")
@RestController
public class InvMoveControllerV4 {
    private InvMoveServiceV4 service;

    @Autowired
    public void setService(InvMoveServiceV4 service) {
        this.service = service;
    }

    @GetMapping(value = "/split-trx", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<List<PickTrx>>> getSplitTrxList(@RequestParam("bp-code") String bpCode,
                                                                   @RequestParam("inv-code") String invCode,
                                                                   @RequestParam("qty") double qty) {
        List<PickTrx> result = service.getSplitTrxList(bpCode, invCode, qty);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @PostMapping(value = "/create-transfer")
    public ResponseEntity<Response<Void>> createTransfer(@RequestBody TransferRequest request) {
        service.createTransfer(request);
        return ResponseEntity.ok(Response.getSuccessResponse());
    }

    @PostMapping(value = "/approve-prd/insert", consumes = "application/json;charset=UTF-8")
    public ResponseEntity<Response<Void>> insertProductApproveData(@RequestBody ProductApproveRequest request) {
        service.insertProductApproveData(request);
        return ResponseEntity.ok(Response.getSuccessResponse());
    }

    @GetMapping(value = "/approve-prd/doc-list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<List<PickDoc>>> getApproveDocList() {
        List<PickDoc> result = service.getApproveDocList();
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @GetMapping(value = "/approve-prd/trx-list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<List<PickTrx>>> getApproveTrxList() {
        List<PickTrx> result = service.getApproveTrxList();
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @PostMapping(value = "/create-internal-use", consumes = "application/json;charset=UTF-8")
    public ResponseEntity<Response<Void>> createInternalUseDoc(@RequestBody InternalUseRequest request) {
        service.createInternalUseDoc(request);
        return ResponseEntity.ok(Response.getSuccessResponse());
    }
}
