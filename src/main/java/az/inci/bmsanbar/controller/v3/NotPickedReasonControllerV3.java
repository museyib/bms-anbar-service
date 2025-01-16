package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.model.v2.Response;
import az.inci.bmsanbar.model.v3.NotPickedReason;
import az.inci.bmsanbar.services.v3.NotPickedReasonServiceV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static az.inci.bmsanbar.Utilities.getMessage;

@RequestMapping("/v3/not-picked-reason")
@RestController
@Slf4j
public class NotPickedReasonControllerV3
{
    private NotPickedReasonServiceV3 service;

    @Autowired
    public void setService(NotPickedReasonServiceV3 service)
    {
        this.service = service;
    }

    @GetMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> getReasonList()
    {
        try
        {
            List<NotPickedReason> result = service.getReasonList();
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }
}
