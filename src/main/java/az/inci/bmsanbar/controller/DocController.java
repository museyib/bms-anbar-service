/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.inci.bmsanbar.controller;

import az.inci.bmsanbar.model.Doc;
import az.inci.bmsanbar.services.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author User
 */
@RequestMapping("/doc")
@RestController
public class DocController
{
    private DocService service;

    @Autowired
    public void setService(DocService service)
    {
        this.service = service;
    }

    @RequestMapping(value = "/pick", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Doc> pickDoc(@RequestParam("trx-no") String trxNo,
                                       @RequestParam("pick-user") String pickUser)
    {
        Doc doc = service.getPickDocByTrxNo(trxNo, pickUser);
        return new ResponseEntity<>(doc, HttpStatus.OK);
    }

    @RequestMapping(value = "/pack", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Doc> packDoc(@RequestParam("trx-no") String trxNo)
    {
        Doc doc = service.getPackDocByTrxNo(trxNo);
        return new ResponseEntity<>(doc, HttpStatus.OK);
    }

    @RequestMapping(value = "/pack/all", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<List<Doc>> packDocList(@RequestParam("user-id") String userId)
    {
        List<Doc> doc = service.getPackDocList(userId);
        return new ResponseEntity<>(doc, HttpStatus.OK);
    }

    @RequestMapping(value = "/approve-prd/list", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<List<Doc>> getApproveDocList()
    {
        List<Doc> trxList = service.getApproveDocList();
        return new ResponseEntity<>(trxList, HttpStatus.OK);
    }

    @RequestMapping(value = "/pick/reset", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Boolean> resetPickDoc(@RequestParam("trx-no") String trxNo,
                                                @RequestParam("user-id") String userId)
    {
        boolean result = service.resetPickDoc(trxNo, userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/shipment-is-valid")
    @ResponseBody
    public ResponseEntity<Boolean> shipmentIsValid(@RequestParam("trx-no") String trxNo)
    {
        boolean result = service.shipmentIsValid(trxNo);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/taxed")
    @ResponseBody
    public ResponseEntity<Boolean> isTaxed(@RequestParam("trx-no") String trxNo)
    {
        boolean result = service.isTaxed(trxNo);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
