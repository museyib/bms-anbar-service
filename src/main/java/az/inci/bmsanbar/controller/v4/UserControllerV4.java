package az.inci.bmsanbar.controller.v4;

import az.inci.bmsanbar.model.v4.LoginRequest;
import az.inci.bmsanbar.model.v4.Response;
import az.inci.bmsanbar.model.v4.User;
import az.inci.bmsanbar.services.v4.UserServiceV4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v4/user")
@RestController
public class UserControllerV4 {
    private UserServiceV4 service;

    @Autowired
    public void setService(UserServiceV4 service) {
        this.service = service;
    }

    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<List<User>>> all() {
        List<User> result = service.all();
        return ResponseEntity.ok(Response.getResultResponse(result));
    }

    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Response<User>> login(@RequestBody LoginRequest request) {
        boolean b = service.login(request.getUserId(), request.getPassword());
        if (b) {
            User result = service.getById(request.getUserId());
            return ResponseEntity.ok(Response.getResultResponse(result));
        } else {
            return ResponseEntity.ok(Response.getUserErrorResponse("İstifadəçi adı və ya şifrə yanlışdır."));
        }
    }
}
