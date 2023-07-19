/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.inci.bmsanbar.controller.v3;

import az.inci.bmsanbar.model.User;
import az.inci.bmsanbar.model.v2.LoginRequest;
import az.inci.bmsanbar.model.v2.Response;
import az.inci.bmsanbar.services.v3.UserServiceV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author User
 */
@RequestMapping("/v3/user")
@RestController
public class UserControllerV3
{
    private UserServiceV3 service;

    @Autowired
    public void setService(UserServiceV3 service)
    {
        this.service = service;
    }

    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> all()
    {
        try
        {
            List<User> userList = service.all();
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(userList)
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

    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> login(@RequestBody LoginRequest request)
    {
        try
        {
            boolean b = service.login(request.getUserId(), request.getPassword());
            if(b)
            {
                User user = service.getById(request.getUserId());
                return ResponseEntity.ok(Response.builder()
                                                 .statusCode(0)
                                                 .data(user)
                                                 .build());
            }
            else
            {
                return ResponseEntity.ok(Response.builder()
                                                 .statusCode(2)
                                                 .developerMessage("İstifadəçi adı və ya şifrə yanlışdır.")
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
    }
}
