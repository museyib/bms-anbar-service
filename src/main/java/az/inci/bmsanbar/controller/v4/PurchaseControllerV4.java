package az.inci.bmsanbar.controller.v4;

import az.inci.bmsanbar.model.v2.Response;
import az.inci.bmsanbar.model.v3.PurchaseDoc;
import az.inci.bmsanbar.model.v3.PurchaseTrx;
import az.inci.bmsanbar.model.v3.UpdatePurchaseTrxRequest;
import az.inci.bmsanbar.services.v4.PurchaseServiceV4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v4/purchase")
@RestController
public class PurchaseControllerV4 {

    private PurchaseServiceV4 service;

    @Autowired
    public void setService(PurchaseServiceV4 service) {
        this.service = service;
    }

    @GetMapping("/doc")
    public ResponseEntity<Response> getAll(@RequestParam("user-id") String userId)
    {
        List<PurchaseDoc> result = service.getDocList(userId);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @GetMapping("/trx-list")
    public ResponseEntity<Response> getTrxList(@RequestParam("trx-no") String trxNo)
    {
        List<PurchaseTrx> result = service.getTrxList(trxNo);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @PostMapping("/update-qty")
    public ResponseEntity<Response> updateQty(@RequestBody UpdatePurchaseTrxRequest request)
    {
        service.updatePurchaseQty(request);
        return ResponseEntity.ok(Response.getSuccessResponse());
    }
}
