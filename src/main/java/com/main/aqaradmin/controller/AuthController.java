package com.main.aqaradmin.controller;

import com.main.aqaradmin.dto.LoginRequestDto;
import com.main.aqaradmin.dto.LoginResponseDto;
import com.main.aqaradmin.dto.RegisterAdminRequestDto;
import com.main.aqaradmin.dto.ReturnObject;
import com.main.aqaradmin.model.Admin;
import com.main.aqaradmin.repository.AdminRepository;
import com.main.aqaradmin.service.AuthService;
import com.main.aqaradmin.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final AdminRepository adminRepository;
    private final JwtUtil jwtUtil;


    @PostMapping("/register")
    public ResponseEntity<ReturnObject> register(
            @RequestBody RegisterAdminRequestDto requestDto,
            HttpServletResponse httpResponse) {

        try {


            if (requestDto == null) {
                return new ResponseEntity<>(new ReturnObject(
                        "the request is empty",
                        false,
                        null
                ), HttpStatus.BAD_REQUEST);
            }

            return authService.register(requestDto, httpResponse);
        } catch (EnumConstantNotPresentException e) {
            return new ResponseEntity<>(new ReturnObject(
                    e.getMessage(),
                    false,
                    null
            ),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ReturnObject> verifyOtp(
            @CookieValue("Authorization") String token,
            @RequestBody String otp) {

        if (token == null) {
            return new ResponseEntity<>(ReturnObject.builder()
                    .message("Invalid session, please login again.")
                    .status(false)
                    .data(null)
                    .build(),
                    HttpStatus.BAD_REQUEST);
        }

        if (otp == null) {
            return new ResponseEntity<>(ReturnObject.builder()
                    .message("Please fill the otp field")
                    .status(false)
                    .data(null)
                    .build(),
                    HttpStatus.BAD_REQUEST);
        }

        String email = jwtUtil.extractEmail(token);
        Admin admin = adminRepository.findByEmail(email);
        return authService.verifyOtp(admin, otp);
    }


    @PostMapping("/login")
    public ResponseEntity<ReturnObject> login(
            @RequestBody LoginRequestDto loginRequestBody,
            HttpServletResponse httpResponse) {
        if (loginRequestBody == null) {
            log.error("the login request is empty");
            return new ResponseEntity<>(ReturnObject.builder()
                    .message("Please fill required fields")
                    .status(false)
                    .data(null)
                    .build(),
                    HttpStatus.BAD_REQUEST);
        }

        return authService.login(loginRequestBody, httpResponse);
    }

}
