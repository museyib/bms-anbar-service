package az.inci.wms.controller.v4;

import az.inci.wms.model.v4.PickDoc;
import az.inci.wms.model.v4.Response;
import az.inci.wms.services.v4.DocServiceV4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/v4/doc")
@RestController
public class DocControllerV4 {
    private DocServiceV4 service;

    @Autowired
    public void setService(DocServiceV4 service) {
        this.service = service;
    }

    @GetMapping(value = "/pack/all", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<List<PickDoc>>> packDocList(@RequestParam("user-id") String userId) {
        List<PickDoc> result = service.getPackDocList(userId);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @GetMapping(value = "/taxed")
    public ResponseEntity<Response<Boolean>> isTaxed(@RequestParam("trx-no") String trxNo) {
        boolean result = service.isTaxed(trxNo);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }
}
