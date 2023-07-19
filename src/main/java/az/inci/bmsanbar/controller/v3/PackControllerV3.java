package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.model.Doc;
import az.inci.bmsanbar.model.Trx;
import az.inci.bmsanbar.model.v2.CollectTrxRequest;
import az.inci.bmsanbar.model.v2.Response;
import az.inci.bmsanbar.services.v3.PackServiceV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v3/pack")
@RestController
public class PackControllerV3
{
    private PackServiceV3 service;

    @Autowired
    public void setService(PackServiceV3 service)
    {
        this.service = service;
    }

    @GetMapping(value = "/get-doc", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getPackDoc(@RequestParam("approve-user") String approveUser,
                                               @RequestParam("mode") int mode)
    {
        try
        {
            Doc doc = service.getPackDoc(approveUser, mode);
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
    @ResponseBody
    public ResponseEntity<Response> collectTrx(@RequestParam("trx-no") String trxNo,
                                               @RequestBody List<CollectTrxRequest> data)
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
    @ResponseBody
    public ResponseEntity<Response> packDocList(@RequestParam("user-id") String userId)
    {
        try
        {
            List<Doc> doc = service.getPackDocList(userId);
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
    @ResponseBody
    public ResponseEntity<Response> waitingPackTrx(@RequestParam("trx-no") String trxNo)
    {
        try
        {
            List<Trx> trxList = service.getWaitingPackItems(trxNo);
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
