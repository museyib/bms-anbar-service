package az.inci.bmsanbar.controller;

import az.inci.bmsanbar.model.PickDoc;
import az.inci.bmsanbar.model.PickTrx;
import az.inci.bmsanbar.services.PackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/pack")
@RestController
public class PackController
{
    private PackService service;

    @Autowired
    public void setService(PackService service)
    {
        this.service = service;
    }

    @RequestMapping(value = "/get-doc", produces = "application/json;charset=UTF-8")
    public ResponseEntity<PickDoc> pickDoc(@RequestParam("approve-user") String approveUser,
                                           @RequestParam("mode") int mode)
    {
        PickDoc doc = service.getPackDoc(approveUser, mode);
        return new ResponseEntity<>(doc, HttpStatus.OK);
    }

    @RequestMapping(value = "/collect", method = RequestMethod.POST)
    public ResponseEntity<Boolean> collectTrx(@RequestBody String data,
                                              @RequestParam("trx-no") String trxNo)
    {
        boolean result = service.collectTrx(data, trxNo);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/waiting-docs", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<PickDoc>> packDocList(@RequestParam("user-id") String userId)
    {
        List<PickDoc> doc = service.getPackDocList(userId);
        return new ResponseEntity<>(doc, HttpStatus.OK);
    }

    @RequestMapping(value = "/waiting-doc-items", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<PickTrx>> waitingPackTrx(@RequestParam("trx-no") String trxNo)
    {
        List<PickTrx> trxList = service.getWaitingPackItems(trxNo);
        return new ResponseEntity<>(trxList, HttpStatus.OK);
    }
}
