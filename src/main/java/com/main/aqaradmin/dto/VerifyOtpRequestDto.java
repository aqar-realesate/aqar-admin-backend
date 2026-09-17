package com.main.aqaradmin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyOtpRequestDto {

    private String otp;
}
