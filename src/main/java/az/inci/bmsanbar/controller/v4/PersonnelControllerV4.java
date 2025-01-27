package az.inci.bmsanbar.controller.v4;

import az.inci.bmsanbar.model.v4.Response;
import az.inci.bmsanbar.services.v4.PersonnelServiceV4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v4/personnel")
@RestController
public class PersonnelControllerV4 {
    private PersonnelServiceV4 service;

    @Autowired
    public void setService(PersonnelServiceV4 service) {
        this.service = service;
    }

    @GetMapping(value = "/get-name", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<String>> getPersonnelName(@RequestParam("per-code") String perCode) {
        String result = service.getPersonnelName(perCode);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }
}
