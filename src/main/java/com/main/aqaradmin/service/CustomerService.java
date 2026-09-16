package com.main.aqaradmin.service;

import com.main.aqaradmin.dto.CustomerInfoDto;
import com.main.aqaradmin.dto.ReturnObject;
import com.main.aqaradmin.model.Customer;
import com.main.aqaradmin.model.Unit;
import com.main.aqaradmin.repository.CustomerRepository;
import com.main.aqaradmin.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {


    private final CustomerRepository customerRepository;
    private final UnitRepository unitRepository;

    @Transactional
    public ResponseEntity<ReturnObject> getCustomerInfo(Integer customerId) {

        try {

            Optional<Customer> optCustomer = customerRepository.findById(customerId);
            if (optCustomer.isEmpty()) {
                return new ResponseEntity<>(new ReturnObject(
                        "Customer not found",
                        false,
                        null
                ), HttpStatus.NOT_FOUND);
            }
            Customer customer = optCustomer.get();
            Unit unit = unitRepository.findByOwner(customerId);


            CustomerInfoDto response = CustomerInfoDto.builder()
                    .customerName(customer.getName())
                    .customerPhone(customer.getPhone())
                    .customerEmail(customer.getEmail())
                    .customerCity(customer.getCity())
                    .customerState(customer.getState())
                    .customerAddress(customer.getAddress())
                    .customerNationalityId(customer.getNationalityId())
                    .unitTitle(unit.getTitle())
                    .unitDescription(unit.getDescription())
                    .unitType(unit.getUnitType())
                    .unitBedrooms(unit.getBedrooms())
                    .unitBathrooms(unit.getBathrooms())
                    .unitFloor(unit.getFloor())
                    .unitPrice(unit.getPrice())
                    .unitStatus(unit.getUnitStatus())
                    .build();

            return new ResponseEntity<>(new ReturnObject(
                    "Customer info fetched successfully",
                    true,
                    response
            ),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ReturnObject(
                    e.getMessage(),
                    false,
                    null
            ),
                    HttpStatus.BAD_REQUEST);

        }
    }
}
