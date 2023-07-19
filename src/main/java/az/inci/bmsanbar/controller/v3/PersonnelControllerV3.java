package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.model.v2.Response;
import az.inci.bmsanbar.services.v3.PersonnelServiceV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v3/personnel")
@RestController
public class PersonnelControllerV3
{
    private PersonnelServiceV3 service;

    @Autowired
    public void setService(PersonnelServiceV3 service)
    {
        this.service = service;
    }

    @GetMapping(value = "/get-name", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getPersonnelName(@RequestParam("per-code") String perCode)
    {
        try
        {
            String perName = service.getPersonnelName(perCode);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(perName)
                                             .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .developerMessage("Server xətası")
                                             .systemMessage(e.toString())
                                             .build());
        }
    }
}
