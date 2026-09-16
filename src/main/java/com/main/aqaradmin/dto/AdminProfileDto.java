package com.main.aqaradmin.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminProfileDto {

    private Integer id;
    private String name;
    private String email;
}
