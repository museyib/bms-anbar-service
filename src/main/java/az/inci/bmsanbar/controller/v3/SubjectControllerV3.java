package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.model.Customer;
import az.inci.bmsanbar.model.ExpCenter;
import az.inci.bmsanbar.model.Sbe;
import az.inci.bmsanbar.model.Whs;
import az.inci.bmsanbar.model.v2.Response;
import az.inci.bmsanbar.services.v3.SubjectServiceV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v3/src")
@RestController
@Slf4j
public class SubjectControllerV3
{
    private SubjectServiceV3 service;

    @Autowired
    public void setService(SubjectServiceV3 service)
    {
        this.service = service;
    }

    @GetMapping(value = "/sbe", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getQty()
    {
        try
        {
            List<Sbe> result = service.getSbeList();
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/customer", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getCustomerList(@RequestParam("sbe-code") String sbeCode)
    {
        try
        {
            List<Customer> result = service.getCustomerList(sbeCode);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/whs", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getWhsList()
    {
        try
        {
            List<Whs> result = service.getWhsList();
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/whs/target", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getTrgWhsList(@RequestParam("user-id") String userId)
    {
        try
        {
            List<Whs> result = service.getTrgWhsList(userId);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/exp-center", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getExpCenterList()
    {
        try
        {
            List<ExpCenter> result = service.getExpCenterList();
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }
}
