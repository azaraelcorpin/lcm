package com.ems.lcm.DataSource_LCM.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.lcm.DataSource_LCM.model.LeaveCredit;


public interface LeaveCreditRepository extends JpaRepository<LeaveCredit,Long>{
    
    Optional<LeaveCredit> findById(Long id);

    LeaveCredit findByEmpId(String empId);

    List<LeaveCredit> findAll();
    
}
