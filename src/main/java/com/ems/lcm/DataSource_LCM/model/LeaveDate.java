package com.ems.lcm.DataSource_LCM.model;

import javax.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "leave_date" ,schema = "test")

public class LeaveDate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "leave_application_id")
    private Long leaveApplicationId;

    @Column(name = "date",nullable = false)
    private Date date;

    //use for instances that the applicant have not expend this spicific date..
    //posible value   [active,used,repealed]
    // @Column(name = "status",nullable = false)
    // private String status = "active";

    //offset to a specified accumulated leave credits
    @Column(name = "offset_to",nullable = true)
    private String offsetTo;
    
}
