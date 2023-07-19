package az.inci.bmsanbar.controller;

import az.inci.bmsanbar.model.Doc;
import az.inci.bmsanbar.services.PickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/pick")
@RestController
public class PickController
{
    private PickService service;

    @Autowired
    public void setService(PickService service)
    {
        this.service = service;
    }

    @RequestMapping(value = "/get-doc", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Doc> pickDoc(@RequestParam("pick-user") String pickUser,
                                       @RequestParam("mode") int mode)
    {
        Doc doc = service.getPickDoc(pickUser, mode);
        return new ResponseEntity<>(doc, HttpStatus.OK);
    }

    @RequestMapping(value = "/collect", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Boolean> collectTrx(@RequestBody String data)
    {
        boolean result = service.collectTrx(data);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Boolean> resetPickDoc(@RequestParam("trx-no") String trxNo,
                                                @RequestParam("user-id") String userId)
    {
        boolean result = service.resetPickDoc(trxNo, userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
