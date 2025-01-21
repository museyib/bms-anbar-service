/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.model.User;
import az.inci.bmsanbar.model.v2.LoginRequest;
import az.inci.bmsanbar.model.v4.Response;
import az.inci.bmsanbar.services.v3.UserServiceV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static az.inci.bmsanbar.Utilities.getMessage;

/**
 * @author User
 */
@RequestMapping("/v3/user")
@RestController
@Slf4j
public class UserControllerV3
{
    private UserServiceV3 service;

    @Autowired
    public void setService(UserServiceV3 service)
    {
        this.service = service;
    }

    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> all()
    {
        List<User> result = service.all();
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response> login(@RequestBody LoginRequest request)
    {
        boolean b = service.login(request.getUserId(), request.getPassword());
        if(b)
        {
            User result = service.getById(request.getUserId());
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        else
        {
            return ResponseEntity.ok(Response.getUserErrorResponse("İstifadəçi adı və ya şifrə yanlışdır."));
        }
    }
}
