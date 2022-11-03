package com.ems.lcm.DataSource_LCM.model;

import javax.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "test_Model" ,schema = "test")

public class testModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description2")
    private String description2String;
}
