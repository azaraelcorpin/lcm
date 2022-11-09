package com.ems.lcm.DataSource_LCM.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.ems.lcm.DataSource_LCM.model.LeaveType;


public interface LeaveTypeRepository extends JpaRepository<LeaveType,Long>{
    
    Optional<LeaveType> findById(Long id);

    List<LeaveType> findByCodeContains(String code);

    List<LeaveType> findByDescriptionContains(String desc);

    List<LeaveType> findAll();

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    @Query(value = "SELECT * FROM leave_type WHERE "+
    " (:code IS NULL OR code LIKE :code% )"+
    " AND (:description IS NULL OR description LIKE %:description% )",nativeQuery = true)
    Page <LeaveType> searchLeaveType(
        @Param("code")String code,
        @Param("description") String description,
        Pageable pageable 
        );
    
}
