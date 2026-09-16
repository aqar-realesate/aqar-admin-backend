package com.main.aqaradmin.model;

import com.main.aqaradmin.model.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "unit_id")
    private Integer unitId;

    private String fileName;          // original file name
    private String filePath;          // path where the file is saved


    @Column(name = "request_status")
    private RequestStatus requestStatus;

    @Column(name = "admin_id")
    private Integer adminId;

    @Column(nullable = false, name = "customer_id")
    private Integer customerId;

    @Column(name = "action_comment")
    private String actionComment;


}
