package az.inci.bmsanbar.controller;

import az.inci.bmsanbar.model.ShipDoc;
import az.inci.bmsanbar.services.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/logistics")
@RestController
public class LogisticsController
{
    private LogisticsService service;

    @Autowired
    public void setService(LogisticsService service)
    {
        this.service = service;
    }

    @RequestMapping(value = "/sending", produces = "application/json;charset=UTF-8")
    public ResponseEntity<String[]> getDocInfoForSendingByTrxNo(@RequestParam("trx-no") String trxNo)
    {
        String[] result = service.getDocInfoForSendingByTrxNo(trxNo);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/return", produces = "application/json;charset=UTF-8")
    public ResponseEntity<String[]> getDocInfoForReturn(@RequestParam("trx-no") String trxNo)
    {
        String[] result = service.getDocInfoForReturnByTrxNo(trxNo);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/delivery", produces = "application/json;charset=UTF-8")
    public ResponseEntity<String[]> getDocInfoForDeliveryByTrxNo(@RequestParam("trx-no") String trxNo)
    {
        String[] result = service.getDocInfoForDeliveryByTrxNo(trxNo);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/confirm", produces = "application/json;charset=UTF-8")
    public ResponseEntity<String[]> getDocInfoForConfirmByTrxNo(@RequestParam("trx-no") String trxNo)
    {
        String[] result = service.getDocInfoForConfirmByTrxNo(trxNo);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/check-doc-status", produces = "application/json;charset=UTF-8")
    public ResponseEntity<String[]> getDocInfoForStatusByTrxNo(@RequestParam("trx-no") String trxNo)
    {
        String[] result = service.getDocInfoForStatusByTrxNo(trxNo);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/doc-list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<ShipDoc>> getDocList(@RequestParam("start-date") String startDate,
                                                    @RequestParam("end-date") String endDate)
    {
        List<ShipDoc> result = service.getDocList(startDate, endDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/change-doc-status", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Boolean> changeDocStatus(@RequestParam("trx-no") String trxNo,
                                                   @RequestParam("status") String status,
                                                   @RequestParam("note") String note,
                                                   @RequestParam("deliver-person") String deliverPerson)
    {
        Boolean result = service.changeDocStatus(trxNo, status, note, deliverPerson);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/update-location", method = RequestMethod.POST)
    public ResponseEntity<Boolean> updateDocLocation(@RequestParam("latitude") String lat,
                                                     @RequestParam("aptitude") String apt,
                                                     @RequestParam("address") String address,
                                                     @RequestParam("user-id") String userId)
    {
        Boolean result = service.updateDocLocation(lat, apt, address, userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
