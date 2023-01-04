package com.ems.lcm.DataSource_LCM.model;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "leave_credit_transaction" ,schema = "test")

public class LeaveCreditTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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


    @Column(name = "transaction_referrence_id")
    private String transactionReferrenceId;

    @Column(name = "posted_by")
    private String postedBy;

    @Column(name = "remarks")
    private String remarks;

    @Transient //@transient - annotates that this attribute must not reflect to the database
    public static final String TYPE_INITIAL_VALUE = "INITIAL_VALUE";

    @Transient //@transient - annotates that this attribute must not reflect to the database
    public static final String TYPE_ADJUSTMENT_VALUE = "TYPE_ADJUSTMENT_VALUE";

    @Transient //@transient - annotates that this attribute must not reflect to the database
    public static final String TYPE_UPDATE_ENTRY = "TYPE_UPDATE_ENTRY";

    @Transient //@transient - annotates that this attribute must not reflect to the database
    public static final String TYPE_INCREMENT = "INCREMENT";

    @Transient //@transient - annotates that this attribute must not reflect to the database
    public static final String TYPE_DEDUCTION = "DEDUCTION";
}
