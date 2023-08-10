package az.inci.bmsanbar.controller.v2;

import az.inci.bmsanbar.model.InvAttribute;
import az.inci.bmsanbar.model.InvBarcode;
import az.inci.bmsanbar.model.Inventory;
import az.inci.bmsanbar.model.v2.InvInfo;
import az.inci.bmsanbar.model.v2.Response;
import az.inci.bmsanbar.services.v2.InventoryServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/v2/inv")
@RestController
public class InventoryControllerV2
{
    private InventoryServiceV2 service;

    @Autowired
    public void setService(InventoryServiceV2 service)
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
            BigDecimal qty = service.getQty(whsCode, invCode);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(qty)
                                             .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }

    @GetMapping(value = "/info-by-barcode", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getInfoByBarcode(@RequestParam("barcode") String barcode,
                                                     @RequestParam(value = "user-id", required = false) String userId)
    {
        try
        {
            InvInfo info = service.getInfoByBarcode(barcode, userId);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(info)
                                             .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }

    @GetMapping(value = "/info-by-inv-code", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getInfoByInvCode(@RequestParam("inv-code") String invCode,
                                                     @RequestParam(value = "user-id", required = false) String userId)
    {
        try
        {
            InvInfo info = service.getInfoByInvCode(invCode, userId);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(info)
                                             .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }

    @GetMapping(value = "/search", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getSearchResult(@RequestParam("keyword") String keyword,
                                                    @RequestParam(value = "in", required = false) String field)
    {
        try
        {
            List<Inventory> inventoryList = service.getSearchResult(keyword, field);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(inventoryList)
                                             .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }

    @GetMapping(value = "/by-barcode", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getInvByBarcode(@RequestParam("barcode") String barcode)
    {
        try
        {
            Inventory inventory = service.getInvByBarcode(barcode);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(inventory)
                                             .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
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
            Integer qty = service.getPickReport(startDate, endDate, pickUser);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(qty)
                                             .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
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
            Integer qty = service.getPackReport(startDate, endDate, approveUser);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(qty)
                                             .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }

    @GetMapping(value = "/attribute-list", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getAttributeList(@RequestParam("inv-code") String invCode)
    {
        try
        {
            List<InvAttribute> attributeList = service.getAttributeList(invCode);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(attributeList)
                                             .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }

    @GetMapping(value = "/attribute-list-by-whs", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getAttributeListByWhs(@RequestParam("inv-code") String invCode,
                                                          @RequestParam("user-id") String userId)
    {
        try
        {
            List<InvAttribute> attributeList = service.getAttributeList(invCode, userId);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(attributeList)
                                             .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }

    @GetMapping(value = "/barcode-list", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getBarcodeList(@RequestParam("inv-code") String invCode)
    {
        try
        {
            List<InvBarcode> barcodeList = service.getBarcodeList(invCode);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(barcodeList)
                                             .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }

    @PostMapping(value = "/update-attributes", consumes = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> updateInvAttributes(@RequestBody List<InvAttribute> attributeList)
    {
        try
        {
            service.updateInvAttributes(attributeList);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .developerMessage("Uğurlu əməliyyat")
                                             .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
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
                    return ResponseEntity.ok(Response.builder()
                                                     .statusCode(2)
                                                     .developerMessage("Barkod üzrə mal tapılmadı: " + invBarcode)
                                                     .build());
            }
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
        }
        return ResponseEntity.ok(Response.builder()
                                         .statusCode(0)
                                         .build());
    }

//    @PostMapping(value = "/update-barcodes", consumes = "application/json;charset=UTF-8")
//    @ResponseBody
//    public ResponseEntity<Response> updateInvBarcodes(@RequestBody List<InvBarcode> barcodeList)
//    {
//        try
//        {
//            service.updateInvBarcodes(barcodeList);
//            return ResponseEntity.ok(Response.builder()
//                                             .statusCode(0)
//                                             .developerMessage("Uğurlu əməliyyat")
//                                             .build());
//        }
//        catch(Exception e)
//        {
//            return ResponseEntity.ok(Response.builder()
//                                             .statusCode(1)
//                                             .systemMessage(e.toString())
//                                             .developerMessage("Server xətası")
//                                             .build());
//        }
//    }

    @GetMapping(produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getInvList()
    {
        try
        {
            List<Inventory> inventoryList = service.getInvList();
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(inventoryList)
                                             .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }

    @GetMapping(value = "/by-user-producer-list", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getInvListByUser(@RequestParam("user-id") String userId)
    {
        try
        {
            List<Inventory> inventoryList = service.getInvListByUser(userId);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(inventoryList)
                                             .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }

    @GetMapping(value = "/whs-sum", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getWhsSumByUser(@RequestParam("user-id") String userId,
                                                    @RequestParam("whs-code") String whsCode)
    {
        try
        {
            List<Inventory> inventoryList = service.getWhsSumByUser(userId, whsCode);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(inventoryList)
                                             .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }

    @GetMapping(value = "/inv-barcode", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getInvBarcode(@RequestParam("barcode") String barcode)
    {
        try
        {
            InvBarcode invBarcode = service.getInvBarcode(barcode);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(invBarcode)
                                             .build());
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.toString())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }
}
