package com.main.aqaradmin.service;

import com.main.aqaradmin.model.Admin;
import com.main.aqaradmin.repository.AdminRepository;
import com.main.aqaradmin.util.OtpGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final AdminRepository adminRepository;
    private final OtpGenerator otpGenerator;

    @Value("${otp.expiry.minutes:5}")
    private int otpExpiryMinutes;

    // Generate and send a fresh otp for the given email
    @Transactional
    public void generateAndSendOtp(Admin admin) {
        String otp = otpGenerator.generate();
        admin.setOtp(otp);
        admin.setOtpExpiresAt(LocalDateTime.now().plusMinutes(otpExpiryMinutes));
        admin.setOtpLastSentAt(LocalDateTime.now());
        admin.setOtpCount(admin.getOtpCount() + 1);
        admin.setFailedAttempts(0);
        adminRepository.save(admin);
        log.info("Otp generated and saved in database");
    }

}
