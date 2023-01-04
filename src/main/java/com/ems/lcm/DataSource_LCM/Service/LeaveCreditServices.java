package com.ems.lcm.DataSource_LCM.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.ems.lcm.DataSource_LCM.model.LeaveCredit;
import com.ems.lcm.DataSource_LCM.model.LeaveCreditTransaction;
import com.ems.lcm.DataSource_LCM.repository.LeaveCreditRepository;
import com.ems.lcm.DataSource_LCM.repository.LeaveCreditTransactionRepository;
import lombok.extern.slf4j.Slf4j;

 @Slf4j
@Service
public class LeaveCreditServices {
    @Autowired
    private LeaveCreditTransactionRepository leaveCreditTransactionRepository;

    @Autowired
    private LeaveCreditRepository leaveCreditRepository;

    @Autowired
    private GeneralService generalService;


    @Transactional("LcmTransactionManager")
    private boolean createLeaveCreditToDB(LeaveCredit leaveCredit){
        
        return leaveCreditRepository.save(leaveCredit)!=null;
    }

    @Transactional("LcmTransactionManager")
    public ResponseEntity<Object> createLeaveCredit(@RequestBody Map<String,Object> params, String user_email){
        try{
            String empID = generalService.getString(params.get("empID"));    
            String firstname = generalService.getString(params.get("firstname"));  
            String middlename = generalService.getString(params.get("middlename"));  
            String lastname = generalService.getString(params.get("lastname"));  
            String position = generalService.getString(params.get("position"));  
            String department = generalService.getString(params.get("department"));  
            Double salary = Double.valueOf(params.get("salary").toString());    
            Double VlAmount = Double.valueOf(params.get("VlAmount").toString());
            Double SlAmount = Double.valueOf(params.get("SlAmount").toString());

            String remarks = params.get("remarks").toString(); // States in the remarks the details of new registrants records of leave_credit with document Id.. 
                                                               //(leave credit balance from physical document No. as of date....)
            String transactionReferrenceId = params.get("transactionReferrenceId").toString();
            if(empID == null | firstname == null | lastname == null | position == null |department == null )
                return new ResponseEntity<Object>(generalService.renderJsonResponse("400", "Invalid value"),HttpStatus.BAD_REQUEST);              

            //registering into leave_credit table
            LeaveCredit leaveCredit =  new LeaveCredit();
            leaveCredit.setEmpId(empID);
            leaveCredit.setFirstname(firstname);
            leaveCredit.setMiddlename(middlename);
            leaveCredit.setLastname(lastname);
            leaveCredit.setPosition(position);
            leaveCredit.setDepartment(department);
            leaveCredit.setSalary(BigDecimal.valueOf(salary));
            leaveCredit.setTotalSlEarned(SlAmount);
            leaveCredit.setTotalVlEarned(VlAmount);
            leaveCredit.setRemarks(remarks);
            leaveCredit.setLastUpdated(new Date());

            leaveCreditRepository.save(leaveCredit);
            // createLeaveCreditToDB(leaveCredit);

            //recording transaction to leave_credit_transaction table
            LeaveCreditTransaction leaveCreditTransaction = new LeaveCreditTransaction();
            leaveCreditTransaction.setDatePosted(new Date());
            leaveCreditTransaction.setEmpId(empID);
            leaveCreditTransaction.setPostedBy(user_email);
            leaveCreditTransaction.setPrevSlBalance(0.0);
            leaveCreditTransaction.setPrevVlBalance(0.0);
            leaveCreditTransaction.setRemarks(remarks);   // States in the remarks the details of new registrants records of leave_credit with document Id.. 
                                                          //(leave credit balance from physical document as of date....)
            leaveCreditTransaction.setSlAmount(SlAmount);
            leaveCreditTransaction.setVlAmount(VlAmount);
            leaveCreditTransaction.setTransactionReferrenceId(transactionReferrenceId);
            leaveCreditTransaction.setTransactionType(LeaveCreditTransaction.TYPE_INITIAL_VALUE);

            leaveCreditTransactionRepository.save(leaveCreditTransaction);

            return new ResponseEntity<Object>(generalService.renderJsonResponse("201", "Success",leaveCreditTransaction),
            HttpStatus.CREATED);

        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<Object>(generalService.renderJsonResponse("500", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional("LcmTransactionManager")
    public ResponseEntity<Object> updateLeaveCredit(@RequestBody Map<String,Object> params, String user_email){
        try{
            String id = generalService.getString(params.get("id"));
            String empID = generalService.getString(params.get("empID"));    
            String firstname = generalService.getString(params.get("firstname"));  
            String middlename = generalService.getString(params.get("middlename"));  
            String lastname = generalService.getString(params.get("lastname"));  
            String position = generalService.getString(params.get("position"));  
            String department = generalService.getString(params.get("department"));  
            Double salary = Double.valueOf(params.get("salary").toString());
            Double VlAmount = Double.valueOf(params.get("VlAmount").toString());
            Double SlAmount = Double.valueOf(params.get("SlAmount").toString());
            String remarks = params.get("remarks").toString();
            String update_credits_remarks = params.get("update_credits_remarks")==null?null:params.get("update_credits_remarks").toString();
            String transactionReferrenceId = params.get("transactionReferrenceId").toString();

            if(empID == null | firstname == null | lastname == null | position == null |department == null )
                return new ResponseEntity<Object>(generalService.renderJsonResponse("400", "Invalid value"),HttpStatus.BAD_REQUEST); 
            
            Optional <LeaveCredit> item= leaveCreditRepository.findByEmpId(id);
            LeaveCredit oldLeaveCredit = item.get();
            Double prevSlBalance = oldLeaveCredit.getTotalSlEarned();
            Double prevVlBalance = oldLeaveCredit.getTotalVlEarned();

            //updating into leave_credit table
            LeaveCredit leaveCredit =  new LeaveCredit();
            leaveCredit.setEmpId(empID);
            leaveCredit.setFirstname(firstname);
            leaveCredit.setMiddlename(middlename);
            leaveCredit.setLastname(lastname);
            leaveCredit.setPosition(position);
            leaveCredit.setDepartment(department);
            leaveCredit.setSalary(BigDecimal.valueOf(salary));
            leaveCredit.setTotalSlEarned(SlAmount);
            leaveCredit.setTotalVlEarned(VlAmount);
            leaveCredit.setRemarks(remarks);
            leaveCredit.setLastUpdated(new Date());

            leaveCreditRepository.save(leaveCredit);

            //recording transaction to leave_credit_transaction table
            LeaveCreditTransaction leaveCreditTransaction = new LeaveCreditTransaction();
            leaveCreditTransaction.setDatePosted(new Date());
            leaveCreditTransaction.setEmpId(empID);
            leaveCreditTransaction.setPostedBy(user_email);
            leaveCreditTransaction.setPrevSlBalance(prevSlBalance);
            leaveCreditTransaction.setPrevVlBalance(prevVlBalance);
            leaveCreditTransaction.setRemarks(update_credits_remarks!=null?update_credits_remarks:remarks);   // States in the remarks the details of updated registrants records of leave_credit with document Id.. 
                                                          //(leave credit balance from physical document as of date....)
            leaveCreditTransaction.setSlAmount(SlAmount);
            leaveCreditTransaction.setVlAmount(VlAmount);
            leaveCreditTransaction.setTransactionReferrenceId(transactionReferrenceId);
            leaveCreditTransaction.setTransactionType(update_credits_remarks!=null?LeaveCreditTransaction.TYPE_ADJUSTMENT_VALUE:LeaveCreditTransaction.TYPE_UPDATE_ENTRY);

            leaveCreditTransactionRepository.save(leaveCreditTransaction);

            return new ResponseEntity<Object>(generalService.renderJsonResponse("200", "Success",leaveCredit),
            HttpStatus.OK);

        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<Object>(generalService.renderJsonResponse("500", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
/*
    @Transactional("LcmTransactionManager")
    public ResponseEntity<Object> deleteLeaveCreditTransaction(@RequestBody Map<String,Object> params){
        try{
            Long id = Long.valueOf(params.get("id").toString());
            
            if(!leaveApplicationRepository.hasLeaveType(id).isEmpty())
                return new ResponseEntity<Object>(generalService.renderJsonResponse("400", "ForeignKey Constraint"),HttpStatus.BAD_REQUEST);              

            leaveTypeRepository.deleteById(id);

            return new ResponseEntity<Object>(generalService.renderJsonResponse("200", "Success"),
            HttpStatus.OK);

        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<Object>(generalService.renderJsonResponse("500", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
*/
    @Transactional("LcmTransactionManager")
    public ResponseEntity<Object> searchLeaveCreditTransaction(@RequestBody Map<String,Object> params){
        try{
            String name = generalService.getString(params.get("name"));        
            String position = generalService.getString(params.get("position"));
            String department = generalService.getString(params.get("department"));
            String empId = generalService.getString(params.get("empId"));
            String searchValue = generalService.getString(params.get("searchValue"));
            System.out.println("searchValue:"+searchValue);
            //Paging
            int pageNo = (Integer)(params.get("pageNo"));
            int pageSize = (Integer)(params.get("pageSize"));

            Pageable limit = pageSize == 0?Pageable.unpaged():PageRequest.of(pageNo,pageSize,Sort.by("emp_id").ascending());

            Page<LeaveCredit> leaveCredits = (searchValue==null)?leaveCreditRepository.searchLeaveCredit(name,position,department,empId, limit):
                                                                 leaveCreditRepository.searchValue(searchValue,searchValue,searchValue,searchValue, limit);
            return new ResponseEntity<Object>(generalService.renderJsonResponse("200", "Success","leaveCredits",leaveCredits),
            HttpStatus.OK);

        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<Object>(generalService.renderJsonResponse("500", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }    

    @Transactional("LcmTransactionManager")
    public ResponseEntity<Object> getDepartmentList(){
        try{
            
            List<String> departmenList = leaveCreditRepository.getDepartment();
            return new ResponseEntity<Object>(generalService.renderJsonResponse("200", "Success","departmenList",departmenList),
            HttpStatus.OK);

        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<Object>(generalService.renderJsonResponse("500", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }    
     
}
