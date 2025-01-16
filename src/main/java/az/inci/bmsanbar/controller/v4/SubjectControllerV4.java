package az.inci.bmsanbar.controller.v4;

import az.inci.bmsanbar.model.Customer;
import az.inci.bmsanbar.model.ExpCenter;
import az.inci.bmsanbar.model.Sbe;
import az.inci.bmsanbar.model.Whs;
import az.inci.bmsanbar.model.v2.Response;
import az.inci.bmsanbar.services.v4.SubjectServiceV4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/v4/src")
@RestController
public class SubjectControllerV4
{
    private SubjectServiceV4 service;

    @Autowired
    public void setService(SubjectServiceV4 service)
    {
        this.service = service;
    }

    @GetMapping(value = "/sbe", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getQty()
    {
        List<Sbe> result = service.getSbeList();
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @GetMapping(value = "/customer", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getCustomerList(@RequestParam("sbe-code") String sbeCode)
    {
        List<Customer> result = service.getCustomerList(sbeCode);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @GetMapping(value = "/whs", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getWhsList()
    {
        List<Whs> result = service.getWhsList();
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @GetMapping(value = "/whs/target", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getTrgWhsList(@RequestParam("user-id") String userId)
    {
        List<Whs> result = service.getTrgWhsList(userId);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @GetMapping(value = "/exp-center", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getExpCenterList()
    {
        List<ExpCenter> result = service.getExpCenterList();
        return ResponseEntity.ok(Response.getResultResponse(result));
    }
}
