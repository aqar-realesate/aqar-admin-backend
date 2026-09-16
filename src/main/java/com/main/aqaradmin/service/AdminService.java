package com.main.aqaradmin.service;

import com.main.aqaradmin.dto.AdminProfileDto;
import com.main.aqaradmin.dto.ReturnObject;
import com.main.aqaradmin.model.Admin;
import com.main.aqaradmin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;

    private static final int MAX_ATTEMPTS = 5;


    @Transactional
    public ResponseEntity<ReturnObject> sendOtp(Admin admin) {

        try {
            otpService.generateAndSendOtp(admin);
            return new ResponseEntity<>(ReturnObject.builder()
                    .message("Otp sent successfully")
                    .status(true)
                    .data(null)
                    .build(),
                    HttpStatus.OK);
        } catch (Exception e) {
            log.error("There's an error happen when trying to send new otp: {}", e.getMessage());
            return new ResponseEntity<>(ReturnObject.builder()
                    .message("There's an error happen when trying to send new otp, please try again")
                    .status(false)
                    .data(null)
                    .build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<ReturnObject> verifyOtp(Admin admin, String inputOtp) {


        // Check otp expiry
        if (LocalDateTime.now().getMinute() > admin.getOtpExpiresAt().getMinute()) {
            log.info("The otp has been expired");
            admin.setOtp(null);
            admin.setOtpExpiresAt(null);
            adminRepository.save(admin);
            return new ResponseEntity<>(new ReturnObject("Otp is expired, send a new otp", false, null),
                    HttpStatus.BAD_REQUEST);
        }

        // Check otp failed attempts
        if(admin.getFailedAttempts() > MAX_ATTEMPTS) {
            log.info("The admin reached the max otp failed attempts");
            admin.setIsBlocked(true);
            admin.setBlockedAt(LocalDateTime.now());
            adminRepository.save(admin);
            return new ResponseEntity<>(new ReturnObject("You've achieved the maximum otp failed attempts", false, null),
                    HttpStatus.BAD_REQUEST);
        }

        // Check equity of otp send and saved one
        if (!admin.getOtp().equals(inputOtp)) {
            log.info("The otp input doesn't match the saved one");
            admin.setFailedAttempts(admin.getFailedAttempts() + 1);
            adminRepository.save(admin);
            return new ResponseEntity<>(new ReturnObject("Wrong otp input", false, null),
                    HttpStatus.BAD_REQUEST);
        }

        admin.setIsVerified(true);
        admin.setIsBlocked(false);
        admin.setBlockedAt(null);
        admin.setFailedAttempts(0);
        admin.setOtpCount(0);
        admin.setOtp(null);
        admin.setOtpExpiresAt(null);
        admin.setOtpLastSentAt(null);
        adminRepository.save(admin);
        log.info("Otp verified successfully for: {}", admin.getEmail());
        return new ResponseEntity<>(new ReturnObject("Otp verified successfully", true, null),
                HttpStatus.OK);
    }

    public ResponseEntity<ReturnObject> changePassword(Admin admin, String password) {

        admin.setPassword(passwordEncoder.encode(password));
        adminRepository.save(admin);

        return new ResponseEntity<>(new ReturnObject(
                "Password changed successfully",
                true,
                null
        ),
                HttpStatus.OK);
    }

    public ResponseEntity<ReturnObject> getProfileInfo(Admin admin) {

        AdminProfileDto response = AdminProfileDto.builder()
                .id(admin.getId())
                .name(admin.getName())
                .email(admin.getEmail())
                .build();

        return new ResponseEntity<>(new ReturnObject(
                "Admin profile data fetched successfully",
                true,
                response
        ),
                HttpStatus.OK);
    }
}
