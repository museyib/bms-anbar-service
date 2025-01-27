package az.inci.bmsanbar.controller.v4;

import az.inci.bmsanbar.model.v4.CollectTrxRequest;
import az.inci.bmsanbar.model.v4.PickDoc;
import az.inci.bmsanbar.model.v4.ResetPickRequest;
import az.inci.bmsanbar.model.v4.Response;
import az.inci.bmsanbar.services.v4.PickServiceV4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v4/pick")
@RestController
public class PickControllerV4 {
    private PickServiceV4 service;

    @Autowired
    public void setService(PickServiceV4 service) {
        this.service = service;
    }

    @GetMapping(value = "/get-doc", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<PickDoc>> pickDoc(@RequestParam("pick-user") String pickUser,
                                                     @RequestParam("mode") int mode) {
        PickDoc result = service.getPickDoc(pickUser, mode);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @GetMapping(value = "/get-doc-by-no", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<PickDoc>> pickDocByTrxNo(@RequestParam("trx-no") String trxNo,
                                                            @RequestParam("pick-user") String pickUser) {
        PickDoc result = service.getPickDocByTrxNo(trxNo, pickUser);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @PostMapping(value = "/collect")
    public ResponseEntity<Response<Void>> collectTrx(@RequestBody List<CollectTrxRequest> data) {
        service.collectTrx(data);
        return ResponseEntity.ok(Response.getSuccessResponse());
    }

    @PostMapping(value = "/reset")
    public ResponseEntity<Response<Boolean>> resetPickDoc(@RequestBody ResetPickRequest request) {
        boolean result = service.resetPickDoc(request.getTrxNo(), request.getUserId());
        if (result)
            return ResponseEntity.ok(Response.getSuccessResponse());
        else
            return ResponseEntity.ok(Response.getUserErrorResponse("Bu əməliyyat üçün səlahiyyətiniz yoxdur"));
    }

    @GetMapping(value = "/reset-allowed")
    public ResponseEntity<Response<Boolean>> resetAllowed(@RequestParam("user-id") String userId) {
        boolean result = service.resetAllowed(userId);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @GetMapping(value = "/report", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<Integer>> getReport(@RequestParam("start-date") String startDate,
                                                       @RequestParam("end-date") String endDate,
                                                       @RequestParam("user-id") String pickUser) {
        Integer result = service.getReport(startDate, endDate, pickUser);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @GetMapping(value = "/report-actual", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<Integer>> getReportActual(@RequestParam("start-date") String startDate,
                                                             @RequestParam("end-date") String endDate,
                                                             @RequestParam("user-id") String pickUser) {
        Integer result = service.getReportActual(startDate, endDate, pickUser);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }
}
