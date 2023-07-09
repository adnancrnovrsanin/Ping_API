package com.adnancrnovrsanin.ping.PhoneNumberUtils;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhoneNumberVerificationService {
    public void generateOTP(String phoneNumber) {
        Twilio.init("AC9d1e7888fa372ad6b7c21126021cd24b", "8d7661bf4c4cbccf1eace9b145999994");

        Verification verification = Verification.creator(
                "VA84a1f758b2487251bc29fc0bf8059ab7",
                phoneNumber,
                "sms"
        ).create();
        System.out.println(verification.getStatus());
        log.info("OTP has been successfully generated, and awaits your verification {}", LocalDateTime.now());
    }

    public boolean verifyOTP(String phoneNumber, String OTP) throws Exception {
        Twilio.init("AC9d1e7888fa372ad6b7c21126021cd24b", "8d7661bf4c4cbccf1eace9b145999994");

        try {
            VerificationCheck verificationCheck = VerificationCheck.creator(
                            "VA84a1f758b2487251bc29fc0bf8059ab7")
                    .setTo(phoneNumber)
                    .setCode(OTP)
                    .create();
            System.out.println(verificationCheck.getStatus());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
