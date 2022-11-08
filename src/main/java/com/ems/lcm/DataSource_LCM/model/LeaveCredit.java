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
    @Column(name = "emp_id")
    private String empId;

    @Column(name = "total_vl_earned",nullable = false)
    private Double totalVlEarned;
    
    @Column(name = "total_sl_earned",nullable = false)
    private Double totalSlEarned;

}
