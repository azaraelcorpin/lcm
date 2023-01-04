package com.ems.lcm.DataSource_LCM.model;

import java.math.BigDecimal;
import java.util.Date;

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

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "middlename", nullable = true)
    private String middlename;

    @Column(name = "position", nullable = true)
    private String position;

    @Column(name = "salary", nullable = true)
    private BigDecimal salary = BigDecimal.ZERO;

    @Column(name = "total_vl_earned",nullable = false)
    private Double totalVlEarned;
    
    @Column(name = "total_sl_earned",nullable = false)
    private Double totalSlEarned;

    @Column(name = "last_updated",nullable = false)
    private Date lastUpdated;

    @Column(name = "remarks",nullable = true)
    private String remarks;

    /*
     * Active or inactive
     */
    @Column(name = "status",nullable = true)
    private String status;
}
