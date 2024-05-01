package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.model.PickDoc;
import az.inci.bmsanbar.model.PickTrx;
import az.inci.bmsanbar.model.v2.CollectTrxRequest;
import az.inci.bmsanbar.model.v2.Response;
import az.inci.bmsanbar.services.v3.PackServiceV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static az.inci.bmsanbar.Utilities.getMessage;

@RequestMapping("/v3/pack")
@RestController
@Slf4j
public class PackControllerV3
{
    private PackServiceV3 service;

    @Autowired
    public void setService(PackServiceV3 service)
    {
        this.service = service;
    }

    @GetMapping(value = "/get-doc", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getPackDoc(@RequestParam("approve-user") String approveUser,
                                               @RequestParam("mode") int mode)
    {
        try
        {
            PickDoc result = service.getDoc(approveUser, mode);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @PostMapping(value = "/collect")
    public ResponseEntity<Response> collectTrx(@RequestParam("trx-no") String trxNo,
                                               @RequestBody List<CollectTrxRequest> data)
    {
        try
        {
            service.collectTrx(data, trxNo);
            return ResponseEntity.ok(Response.getSuccessResponse());
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @GetMapping(value = "/waiting-docs", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> packDocList(@RequestParam("user-id") String userId)
    {
        try
        {
            List<PickDoc> result = service.getDocList(userId);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @GetMapping(value = "/waiting-doc-items", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> waitingPackTrx(@RequestParam("trx-no") String trxNo)
    {
        try
        {
            List<PickTrx> result = service.getWaitingPackItems(trxNo);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @GetMapping(value = "/report", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getReport(@RequestParam("start-date") String startDate,
                                              @RequestParam("end-date") String endDate,
                                              @RequestParam("user-id") String pickUser)
    {
        try
        {
            Integer result = service.getReport(startDate, endDate, pickUser);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @GetMapping(value = "/report-actual", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getReportActual(@RequestParam("start-date") String startDate,
                                                    @RequestParam("end-date") String endDate,
                                                    @RequestParam("user-id") String pickUser)
    {
        try
        {
            Integer result = service.getReportActual(startDate, endDate, pickUser);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }
}
