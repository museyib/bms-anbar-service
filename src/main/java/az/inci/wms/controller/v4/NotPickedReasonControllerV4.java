package az.inci.wms.controller.v4;

import az.inci.wms.model.v4.NotPickedReason;
import az.inci.wms.model.v4.Response;
import az.inci.wms.services.v4.NotPickedReasonServiceV4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/v4/not-picked-reason")
@RestController
public class NotPickedReasonControllerV4 {
    private NotPickedReasonServiceV4 service;

    @Autowired
    public void setService(NotPickedReasonServiceV4 service) {
        this.service = service;
    }

    @GetMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<List<NotPickedReason>>> getReasonList() {
        List<NotPickedReason> result = service.getReasonList();
        return ResponseEntity.ok(Response.getResultResponse(result));
    }
}
