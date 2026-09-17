package com.main.aqaradmin.controller;

import com.main.aqaradmin.dto.ChangePasswordRequestDto;
import com.main.aqaradmin.dto.ReturnObject;
import com.main.aqaradmin.dto.VerifyOtpRequestDto;
import com.main.aqaradmin.model.Admin;
import com.main.aqaradmin.repository.AdminRepository;
import com.main.aqaradmin.service.AdminService;
import com.main.aqaradmin.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final JwtUtil jwtUtil;
    private final AdminRepository adminRepository;

    @GetMapping("")
    public ResponseEntity<ReturnObject> getProfileInfo(@CookieValue("Authorization") String token) {

        if (token == null) {
            return new ResponseEntity<>(ReturnObject.builder()
                    .message("Invalid session, please login again.")
                    .status(false)
                    .data(null)
                    .build(),
                    HttpStatus.BAD_REQUEST);
        }

        String email = jwtUtil.extractEmail(token);
        Admin admin = adminRepository.findByEmail(email);

        return adminService.getProfileInfo(admin);
    }

    @PostMapping("/change-password/send-otp")
    public ResponseEntity<ReturnObject> sendOtp(@CookieValue("Authorization") String token) {

        if (token == null) {
            return new ResponseEntity<>(ReturnObject.builder()
                    .message("Invalid session, please login again.")
                    .status(false)
                    .data(null)
                    .build(),
                    HttpStatus.BAD_REQUEST);
        }

        String email = jwtUtil.extractEmail(token);
        Admin admin = adminRepository.findByEmail(email);

        return adminService.sendOtp(admin);
    }

    @PostMapping("/change-password/verify-otp")
    public ResponseEntity<ReturnObject> verifyOtp(
            @CookieValue("Authorization") String token,
            @RequestBody VerifyOtpRequestDto requestDto) {

        if (token == null) {
            return new ResponseEntity<>(ReturnObject.builder()
                    .message("Invalid session, please login again.")
                    .status(false)
                    .data(null)
                    .build(),
                    HttpStatus.BAD_REQUEST);
        }

        if (requestDto == null) {
            return new ResponseEntity<>(ReturnObject.builder()
                    .message("Please fill the otp field")
                    .status(false)
                    .data(null)
                    .build(),
                    HttpStatus.BAD_REQUEST);
        }

        String email = jwtUtil.extractEmail(token);
        Admin admin = adminRepository.findByEmail(email);
        return adminService.verifyOtp(admin, requestDto.getOtp());
    }


    @PostMapping("/change_password")
    public ResponseEntity<ReturnObject> changePassword(
            @CookieValue("Authorization") String token,
            @RequestBody ChangePasswordRequestDto requestDto
    ) {

        if (token == null) {
            return new ResponseEntity<>(ReturnObject.builder()
                    .message("Invalid session, please login again.")
                    .status(false)
                    .data(null)
                    .build(),
                    HttpStatus.BAD_REQUEST);
        }

        String email = jwtUtil.extractEmail(token);
        Admin admin = adminRepository.findByEmail(email);

        return adminService.changePassword(admin, requestDto.getPassword());
    }
}
