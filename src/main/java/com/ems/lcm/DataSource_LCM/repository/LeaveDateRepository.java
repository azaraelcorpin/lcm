package com.ems.lcm.DataSource_LCM.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.lcm.DataSource_LCM.model.LeaveDate;


public interface LeaveDateRepository extends JpaRepository<LeaveDate,Long>{
    
    Optional<LeaveDate> findById(Long id);

    // LeaveDate findByEmpId(String empId);

    List<LeaveDate> findAll();

    void deleteAllByLeaveApplicationId(Long leaveApplicationId);
    
}
