package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.exception.OperationNotCompletedException;
import az.inci.bmsanbar.model.Doc;
import az.inci.bmsanbar.model.Trx;
import az.inci.bmsanbar.model.v2.ProductApproveRequest;
import az.inci.bmsanbar.model.v2.Response;
import az.inci.bmsanbar.model.v2.TransferRequest;
import az.inci.bmsanbar.model.v3.InternalUseRequest;
import az.inci.bmsanbar.services.v3.InvMoveServiceV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v3/inv-move")
@RestController
public class InvMoveControllerV3
{
    private InvMoveServiceV3 service;

    @Autowired
    public void setService(InvMoveServiceV3 service)
    {
        this.service = service;
    }

    @GetMapping(value = "/split-trx", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getSplitTrxList(@RequestParam("bp-code") String bpCode,
                                                    @RequestParam("inv-code") String invCode,
                                                    @RequestParam("qty") double qty)
    {
        try
        {
            List<Trx> trxList = service.getSplitTrxList(bpCode, invCode, qty);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(trxList)
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

    @PostMapping(value = "/create-transfer")
    @ResponseBody
    public ResponseEntity<Response> createTransfer(@RequestBody TransferRequest request)
    {
        try
        {
            service.createTransfer(request);
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

    @PostMapping(value = "/approve-prd/insert", consumes = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> insertProductApproveData(@RequestBody ProductApproveRequest request)
    {
        try
        {
            service.insertProductApproveData(request);
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

    @GetMapping(value = "/approve-prd/doc-list", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getApproveDocList()
    {
        try
        {
            List<Doc> docList = service.getApproveDocList();
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(docList)
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

    @GetMapping(value = "/approve-prd/trx-list", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getApproveTrxList()
    {
        try
        {
            List<Trx> trxList = service.getApproveTrxList();
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(trxList)
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

    @PostMapping(value = "/create-internal-use", consumes = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> createInternalUseDoc(@RequestBody InternalUseRequest request)
    {
        try
        {
            service.createInternalUseDoc(request);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .build());
        }
        catch (OperationNotCompletedException e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(2)
                                             .systemMessage(e.toString())
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
