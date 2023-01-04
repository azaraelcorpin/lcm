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

import com.ems.lcm.DataSource_LCM.model.LeaveCredit;


public interface LeaveCreditRepository extends JpaRepository<LeaveCredit,Long>{
    


    Optional<LeaveCredit> findByEmpId(String empId);

    List<LeaveCredit> findAll();

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    @Query(value = "SELECT * FROM leave_credit WHERE "+
    " (:empId IS NULL OR emp_id LIKE :empId% )"+
    " AND (:name IS NULL OR ((firstname LIKE %:name% ) OR (lastname LIKE %:name% ) OR (firstname LIKE %:name% ))) "+
    " AND (:position IS NULL OR position LIKE %:position% ) "+
    " AND (:department IS NULL OR department LIKE %:department% )"
    ,nativeQuery = true)
    Page <LeaveCredit> searchLeaveCredit(
        @Param("name")String name,
        @Param("position") String position,
        @Param("department") String department,
        @Param("empId") String empId,
        Pageable pageable 
        );

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    @Query(value = "SELECT * FROM leave_credit WHERE "+
    " (emp_id LIKE %:empId% )"+
    " OR (((firstname LIKE %:name% ) OR (lastname LIKE %:name% ) OR (firstname LIKE %:name% ))) "+
    " OR (position LIKE %:position% ) "+
    " OR (department LIKE %:department% )"+
    " OR (remarks LIKE %:department% )"
    ,nativeQuery = true)
    Page <LeaveCredit> searchValue(
        @Param("name")String name,
        @Param("position") String position,
        @Param("department") String department,
        @Param("empId") String empId,
        Pageable pageable 
        );
    
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    @Query(value = "SELECT DISTINCT(department) FROM leave_credit "
    ,nativeQuery = true)
    List <String> getDepartment();
}
