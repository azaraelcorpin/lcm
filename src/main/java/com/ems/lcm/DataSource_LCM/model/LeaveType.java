package com.ems.lcm.DataSource_LCM.model;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "leave_type" ,schema = "test")

public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "number_of_days")
    private int numberOfDays;
        
}
