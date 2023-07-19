package az.inci.bmsanbar.controller;

import az.inci.bmsanbar.model.InvAttribute;
import az.inci.bmsanbar.model.InvBarcode;
import az.inci.bmsanbar.model.Inventory;
import az.inci.bmsanbar.model.Response;
import az.inci.bmsanbar.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/inv")
@RestController
public class InventoryController
{
    private InventoryService service;

    @Autowired
    public void setService(InventoryService service)
    {
        this.service = service;
    }

    @RequestMapping(value = "/qty", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<BigDecimal> getQty(@RequestParam("whs-code") String whsCode,
                                             @RequestParam("inv-code") String invCode)
    {
        BigDecimal qty = service.getQty(whsCode, invCode);
        return new ResponseEntity<>(qty, HttpStatus.OK);
    }

    @RequestMapping(value = "/info-by-barcode", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> getInfoByBarcode(@RequestParam("barcode") String barcode,
                                                   @RequestParam(value = "user-id", required = false) String userId)
    {
        String info = service.getInfoByBarcode(barcode, userId);
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/info-by-inv-code", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> getInfoByInvCode(@RequestParam("inv-code") String invCode,
                                                   @RequestParam(value = "user-id", required = false) String userId)
    {
        String info = service.getInfoByInvCode(invCode, userId);
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/search", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<List<Inventory>> getSearchResult(@RequestParam("keyword") String keyword,
                                                           @RequestParam(value = "in", required = false) String field)
    {
        List<Inventory> inventoryList = service.getSearchResult(keyword, field);
        return new ResponseEntity<>(inventoryList, HttpStatus.OK);
    }

    @RequestMapping(value = "/by-barcode", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Inventory> getInvByBarcode(@RequestParam("barcode") String barcode)
    {
        Inventory inventory = service.getInvByBarcode(barcode);
        return new ResponseEntity<>(inventory, HttpStatus.OK);
    }

    @RequestMapping(value = "/pick-report", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Integer> getPickReport(@RequestParam("start-date") String startDate,
                                                 @RequestParam("end-date") String endDate,
                                                 @RequestParam("user-id") String pickUser)
    {
        Integer qty = service.getPickReport(startDate, endDate, pickUser);
        return new ResponseEntity<>(qty, HttpStatus.OK);
    }

    @RequestMapping(value = "/pack-report", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Integer> getPackReport(@RequestParam("start-date") String startDate,
                                                 @RequestParam("end-date") String endDate,
                                                 @RequestParam("user-id") String approveUser)
    {
        Integer qty = service.getPackReport(startDate, endDate, approveUser);
        return new ResponseEntity<>(qty, HttpStatus.OK);
    }

    @RequestMapping(value = "/attribute-list", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<List<InvAttribute>> getAttributeList(@RequestParam("inv-code") String invCode)
    {
        List<InvAttribute> attributeList = service.getAttributeList(invCode);
        return new ResponseEntity<>(attributeList, HttpStatus.OK);
    }

    @RequestMapping(value = "/attribute-list-by-whs", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<List<InvAttribute>> getAttributeListByWhs(@RequestParam("inv-code") String invCode,
                                                                    @RequestParam("user-id") String userId)
    {
        List<InvAttribute> attributeList = service.getAttributeList(invCode, userId);
        return new ResponseEntity<>(attributeList, HttpStatus.OK);
    }

    @RequestMapping(value = "/barcode-list", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<List<InvBarcode>> getBarcodeList(@RequestParam("inv-code") String invCode)
    {
        List<InvBarcode> barcodeList = service.getBarcodeList(invCode);
        return new ResponseEntity<>(barcodeList, HttpStatus.OK);
    }

    @RequestMapping(value = "/update-attributes",
            consumes = "application/json;charset=UTF-8",
            method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Boolean> updateInvAttributes(@RequestBody List<InvAttribute> attributeList)
    {
        boolean result = service.updateInvAttributes(attributeList);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/update-shelf-barcode", consumes = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Response> updateShelfBarcode(@RequestParam("whs-code") String whsCode,
                                                       @RequestParam("shelf-barcode") String shelfBarcode,
                                                       @RequestBody List<String> attributeList)
    {
        Response result = service.updateShelfBarcode(whsCode, shelfBarcode, attributeList);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/update-barcodes",
            consumes = "application/json;charset=UTF-8",
            method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Boolean> updateInvBarcodes(@RequestBody List<InvBarcode> barcodeList)
    {
        boolean result = service.updateInvBarcodes(barcodeList);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<List<Inventory>> getInvList()
    {
        List<Inventory> inventoryList = service.getInvList();
        return new ResponseEntity<>(inventoryList, HttpStatus.OK);
    }

    @RequestMapping(value = "/by-user-producer-list", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<List<Inventory>> getInvListByUser(@RequestParam("user-id") String userId)
    {
        List<Inventory> inventoryList = service.getInvListByUser(userId);
        return new ResponseEntity<>(inventoryList, HttpStatus.OK);
    }

    @RequestMapping(value = "/whs-sum", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<List<Inventory>> getWhsSumByUser(@RequestParam("user-id") String userId,
                                                           @RequestParam("whs-code") String whsCode)
    {
        List<Inventory> inventoryList = service.getWhsSumByUser(userId, whsCode);
        return new ResponseEntity<>(inventoryList, HttpStatus.OK);
    }

    @RequestMapping(value = "/inv-barcode", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<InvBarcode> getInvBarcode(@RequestParam("barcode") String barcode)
    {
        InvBarcode invBarcode = service.getInvBarcode(barcode);
        return new ResponseEntity<>(invBarcode, HttpStatus.OK);
    }
}
