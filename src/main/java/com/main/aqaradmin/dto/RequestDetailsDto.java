package com.main.aqaradmin.dto;

import com.main.aqaradmin.model.enums.RequestStatus;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestDetailsDto {

    private Integer requestId;
    private Integer unitId;
    private String fileName;
    private String filePath;
    private RequestStatus requestStatus;
    private Integer adminId;
    private Integer customerId;

    private String requestActionComment;
}
