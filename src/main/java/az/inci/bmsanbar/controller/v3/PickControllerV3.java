package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.model.PickDoc;
import az.inci.bmsanbar.model.v2.CollectTrxRequest;
import az.inci.bmsanbar.model.v2.ResetPickRequest;
import az.inci.bmsanbar.model.v4.Response;
import az.inci.bmsanbar.services.v3.PickServiceV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static az.inci.bmsanbar.Utilities.getMessage;

@RequestMapping("/v3/pick")
@RestController
@Slf4j
public class PickControllerV3
{
    private PickServiceV3 service;

    @Autowired
    public void setService(PickServiceV3 service)
    {
        this.service = service;
    }

    @GetMapping(value = "/get-doc", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> pickDoc(@RequestParam("pick-user") String pickUser,
                                            @RequestParam("mode") int mode)
    {
        try
        {
            PickDoc result = service.getPickDoc(pickUser, mode);
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
    public ResponseEntity<Response> collectTrx(@RequestBody List<CollectTrxRequest> data)
    {
        try
        {
            service.collectTrx(data);
            return ResponseEntity.ok(Response.getSuccessResponse());
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @PostMapping(value = "/reset")
    public ResponseEntity<Response> resetPickDoc(@RequestBody ResetPickRequest request)
    {
        try
        {
            boolean result = service.resetPickDoc(request.getTrxNo(), request.getUserId());
            if(result)
                return ResponseEntity.ok(Response.getSuccessResponse());
            else
                return ResponseEntity.ok(Response.getUserErrorResponse("Bu əməliyyat üçün səlahiyyətiniz yoxdur"));
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @GetMapping(value = "/reset-allowed")
    public ResponseEntity<Response> resetAllowed(@RequestParam("user-id") String userId)
    {
        try
        {
            boolean result = service.resetAllowed(userId);
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
