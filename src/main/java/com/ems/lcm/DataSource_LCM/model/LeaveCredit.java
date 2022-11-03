package com.ems.lcm.DataSource_LCM.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "leave_credit" ,schema = "test")

public class LeaveCredit {
    // @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    // private Long id;

    @Id
    private String empId;

    private Double totalVlEarned;
    
    private Double totalSlEarned;

}
