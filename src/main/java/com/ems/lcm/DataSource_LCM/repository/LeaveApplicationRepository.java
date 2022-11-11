package com.ems.lcm.DataSource_LCM.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    @Query(value = "SELECT * FROM leave_application WHERE "+
    " (:empId IS NULL OR emp_id LIKE :empId% )"
    +" AND (:dateFrom IS NULL OR application_date >= :dateFrom )"
    +" AND (:dateTo IS NULL OR application_date <= :dateTo )"
    +" AND (:transactionReferrenceId IS NULL OR transaction_referrence_id LIKE %:transactionReferrenceId% )"
    +" AND (:department IS NULL OR department LIKE %:department% )"
    +" AND (:position IS NULL OR position LIKE %:position% )"
    +" AND (:employeeName IS NULL OR (lastname LIKE %:employeeName% OR firstname LIKE %:employeeName% OR middlename LIKE %:employeeName%) )"
    +" AND (:leaveTypeId IS NULL OR leave_type_id = :leaveTypeId )"
    +" AND (:leaveDetails IS NULL OR leave_details LIKE %:leaveDetails% )"
    +" AND (:leaveDate IS NULL OR leaveDates LIKE %:leaveDate% )"
    +" AND (:commutation IS NULL OR commutation LIKE %:commutation% )"
    +" AND (:status IS NULL OR status LIKE %:status% )"
    ,nativeQuery = true)
    Page <LeaveApplication> searchLeaveApplications(
        @Param("empId")String empId,
        @Param("dateFrom")Date dateFrom,
        @Param("dateTo")Date dateTo,
        @Param("transactionReferrenceId")String transactionReferrenceId,
        @Param("department")String department,
        @Param("position")String position,
        @Param("employeeName")String employeeName,
        @Param("leaveTypeId")Long leaveTypeId,
        @Param("leaveDetails")String leaveDetails,
        @Param("leaveDate")String leaveDate,
        @Param("commutation")String commutation,
        @Param("status")String status,
        Pageable pageable 
        );
}
