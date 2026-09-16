package com.main.aqaradmin.controller;

import com.main.aqaradmin.dto.ReturnObject;
import com.main.aqaradmin.model.Admin;
import com.main.aqaradmin.repository.AdminRepository;
import com.main.aqaradmin.service.RequestService;
import com.main.aqaradmin.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
@Slf4j
public class RequestController {

    private final RequestService requestService;
    private final JwtUtil jwtUtil;
    private final AdminRepository adminRepository;


    @GetMapping("/all")
    public ResponseEntity<ReturnObject> getAllRequests() {

        return requestService.getAllRequests();
    }

    @PostMapping("/approve/{requestId}")
    public ResponseEntity<ReturnObject> approveRequest(@CookieValue("Authorization") String token,
                                                       @PathVariable Integer requestId) {

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
        return requestService.approveRequest(admin, requestId);
    }

    @PostMapping("/reject/{requestId}")
    public ResponseEntity<ReturnObject> rejectRequest(@CookieValue("Authorization") String token,
                                                      @PathVariable Integer requestId) {

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
        return requestService.rejectRequest(admin, requestId);
    }

    @PostMapping("/need_action/{requestId}")
    public ResponseEntity<ReturnObject> needActionRequest(@CookieValue("Authorization") String token,
                                                          @PathVariable Integer requestId,
                                                          @RequestBody String comment) {

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
        return requestService.needActionRequest(admin, requestId, comment);
    }

}