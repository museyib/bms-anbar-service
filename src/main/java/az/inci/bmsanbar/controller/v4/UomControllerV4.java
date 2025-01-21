package az.inci.bmsanbar.controller.v4;

import az.inci.bmsanbar.model.Uom;
import az.inci.bmsanbar.model.v4.Response;
import az.inci.bmsanbar.services.v4.UomServiceV4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/v4/uom")
@RestController
public class UomControllerV4
{
    private UomServiceV4 service;

    @Autowired
    public void setService(UomServiceV4 service)
    {
        this.service = service;
    }

    @GetMapping(value = "/all", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<List<Uom>>> getUomList()
    {
        List<Uom> result = service.getUomList();
        return ResponseEntity.ok(Response.getResultResponse(result));
    }
}
