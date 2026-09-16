package com.main.aqaradmin.service;

import com.main.aqaradmin.dto.RequestDetailsDto;
import com.main.aqaradmin.dto.RequestDto;
import com.main.aqaradmin.dto.ReturnObject;
import com.main.aqaradmin.model.Admin;
import com.main.aqaradmin.model.Customer;
import com.main.aqaradmin.model.Request;
import com.main.aqaradmin.model.Unit;
import com.main.aqaradmin.model.enums.RequestStatus;
import com.main.aqaradmin.repository.CustomerRepository;
import com.main.aqaradmin.repository.RequestRepository;
import com.main.aqaradmin.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestService {


    private final RequestRepository requestRepository;
    private final UnitRepository unitRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public ResponseEntity<ReturnObject> getAllRequests() {

        try {

            List<Request> requests = requestRepository.findAll();
            List<RequestDto> response = new ArrayList<>();

            for (Request request : requests) {
                Unit unit = unitRepository.findById(request.getUnitId()).get();
                Customer customer = customerRepository.findById(request.getCustomerId()).get();
                RequestDto dto = RequestDto.builder()
                        .requestId(request.getId())
                        .unitId(request.getUnitId())
                        .unitTitle(unit.getTitle())
                        .unitDescription(unit.getDescription())
                        .unitType(unit.getUnitType())
                        .unitBedrooms(unit.getBedrooms())
                        .unitBathrooms(unit.getBathrooms())
                        .unitFloor(unit.getFloor())
                        .unitPrice(unit.getPrice())
                        .unitStatus(unit.getUnitStatus())
                        .isActive(unit.getIsActive())
                        .unitOwnerId(unit.getOwner())
                        .customerId(request.getCustomerId())
                        .customerName(customer.getName())
                        .customerPhone(customer.getPhone())
                        .customerEmail(customer.getEmail())
                        .customerCity(customer.getCity())
                        .customerState(customer.getState())
                        .customerAddress(customer.getAddress())
                        .customerNationalityId(customer.getNationalityId())
                        .build();

                response.add(dto);
            }

            return new ResponseEntity<>(new ReturnObject(
                    "All requests fetched successfully",
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

    @Transactional
    public ResponseEntity<ReturnObject> approveRequest(Admin admin, Integer requestId) {

        try {

            Optional<Request> optRequest = requestRepository.findById(requestId);
            if (optRequest.isEmpty()) {
                return new ResponseEntity<>(new ReturnObject(
                        "Request not found",
                        false,
                        null
                ),
                        HttpStatus.NOT_FOUND);
            }
            Request request = optRequest.get();

            request.setRequestStatus(RequestStatus.APPROVED);
            request.setAdminId(admin.getId());
            request.setI
            requestRepository.save(request);

            RequestDetailsDto response = RequestDetailsDto.builder()
                    .requestId(request.getId())
                    .unitId(request.getUnitId())
                    .fileName(request.getFileName())
                    .filePath(request.getFilePath())
                    .requestStatus(request.getRequestStatus())
                    .adminId(request.getAdminId())
                    .customerId(request.getCustomerId())
                    .build();

            return new ResponseEntity<>(new ReturnObject(
                    "Request approved successfully",
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

    @Transactional
    public ResponseEntity<ReturnObject> rejectRequest(Admin admin, Integer requestId) {

        try {

            Optional<Request> optRequest = requestRepository.findById(requestId);
            if (optRequest.isEmpty()) {
                return new ResponseEntity<>(new ReturnObject(
                        "Request not found",
                        false,
                        null
                ),
                        HttpStatus.NOT_FOUND);
            }
            Request request = optRequest.get();

            request.setRequestStatus(RequestStatus.REJECTED);
            request.setAdminId(admin.getId());
            requestRepository.save(request);

            RequestDetailsDto response = RequestDetailsDto.builder()
                    .requestId(request.getId())
                    .unitId(request.getUnitId())
                    .fileName(request.getFileName())
                    .filePath(request.getFilePath())
                    .requestStatus(request.getRequestStatus())
                    .adminId(request.getAdminId())
                    .customerId(request.getCustomerId())
                    .build();

            return new ResponseEntity<>(new ReturnObject(
                    "Request rejected successfully",
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

    @Transactional
    public ResponseEntity<ReturnObject> needActionRequest(Admin admin, Integer requestId, String comment) {

        try {

            Optional<Request> optRequest = requestRepository.findById(requestId);
            if (optRequest.isEmpty()) {
                return new ResponseEntity<>(new ReturnObject(
                        "Request not found",
                        false,
                        null
                ),
                        HttpStatus.NOT_FOUND);
            }
            Request request = optRequest.get();

            request.setRequestStatus(RequestStatus.NEED_ACTION);
            request.setActionComment(comment);
            request.setAdminId(admin.getId());
            requestRepository.save(request);

            RequestDetailsDto response = RequestDetailsDto.builder()
                    .requestId(request.getId())
                    .unitId(request.getUnitId())
                    .fileName(request.getFileName())
                    .filePath(request.getFilePath())
                    .requestStatus(request.getRequestStatus())
                    .adminId(request.getAdminId())
                    .customerId(request.getCustomerId())
                    .requestActionComment(comment)
                    .build();

            return new ResponseEntity<>(new ReturnObject(
                    "Request set need action successfully",
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
