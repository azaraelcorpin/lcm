package com.ems.lcm.DataSource_LCM.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.lcm.DataSource_LCM.model.LeaveCreditTransaction;


public interface LeaveCreditTransactionRepository extends JpaRepository<LeaveCreditTransaction,Long>{
    
    Optional<LeaveCreditTransaction> findById(Long id);

    LeaveCreditTransaction findByEmpId(String empId);

    List<LeaveCreditTransaction> findAll();
    
}
