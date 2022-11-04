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

    @Column(name = "empId")
    private String empId;

    private String lastname;

    private String firstname;

    private String middlename;

    private String position;

    private BigDecimal salary = BigDecimal.ZERO;
    
    private Date applicationDate;

    private String transactionReferrenceId;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", referencedColumnName = "id")
    private LeaveType leaveType;

    @Column(name = "others_leave_type", nullable = true)
    private String othersLeaveType;

    private String leaveDetails;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_application_id")
    private List<LeaveDate> leaveDates;

    //at physical leave form. Section 6.D
    //posible value  [requested, not requested]
    @Column(name = "commutation", nullable = true)
    private String commutation;

    private Double totalVlEarned;
    
    private Double totalSlEarned;

    private Double appliedVlAmount;

    private Double appliedSlAmount;

    private String certifiedBy;

    private Date certifiedOn;
    
    //at physical leave form. Section 7.B
    //posible value  case 1-> "For Approval" case 2-> "For disapproval due to (inputted by user{authorizedOfficer})"
    private String recommendation;
    
    private String recommendationBy;

    private Date recommendationDate;

    //at physical leave form. Section 7.D
    //posible value  case 1-> {daysWithPay:x,daysWithoutPay:y,others:specified} case 2-> {disapproved due to 'user input'}
    private String approvalRemarks;

    private String approvalBy;

    private Date approvalDate;

    private String status;
}
