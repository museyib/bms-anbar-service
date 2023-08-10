package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.model.Uom;
import az.inci.bmsanbar.model.v2.Response;
import az.inci.bmsanbar.services.v3.UomServiceV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/v3/uom")
@RestController
@Slf4j
public class UomControllerV3
{
    private UomServiceV3 service;

    @Autowired
    public void setService(UomServiceV3 service)
    {
        this.service = service;
    }

    @GetMapping(value = "/all", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getUomList()
    {
        try
        {
            List<Uom> result = service.getUomList();
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }
}
