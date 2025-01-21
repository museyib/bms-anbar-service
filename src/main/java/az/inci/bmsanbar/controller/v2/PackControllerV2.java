package az.inci.bmsanbar.controller.v2;

import az.inci.bmsanbar.model.PickDoc;
import az.inci.bmsanbar.model.PickTrx;
import az.inci.bmsanbar.model.v2.CollectTrxRequest;
import az.inci.bmsanbar.model.v4.Response;
import az.inci.bmsanbar.services.v2.PackServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v2/pack")
@RestController
public class PackControllerV2
{
    private PackServiceV2 service;

    @Autowired
    public void setService(PackServiceV2 service)
    {
        this.service = service;
    }

    @GetMapping(value = "/get-doc", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getPackDoc(@RequestParam("approve-user") String approveUser,
                                               @RequestParam("mode") int mode)
    {
        try
        {
            PickDoc doc = service.getPackDoc(approveUser, mode);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(doc)
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

    @PostMapping(value = "/collect")
    public ResponseEntity<Response> collectTrx(@RequestBody List<CollectTrxRequest> data,
                                               @RequestParam("trx-no") String trxNo)
    {
        try
        {
            service.collectTrx(data, trxNo);
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

    @GetMapping(value = "/waiting-docs", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> packDocList(@RequestParam("user-id") String userId)
    {
        try
        {
            List<PickDoc> doc = service.getPackDocList(userId);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(doc)
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

    @GetMapping(value = "/waiting-doc-items", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> waitingPackTrx(@RequestParam("trx-no") String trxNo)
    {
        try
        {
            List<PickTrx> trxList = service.getWaitingPackItems(trxNo);
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
}
