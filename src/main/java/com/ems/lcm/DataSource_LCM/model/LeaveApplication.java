package com.ems.lcm.DataSource_LCM.model;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "leave_application" ,schema = "test")

public class LeaveApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emp_id")
    private String empId;
    
    @Column(name = "application_date", nullable = false)
    private Date applicationDate;

    @Column(name = "transaction_referrence_id", nullable = false,unique = true)
    private String transactionReferrenceId;

    @Column(name = "leave_type_id")
    private Long leaveTypeId;

    @Column(name = "others_leave_type", nullable = true)
    private String othersLeaveType;

    @Column(name = "leave_details")
    private String leaveDetails;

    @Column(name = "leave_dates")
    private String leaveDates = "";

    //at physical leave form. Section 6.D
    //posible value  [requested, not requested]
    @Column(name = "commutation")
    private String commutation = "";

    @Column(name = "total_vl_earned",nullable = true)
    private Double totalVlEarned;
    
    @Column(name = "total_sl_earned",nullable = true)
    private Double totalSlEarned;

    @Column(name = "applied_vl_amount",nullable = true)
    private Double appliedVlAmount;

    @Column(name = "applied_sl_amount",nullable = true)
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

    //posible value [APPLIED,CERTIFIED|INVALID, RECOMMENDED,APPROVED|REJECTED]
    private String status = "APPLIED";

    @Column(name = "remarks",nullable = true)
    private String remarks;

    @Column(name = "number_of_days",nullable = true)
    private String numberOfDays;

    @Transient //@transient - annotates that this attribute must not reflect to the database
    public static final String STATUS_APPLIED = "APPLIED";

    @Transient //@transient - annotates that this attribute must not reflect to the database
    public static final String STATUS_INVALID = "INVALID";

    @Transient //@transient - annotates that this attribute must not reflect to the database
    public static final String STATUS_CERTIFIED = "CERTIFIED";

    @Transient //@transient - annotates that this attribute must not reflect to the database
    public static final String STATUS_RECOMMENDED = "RECOMMENDED";

    @Transient //@transient - annotates that this attribute must not reflect to the database
    public static final String STATUS_APPROVED = "APPROVED";
}
