package com.adnancrnovrsanin.ping.auth;

import com.adnancrnovrsanin.ping.auth.DTOs.AuthenticationRequest;
import com.adnancrnovrsanin.ping.auth.DTOs.AuthenticationResponse;
import com.adnancrnovrsanin.ping.auth.DTOs.InitialRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/otp")
    public ResponseEntity<String> otp(
            @RequestBody InitialRequest request
    ) {
        return authenticationService.otp(request);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return authenticationService.authenticate(request);
    }
}