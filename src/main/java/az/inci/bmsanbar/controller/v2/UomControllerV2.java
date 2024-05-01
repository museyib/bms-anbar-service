package az.inci.bmsanbar.controller.v2;

import az.inci.bmsanbar.model.Uom;
import az.inci.bmsanbar.model.v2.Response;
import az.inci.bmsanbar.services.UomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/v2/uom")
@RestController
public class UomControllerV2
{
    private UomService service;

    @Autowired
    public void setService(UomService service)
    {
        this.service = service;
    }

    @GetMapping(value = "/all", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getUomList()
    {
        try
        {
            List<Uom> result = service.getUomList();
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
