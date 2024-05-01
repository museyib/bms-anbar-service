package az.inci.bmsanbar.controller;

import az.inci.bmsanbar.model.Uom;
import az.inci.bmsanbar.services.UomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/uom")
@RestController
public class UomController
{
    private UomService service;

    @Autowired
    public void setService(UomService service)
    {
        this.service = service;
    }

    @GetMapping(value = "/all", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<Uom>> getUomList()
    {
        return new ResponseEntity<>(service.getUomList(), HttpStatus.OK);
    }
}
