package com.main.aqaradmin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterAdminRequestDto {

    private String name;

    private String email;

    private String password;
}
