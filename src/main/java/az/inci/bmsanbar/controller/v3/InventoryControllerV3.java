package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.model.InvAttribute;
import az.inci.bmsanbar.model.InvBarcode;
import az.inci.bmsanbar.model.Inventory;
import az.inci.bmsanbar.model.v2.InvInfo;
import az.inci.bmsanbar.model.v2.Response;
import az.inci.bmsanbar.services.v3.InventoryServiceV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/v3/inv")
@RestController
@Slf4j
public class InventoryControllerV3
{
    private InventoryServiceV3 service;

    @Autowired
    public void setService(InventoryServiceV3 service)
    {
        this.service = service;
    }

    @GetMapping(value = "/qty", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getQty(@RequestParam("whs-code") String whsCode,
                                           @RequestParam("inv-code") String invCode)
    {
        try
        {
            BigDecimal result = service.getQty(whsCode, invCode);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/info-by-barcode", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getInfoByBarcode(@RequestParam("barcode") String barcode,
                                                     @RequestParam("user-id") String userId)
    {
        try
        {
            InvInfo result = service.getInfoByBarcode(barcode, userId);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/info-by-inv-code", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getInfoByInvCode(@RequestParam("inv-code") String invCode,
                                                     @RequestParam("user-id") String userId)
    {
        try
        {
            InvInfo result = service.getInfoByInvCode(invCode, userId);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/search", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getSearchResult(@RequestParam("keyword") String keyword,
                                                    @RequestParam("in") String field)
    {
        try
        {
            List<Inventory> result = service.getSearchResult(keyword, field);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/by-barcode", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getInvByBarcode(@RequestParam("barcode") String barcode)
    {
        try
        {
            Inventory result = service.getInvByBarcode(barcode);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/pick-report", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getPickReport(@RequestParam("start-date") String startDate,
                                                  @RequestParam("end-date") String endDate,
                                                  @RequestParam("user-id") String pickUser)
    {
        try
        {
            Integer result = service.getPickReport(startDate, endDate, pickUser);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/pack-report", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getPackReport(@RequestParam("start-date") String startDate,
                                                  @RequestParam("end-date") String endDate,
                                                  @RequestParam("user-id") String approveUser)
    {
        try
        {
            Integer result = service.getPackReport(startDate, endDate, approveUser);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/attribute-list", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getAttributeList(@RequestParam("inv-code") String invCode)
    {
        try
        {
            List<InvAttribute> result = service.getAttributeList(invCode);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/attribute-list-by-whs", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getAttributeListByWhs(@RequestParam("inv-code") String invCode,
                                                          @RequestParam("user-id") String userId)
    {
        try
        {
            List<InvAttribute> result = service.getAttributeList(invCode, userId);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/barcode-list", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getBarcodeList(@RequestParam("inv-code") String invCode)
    {
        try
        {
            List<InvBarcode> result = service.getBarcodeList(invCode);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @PostMapping(value = "/update-attributes", consumes = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> updateInvAttributes(@RequestBody List<InvAttribute> attributeList)
    {
        try
        {
            service.updateInvAttributes(attributeList);
            return ResponseEntity.ok(Response.getSuccessResponse());
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @PostMapping(value = "/update-shelf-barcode", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Response> updateShelfBarcode(@RequestParam("whs-code") String whsCode,
                                                       @RequestParam("shelf-barcode") String shelfBarcode,
                                                       @RequestBody List<String> invBarcodeList)
    {
        try
        {
            for(String invBarcode : invBarcodeList)
            {
                boolean result = service.updateShelfBarcode(whsCode, shelfBarcode, invBarcode);
                if(!result)
                    return ResponseEntity.ok(Response.getUserErrorResponse("Barkod üzrə mal tapılmadı: " + invBarcode));
            }
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
        return ResponseEntity.ok(Response.getSuccessResponse());
    }

//    @PostMapping(value = "/update-barcodes", consumes = "application/json;charset=UTF-8")
//    @ResponseBody
//    public ResponseEntity<Response> updateInvBarcodes(@RequestBody List<InvBarcode> barcodeList)
//    {
//        try
//        {
//            service.updateInvBarcodes(barcodeList);
//            return ResponseEntity.ok(Response.getSuccessResponse());
//        }
//        catch(Exception e)
//        {
//            log.error(e.toString());
//            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
//        }
//    }

    @GetMapping(produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getInvList()
    {
        try
        {
            List<Inventory> result = service.getInvList();
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/by-user-producer-list", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getInvListByUser(@RequestParam("user-id") String userId)
    {
        try
        {
            List<Inventory> result = service.getInvListByUser(userId);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/whs-sum", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getWhsSumByUser(@RequestParam("user-id") String userId,
                                                    @RequestParam("whs-code") String whsCode)
    {
        try
        {
            List<Inventory> result = service.getWhsSumByUser(userId, whsCode);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }

    @GetMapping(value = "/inv-barcode", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getInvBarcode(@RequestParam("barcode") String barcode)
    {
        try
        {
            InvBarcode result = service.getInvBarcode(barcode);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            log.error(e.toString());
            return ResponseEntity.ok(Response.getServerErrorResponse(e.toString()));
        }
    }
}
