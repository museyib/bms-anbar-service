package az.inci.bmsanbar.controller;

import az.inci.bmsanbar.model.Customer;
import az.inci.bmsanbar.model.ExpCenter;
import az.inci.bmsanbar.model.Sbe;
import az.inci.bmsanbar.model.Whs;
import az.inci.bmsanbar.services.SrcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/src")
@RestController
public class SrcController
{
    private SrcService service;

    @Autowired
    public void setService(SrcService service)
    {
        this.service = service;
    }

    @RequestMapping(value = "/sbe", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<Sbe>> getQty()
    {
        List<Sbe> list = service.getSbeList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/customer", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<Customer>> getCustomerList(@RequestParam("sbe-code") String sbeCode)
    {
        List<Customer> list = service.getCustomerList(sbeCode);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/whs", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<Whs>> getWhsList()
    {
        List<Whs> list = service.getWhsList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/whs/target", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<Whs>> getTrgWhsList(@RequestParam("user-id") String userId)
    {
        List<Whs> list = service.getTrgWhsList(userId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/exp-center", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<ExpCenter>> getExpCenterList()
    {
        List<ExpCenter> list = service.getExpCenterList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
