package com.main.aqaradmin.dto;

import com.main.aqaradmin.model.enums.UnitStatus;
import com.main.aqaradmin.model.enums.UnitType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerInfoDto {

    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String customerCity;
    private String customerState;
    private String customerAddress;
    private Long customerNationalityId;

    private String unitTitle;
    private String unitDescription;
    private UnitType unitType;
    private Integer unitBedrooms;
    private Integer unitBathrooms;
    private Integer unitFloor;
    private double unitPrice;
    private UnitStatus unitStatus;
}
