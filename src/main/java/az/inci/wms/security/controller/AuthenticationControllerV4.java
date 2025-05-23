package az.inci.wms.security.controller;

import az.inci.wms.model.v4.Response;
import az.inci.wms.security.domain.AuthenticationRequest;
import az.inci.wms.security.domain.AuthenticationResponse;
import az.inci.wms.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v4")
@Slf4j
public class AuthenticationControllerV4 {
    private final AuthenticationService authenticationService;

    @PostMapping("authenticate")
    public ResponseEntity<Response<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse result = authenticationService.authenticate(request);
        return ResponseEntity.ok(Response.getResultResponse(result));
    }
}
