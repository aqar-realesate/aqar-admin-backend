package com.main.aqaradmin.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class LoginResponseDto implements Serializable {
    private Integer id;

    private String name;
    private String email;

    private LocalDateTime cookieExpiry;
    private Boolean isVerified;

    private String token;
}
