/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.inci.bmsanbar.controller;

import az.inci.bmsanbar.model.User;
import az.inci.bmsanbar.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author User
 */
@RequestMapping("/user")
@RestController
public class UserController
{
    private UserService service;

    @Autowired
    public void setService(UserService service)
    {
        this.service = service;
    }

    @RequestMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<User>> all()
    {
        List<User> userList = service.all();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<User> login(@RequestParam("id") String id,
                                      @RequestParam("password") String password)
    {
        User user = new User();
        boolean b = service.login(id, password);
        if(b)
        {
            user = service.getById(id);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
