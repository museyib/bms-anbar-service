package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.model.Doc;
import az.inci.bmsanbar.model.v2.CollectTrxRequest;
import az.inci.bmsanbar.model.v2.ResetPickRequest;
import az.inci.bmsanbar.model.v2.Response;
import az.inci.bmsanbar.services.v3.PickServiceV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v3/pick")
@RestController
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
            Doc doc = service.getPickDoc(pickUser, mode);
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

    @PostMapping(value = "/collect")
    @ResponseBody
    public ResponseEntity<Response> collectTrx(@RequestBody List<CollectTrxRequest> data)
    {
        try
        {
            service.collectTrx(data);
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

    @PostMapping(value = "/reset")
    @ResponseBody
    public ResponseEntity<Response> resetPickDoc(@RequestBody ResetPickRequest request)
    {
        try
        {
            boolean result = service.resetPickDoc(request.getTrxNo(), request.getUserId());
            if(result)
                return ResponseEntity.ok(Response.builder()
                                                 .statusCode(0)
                                                 .developerMessage("Uğurlu əməliyyat")
                                                 .build());
            else
                return ResponseEntity.ok(Response.builder()
                                                 .statusCode(2)
                                                 .developerMessage("Bu əməliyyat üçün səlahiyyətiniz yoxdur")
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

    @GetMapping(value = "/reset-allowed")
    @ResponseBody
    public ResponseEntity<Response> resetAllowed(@RequestParam("user-id") String userId)
    {
        try
        {
            boolean result = service.resetAllowed(userId);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(result)
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
