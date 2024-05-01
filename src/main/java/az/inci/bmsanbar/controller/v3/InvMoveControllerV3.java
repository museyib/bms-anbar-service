package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.exception.OperationNotCompletedException;
import az.inci.bmsanbar.model.PickDoc;
import az.inci.bmsanbar.model.PickTrx;
import az.inci.bmsanbar.model.v2.ProductApproveRequest;
import az.inci.bmsanbar.model.v2.Response;
import az.inci.bmsanbar.model.v2.TransferRequest;
import az.inci.bmsanbar.model.v3.InternalUseRequest;
import az.inci.bmsanbar.services.v3.InvMoveServiceV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static az.inci.bmsanbar.Utilities.getMessage;

@RequestMapping("/v3/inv-move")
@RestController
@Slf4j
public class InvMoveControllerV3
{
    private InvMoveServiceV3 service;

    @Autowired
    public void setService(InvMoveServiceV3 service)
    {
        this.service = service;
    }

    @GetMapping(value = "/split-trx", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getSplitTrxList(@RequestParam("bp-code") String bpCode,
                                                    @RequestParam("inv-code") String invCode,
                                                    @RequestParam("qty") double qty)
    {
        try
        {
            List<PickTrx> result = service.getSplitTrxList(bpCode, invCode, qty);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @PostMapping(value = "/create-transfer")
    public ResponseEntity<Response> createTransfer(@RequestBody TransferRequest request)
    {
        try
        {
            service.createTransfer(request);
            return ResponseEntity.ok(Response.getSuccessResponse());
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @PostMapping(value = "/approve-prd/insert", consumes = "application/json;charset=UTF-8")
    public ResponseEntity<Response> insertProductApproveData(@RequestBody ProductApproveRequest request)
    {
        try
        {
            service.insertProductApproveData(request);
            return ResponseEntity.ok(Response.getSuccessResponse());
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @GetMapping(value = "/approve-prd/doc-list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getApproveDocList()
    {
        try
        {
            List<PickDoc> result = service.getApproveDocList();
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @GetMapping(value = "/approve-prd/trx-list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getApproveTrxList()
    {
        try
        {
            List<PickTrx> result = service.getApproveTrxList();
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @PostMapping(value = "/create-internal-use", consumes = "application/json;charset=UTF-8")
    public ResponseEntity<Response> createInternalUseDoc(@RequestBody InternalUseRequest request)
    {
        try
        {
            service.createInternalUseDoc(request);
            return ResponseEntity.ok(Response.getSuccessResponse());
        }
        catch (OperationNotCompletedException e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getUserErrorResponse(e.toString()));
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }
}
