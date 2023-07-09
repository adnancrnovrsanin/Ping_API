package com.adnancrnovrsanin.ping.auth;

import com.adnancrnovrsanin.ping.PhoneNumberUtils.PhoneNumberVerificationService;
import com.adnancrnovrsanin.ping.auth.DTOs.AuthenticationRequest;
import com.adnancrnovrsanin.ping.auth.DTOs.AuthenticationResponse;
import com.adnancrnovrsanin.ping.auth.DTOs.InitialRequest;
import com.adnancrnovrsanin.ping.jwt.JwtService;
import com.adnancrnovrsanin.ping.user.DTOs.UserDto;
import com.adnancrnovrsanin.ping.user.User;
import com.adnancrnovrsanin.ping.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationProvider authProvider;
    private final PhoneNumberVerificationService phoneNumberVerificationService;

    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        var isNewUser = false;
        var requestUser = UserDto.builder()
                .phoneNumber(request.getPhoneNumber())
                .build();

        var user = repository.findByPhoneNumber(request.getPhoneNumber())
                .orElse(null);
        if (user == null) {
            user = User.builder()
                    .phoneNumber(request.getPhoneNumber())
                    .build();
            repository.save(user);
            isNewUser = true;
        }

        authProvider.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPhoneNumber(),
                        request.getOtp()
                )
        );
        var jwtToken = jwtService.generateToken(user);
        var response =  AuthenticationResponse.builder()
                .user(
                        UserDto.builder()
                                .phoneNumber(user.getPhoneNumber())
                                .displayName(user.getDisplayName())
                                .about(user.getAbout())
                                .profilePictureUrl(user.getProfilePictureUrl())
                                .build()
                )
                .token(jwtToken)
                .isNewUser(isNewUser)
                .tokenExpiration(jwtService.getExpirationDateFromToken(jwtToken).toString())
                .build();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<String> otp(InitialRequest request) {
        phoneNumberVerificationService.generateOTP(request.getPhoneNumber());

        return new ResponseEntity<>("OTP has been successfully generated, and awaits your verification", HttpStatus.OK);
    }
}
