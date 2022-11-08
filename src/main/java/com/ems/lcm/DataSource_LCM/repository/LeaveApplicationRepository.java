package com.ems.lcm.DataSource_LCM.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.ems.lcm.DataSource_LCM.model.LeaveApplication;

public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication,Long>{
    
    Optional<LeaveApplication> findById(Long id);

    LeaveApplication findByTransactionReferrenceId(String transactionReferrenceId);

    List<LeaveApplication> findAll();
    
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    @Query(value = "SELECT id FROM leave_application WHERE leave_type_id = :leave_type_id LIMIT 1",nativeQuery = true)
    List <Long> hasLeaveType(@Param("leave_type_id")Long leave_type_id);
}
