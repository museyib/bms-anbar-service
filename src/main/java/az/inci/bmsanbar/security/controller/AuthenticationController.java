package az.inci.bmsanbar.security.controller;

import az.inci.bmsanbar.model.v2.Response;
import az.inci.bmsanbar.security.domain.AuthenticationRequest;
import az.inci.bmsanbar.security.domain.AuthenticationResponse;
import az.inci.bmsanbar.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3")
public class AuthenticationController
{
    private final AuthenticationService authenticationService;

    @PostMapping("authenticate")
    public ResponseEntity<Response> authenticate(@RequestBody AuthenticationRequest request)
    {
        try {
            AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
            return ResponseEntity.ok(Response.builder()
                    .statusCode(0)
                    .data(authenticationResponse)
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
