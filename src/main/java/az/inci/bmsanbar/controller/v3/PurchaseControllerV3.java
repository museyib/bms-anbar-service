package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.model.v4.Response;
import az.inci.bmsanbar.model.v3.PurchaseDoc;
import az.inci.bmsanbar.model.v3.PurchaseTrx;
import az.inci.bmsanbar.model.v3.UpdatePurchaseTrxRequest;
import az.inci.bmsanbar.services.v3.PurchaseServiceV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static az.inci.bmsanbar.Utilities.getMessage;

@RequestMapping("/v3/purchase")
@RestController
@Slf4j
public class PurchaseControllerV3 {

    private PurchaseServiceV3 service;

    @Autowired
    public void setService(PurchaseServiceV3 service) {
        this.service = service;
    }

    @GetMapping("/doc")
    public ResponseEntity<Response> getAll(@RequestParam("user-id") String userId)
    {
        try
        {
            List<PurchaseDoc> result = service.getDocList(userId);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @GetMapping("/trx-list")
    public ResponseEntity<Response> getTrxList(@RequestParam("trx-no") String trxNo)
    {
        try
        {
            List<PurchaseTrx> result = service.getTrxList(trxNo);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @PostMapping("/update-qty")
    public ResponseEntity<Response> updateQty(@RequestBody UpdatePurchaseTrxRequest request)
    {
        try
        {
            service.updatePurchaseQty(request);
            return ResponseEntity.ok(Response.getSuccessResponse());
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }
}
