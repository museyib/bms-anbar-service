/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.model.PickDoc;
import az.inci.bmsanbar.model.v4.Response;
import az.inci.bmsanbar.services.v3.DocServiceV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static az.inci.bmsanbar.Utilities.getMessage;

/**
 * @author User
 */
@RequestMapping("/v3/doc")
@RestController
@Slf4j
public class DocControllerV3
{
    private DocServiceV3 service;

    @Autowired
    public void setService(DocServiceV3 service)
    {
        this.service = service;
    }

    @GetMapping(value = "/pack/all", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> packDocList(@RequestParam("user-id") String userId)
    {
        try
        {
            List<PickDoc> result = service.getPackDocList(userId);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @GetMapping(value = "/taxed")
    public ResponseEntity<Response> isTaxed(@RequestParam("trx-no") String trxNo)
    {
        try
        {
            boolean result = service.isTaxed(trxNo);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }
}
