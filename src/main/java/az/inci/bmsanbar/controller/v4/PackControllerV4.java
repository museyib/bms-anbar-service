package az.inci.bmsanbar.controller.v4;

import az.inci.bmsanbar.model.PickDoc;
import az.inci.bmsanbar.model.PickTrx;
import az.inci.bmsanbar.model.v2.CollectTrxRequest;
import az.inci.bmsanbar.model.v4.Response;
import az.inci.bmsanbar.services.v4.PackServiceV4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v4/pack")
@RestController
public class PackControllerV4
{
    private PackServiceV4 service;

    @Autowired
    public void setService(PackServiceV4 service)
    {
        this.service = service;
    }

    @GetMapping(value = "/get-doc", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<PickDoc>> getPackDoc(@RequestParam("approve-user") String approveUser,
                                               @RequestParam("mode") int mode)
    {
        PickDoc result = service.getDoc(approveUser, mode);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @PostMapping(value = "/collect")
    public ResponseEntity<Response<Void>> collectTrx(@RequestParam("trx-no") String trxNo,
                                               @RequestBody List<CollectTrxRequest> data)
    {
        service.collectTrx(data, trxNo);
        return ResponseEntity.ok(Response.getSuccessResponse());
    }

    @GetMapping(value = "/waiting-docs", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<List<PickDoc>>> packDocList(@RequestParam("user-id") String userId)
    {
        List<PickDoc> result = service.getDocList(userId);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @GetMapping(value = "/waiting-doc-items", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<List<PickTrx>>> waitingPackTrx(@RequestParam("trx-no") String trxNo)
    {
        List<PickTrx> result = service.getWaitingPackItems(trxNo);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @GetMapping(value = "/report", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<Integer>> getReport(@RequestParam("start-date") String startDate,
                                              @RequestParam("end-date") String endDate,
                                              @RequestParam("user-id") String pickUser)
    {
        Integer result = service.getReport(startDate, endDate, pickUser);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @GetMapping(value = "/report-actual", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<Integer>> getReportActual(@RequestParam("start-date") String startDate,
                                                    @RequestParam("end-date") String endDate,
                                                    @RequestParam("user-id") String pickUser)
    {
        Integer result = service.getReportActual(startDate, endDate, pickUser);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }
}
