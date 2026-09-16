package com.main.aqaradmin.service;

import com.main.aqaradmin.dto.AdminCreationDto;
import com.main.aqaradmin.dto.RegisterAdminRequestDto;
import com.main.aqaradmin.dto.ReturnObject;
import com.main.aqaradmin.model.Admin;
import com.main.aqaradmin.repository.AdminRepository;
import com.main.aqaradmin.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final JwtUtil jwtUtil;

    @Value("${jwt.expiration.ms}")
    private Long cookieExpirationMs;
    private static final int MAX_ATTEMPTS = 5;

    protected void addCookie(String token,
                             HttpServletResponse response,
                             long cookieExpiry) {

        ResponseCookie cookie = ResponseCookie.from("Authorization", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(Duration.ofMillis(cookieExpiry))
                .build();
    }


    public ResponseEntity<ReturnObject> register(RegisterAdminRequestDto requestDto,
                                                 HttpServletResponse httpResponse) {

        // Check if admin already exist by email
        if (adminRepository.existsByEmail(requestDto.getEmail())) {
            log.error("Admin with this email: {} already exist", requestDto.getEmail());
            return new ResponseEntity<>(ReturnObject.builder()
                    .message("There's a admin registered with this mail: "+ requestDto.getEmail())
                    .status(false)
                    .data(null)
                    .build(),
                    HttpStatus.BAD_REQUEST);

        }

        Admin admin = new Admin();
        admin.setEmail(requestDto.getEmail());
        admin.setName(requestDto.getName());
        admin.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        adminRepository.save(admin);
        otpService.generateAndSendOtp(admin);
        AdminCreationDto responseDto = AdminCreationDto.builder()
                .id(admin.getId())
                .name(admin.getName())
                .email(admin.getEmail())
                .build();

        final String jwt = jwtUtil.generateToken(requestDto.getEmail(), admin.getId());
        addCookie(jwt, httpResponse, cookieExpirationMs);
        log.info(httpResponse.getHeaders("Set-Cookie").toString());
        responseDto.setCookieExpiry(LocalDateTime.now().plusSeconds(cookieExpirationMs / 1000));
        responseDto.setToken(jwt);

        return new ResponseEntity<>(ReturnObject.builder()
                .message("Admin registered successfully and otp was sent to his email: " + requestDto.getEmail())
                .status(true)
                .data(responseDto)
                .build(),
                HttpStatus.OK);

    }

    @Transactional
    public ResponseEntity<?> verifyOtp(Admin admin, String inputOtp) {

        // Check if customer is blocked
        if (admin.getIsBlocked()) {
            if (LocalDateTime.now().isAfter(admin.getBlockedAt().plusMinutes(5))) {
                admin.setIsBlocked(false);
                admin.setIsBlocked(null);
                adminRepository.save(admin);
                log.info("Admin reached the block minutes now unblock the customer");
                return null;
            }

            return new ResponseEntity<>(new ReturnObject("Admin is blocked, please try again later", false, null),
                    HttpStatus.BAD_REQUEST);
        }

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

}
