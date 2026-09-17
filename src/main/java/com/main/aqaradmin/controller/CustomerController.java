package com.main.aqaradmin.controller;

import com.main.aqaradmin.dto.GetCustomerInfoRequestDto;
import com.main.aqaradmin.dto.ReturnObject;
import com.main.aqaradmin.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping()
    public ResponseEntity<ReturnObject> getCustomerInfo(@RequestBody GetCustomerInfoRequestDto requestDto) {

        return customerService.getCustomerInfo(requestDto.getCustomerId());
    }

}
