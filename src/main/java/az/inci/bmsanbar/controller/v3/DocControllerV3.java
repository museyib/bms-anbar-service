/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.model.Doc;
import az.inci.bmsanbar.model.v2.Response;
import az.inci.bmsanbar.services.v3.DocServiceV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author User
 */
@RequestMapping("/v3/doc")
@RestController
public class DocControllerV3
{
    private DocServiceV3 service;

    @Autowired
    public void setService(DocServiceV3 service)
    {
        this.service = service;
    }

    @GetMapping(value = "/pack/all", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> packDocList(@RequestParam("user-id") String userId)
    {
        try
        {
            List<Doc> docList = service.getPackDocList(userId);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(docList)
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

    @GetMapping(value = "/taxed")
    @ResponseBody
    public ResponseEntity<Response> isTaxed(@RequestParam("trx-no") String trxNo)
    {
        try
        {
            boolean result = service.isTaxed(trxNo);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(result)
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
