package com.ems.lcm.DataSource_LCM.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "leave_credit_transaction" ,schema = "test")

public class LeaveCreditTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "emp_id")
    private String empId;

    //posible value [increment, deduction]
    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "prev_vl_balance")
    private Double prevVlBalance;

    @Column(name = "prev_sl_balance")
    private Double prevSlBalance;    

    @Column(name = "vl_amount")
    private Double vlAmount;

    @Column(name = "sl_amount")
    private Double slAmount;

    @Column(name = "date_posted")
    private Date datePosted;

    @OneToOne(fetch=FetchType.LAZY,targetEntity = LeaveApplication.class)
    @JoinColumn(name = "transaction_referrence_id", referencedColumnName = "transaction_referrence_id",nullable = true)
    private String transaction_referrence_id;

    @Column(name = "posted_by")
    private String postedBy;
}
