package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.model.Doc;
import az.inci.bmsanbar.model.v2.CollectTrxRequest;
import az.inci.bmsanbar.model.v2.ResetPickRequest;
import az.inci.bmsanbar.model.v2.Response;
import az.inci.bmsanbar.services.v3.PickServiceV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @ResponseBody
    public ResponseEntity<Response> pickDoc(@RequestParam("pick-user") String pickUser,
                                            @RequestParam("mode") int mode)
    {
        try
        {
            Doc result = service.getPickDoc(pickUser, mode);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @PostMapping(value = "/collect")
    @ResponseBody
    public ResponseEntity<Response> collectTrx(@RequestBody List<CollectTrxRequest> data)
    {
        try
        {
            service.collectTrx(data);
            return ResponseEntity.ok(Response.getSuccessResponse());
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @PostMapping(value = "/reset")
    @ResponseBody
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
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/reset-allowed")
    @ResponseBody
    public ResponseEntity<Response> resetAllowed(@RequestParam("user-id") String userId)
    {
        try
        {
            boolean result = service.resetAllowed(userId);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/report", produces = "application/json;charset=UTF-8")
    @ResponseBody
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
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/report-actual", produces = "application/json;charset=UTF-8")
    @ResponseBody
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
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }
}
