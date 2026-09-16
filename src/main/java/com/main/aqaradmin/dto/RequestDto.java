package com.main.aqaradmin.dto;

import com.main.aqaradmin.model.enums.RequestStatus;
import com.main.aqaradmin.model.enums.UnitStatus;
import com.main.aqaradmin.model.enums.UnitType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestDto {

    private Integer requestId;

    private Integer unitId;
    private String unitTitle;
    private String unitDescription;
    private UnitType unitType;
    private Integer unitBedrooms;
    private Integer unitBathrooms;
    private Integer unitFloor;
    private double unitPrice;
    private UnitStatus unitStatus;
    private Boolean isActive;
    private Integer unitOwnerId;

    private Integer customerId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String customerCity;
    private String customerState;
    private String customerAddress;
    private Long customerNationalityId;


    private String fileName;
    private String filePath;

    private RequestStatus requestStatus;
}
