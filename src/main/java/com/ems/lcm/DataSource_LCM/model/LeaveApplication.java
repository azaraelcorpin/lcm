package com.ems.lcm.DataSource_LCM.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "leave_application" ,schema = "test")

public class LeaveApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    @Column(name = "salary", nullable = false)
    private BigDecimal salary = BigDecimal.ZERO;
    
    @Column(name = "application_date", nullable = false)
    private Date applicationDate;

    @Column(name = "transaction_referrence_id", nullable = true,unique = true)
    private String transactionReferrenceId;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", referencedColumnName = "id")
    private LeaveType leaveType;

    @Column(name = "others_leave_type", nullable = true)
    private String othersLeaveType;

    @Column(name = "leave_details")
    private String leaveDetails;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_application_id")
    private List<LeaveDate> leaveDates;

    //at physical leave form. Section 6.D
    //posible value  [requested, not requested]
    @Column(name = "commutation", nullable = true)
    private String commutation;

    @Column(name = "total_vl_earned",nullable = false)
    private Double totalVlEarned;
    
    @Column(name = "total_sl_earned",nullable = false)
    private Double totalSlEarned;

    @Column(name = "applied_vl_amount",nullable = false)
    private Double appliedVlAmount;

    @Column(name = "applied_sl_amount",nullable = false)
    private Double appliedSlAmount;

    @Column(name = "certified_By",nullable = true)
    private String certifiedBy;

    @Column(name = "certified_on",nullable = true)
    private Date certifiedOn;
    
    //at physical leave form. Section 7.B
    //posible value  case 1-> "For Approval" case 2-> "For disapproval due to (inputted by user{authorizedOfficer})"
    private String recommendation;
    
    @Column(name = "recommendation_by",nullable = true)
    private String recommendationBy;

    @Column(name = "recommendation_date",nullable = true)
    private Date recommendationDate;

    //at physical leave form. Section 7.D
    //posible value  case 1-> {daysWithPay:x,daysWithoutPay:y,others:specified} case 2-> {disapproved due to 'user input'}
    @Column(name = "approval_remarks",nullable = true)
    private String approvalRemarks;

    @Column(name = "approval_by",nullable = true)
    private String approvalBy;

    @Column(name = "approval_date",nullable = true)
    private Date approvalDate;

    //posible value [APPLIED,CERTIFIED|INVALID, RECOMMENDEDATION,APPROVED|REJECTED]
    private String status;

    private String remarks;
}
