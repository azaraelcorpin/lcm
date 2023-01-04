package com.ems.lcm.DataSource_LCM.Service;

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

import com.ems.lcm.DataSource_LCM.model.LeaveApplication;
import com.ems.lcm.DataSource_LCM.model.LeaveCredit;
import com.ems.lcm.DataSource_LCM.model.LeaveCreditTransaction;
import com.ems.lcm.DataSource_LCM.model.LeaveDate;
import com.ems.lcm.DataSource_LCM.repository.LeaveApplicationRepository;
import com.ems.lcm.DataSource_LCM.repository.LeaveCreditRepository;
import com.ems.lcm.DataSource_LCM.repository.LeaveCreditTransactionRepository;
import com.ems.lcm.DataSource_LCM.repository.LeaveDateRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LeaveApplicationServices {
    @Autowired
    private LeaveCreditTransactionRepository leaveCreditTransactionRepository;

    @Autowired
    private LeaveCreditRepository leaveCreditRepository;

    @Autowired
    private LeaveDateRepository leaveDateRepository;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private GeneralService generalService;

    private String getOffset(Long leaveTypeId){
        // value id 1 is intended for VACATION LEAVE (VL)
        // value id 2 is intended for MANDATORY/FORCE LEAVE (FL)
        // value id 3 is intended for SICK LEAVE (SL)
        Long VL_id = Long.valueOf("1");
        Long FL_id = Long.valueOf("2");
        Long SL_id = Long.valueOf("3");

        if(leaveTypeId==VL_id|leaveTypeId==FL_id)
            return "VL";
        if(leaveTypeId==SL_id)
            return "SL";
        return null;
    }

    @Transactional("LcmTransactionManager")
    public ResponseEntity<Object> createLeaveApplication(@RequestBody Map<String,Object> params){
        try{
            String empId =  params.get("empId").toString();
            Long leaveTypeId = Long.valueOf(params.get("leaveTypeId").toString());
            String offsetTo = params.get("offsetTo").toString();
            String othersLeaveType = params.get("othersLeaveType").toString();
            String leaveDetails = params.get("leaveDetails").toString();
            String leaveDates = params.get("leaveDates").toString();
            List<Date> list = new Gson().fromJson(params.get("leaveDates").toString(), new TypeToken<List<Date>>() {}.getType());
            String commutation = params.get("commutation").toString();
            String transactionReferrenceId = params.get("transactionReferrenceId").toString();

            LeaveApplication leaveApplication = new LeaveApplication();
            leaveApplication.setApplicationDate(new Date());
            leaveApplication.setEmpId(empId);
            leaveApplication.setLeaveTypeId(leaveTypeId);
            leaveApplication.setOthersLeaveType(othersLeaveType);
            leaveApplication.setLeaveDetails(leaveDetails);
            leaveApplication.setCommutation(commutation);
            leaveApplication.setTransactionReferrenceId(transactionReferrenceId);
            leaveApplication.setLeaveDates(leaveDates);
            leaveApplicationRepository.save(leaveApplication);
            //saving leavedates
            for (Date date : list) {
                LeaveDate leaveDate = new LeaveDate();
                leaveDate.setDate(date);
                leaveDate.setOffsetTo(offsetTo);
                leaveDate.setTransactionReferrenceId(transactionReferrenceId);
                leaveDate.setLeaveApplicationId(leaveApplication.getId());
                leaveDateRepository.save(leaveDate);
            }

            return new ResponseEntity<Object>(generalService.renderJsonResponse("201", "Success",leaveApplication),
            HttpStatus.CREATED);

        }catch(Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<Object>(generalService.renderJsonResponse("500", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional("LcmTransactionManager")
    public ResponseEntity<Object> updateLeaveApplication(@RequestBody Map<String,Object> params){
        try{
            Long id = Long.valueOf(params.get("id").toString());
            Long leaveTypeId = Long.valueOf(params.get("leaveTypeId").toString());
            String offsetTo = params.get("offsetTo").toString();
            String othersLeaveType = params.get("othersLeaveType").toString();
            String leaveDetails = params.get("leaveDetails").toString();
            String leaveDates = params.get("leaveDates").toString();
            List<Date> list = new Gson().fromJson(params.get("leaveDates").toString(), new TypeToken<List<Date>>() {}.getType());
            String commutation = params.get("commutation").toString();
            String transactionReferrenceId = params.get("transactionReferrenceId").toString();

            LeaveApplication leaveApplication = new LeaveApplication();
            leaveApplication.setApplicationDate(new Date()   );
            leaveApplication.setId(id);
            leaveApplication.setLeaveTypeId(leaveTypeId);
            leaveApplication.setOthersLeaveType(othersLeaveType);
            leaveApplication.setLeaveDetails(leaveDetails);
            leaveApplication.setCommutation(commutation);
            leaveApplication.setTransactionReferrenceId(transactionReferrenceId);
            leaveApplication.setLeaveDates(leaveDates);
            leaveApplicationRepository.save(leaveApplication);
            //removing old dates list
                leaveDateRepository.deleteAllByLeaveApplicationId(id);
            //saving new leavedates
            for (Date date : list) {
                LeaveDate leaveDate = new LeaveDate();
                leaveDate.setDate(date);
                leaveDate.setOffsetTo(offsetTo);
                leaveDate.setTransactionReferrenceId(transactionReferrenceId);
                leaveDate.setLeaveApplicationId(id);
                leaveDateRepository.save(leaveDate);
            }

            return new ResponseEntity<Object>(generalService.renderJsonResponse("200", "Success",leaveApplication),
            HttpStatus.OK);

        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<Object>(generalService.renderJsonResponse("500", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional("LcmTransactionManager")
    public ResponseEntity<Object> deleteLeaveApplication(@RequestBody Map<String,Object> params){
        try{
            Long id = Long.valueOf(params.get("id").toString());
            Optional<LeaveApplication> o = leaveApplicationRepository.findById(id);
            LeaveApplication l = o.get();
            if(l.getStatus()!="APPLIED" && l.getStatus()!="INVALID")
                return new ResponseEntity<Object>(generalService.renderJsonResponse("400", "Invalid Status value"),HttpStatus.BAD_REQUEST);              
            
            //remove list of dates
            leaveDateRepository.deleteAllByLeaveApplicationId(id);
            
            leaveApplicationRepository.deleteById(id);

            return new ResponseEntity<Object>(generalService.renderJsonResponse("200", "Successfully Deleted",l),
            HttpStatus.OK);

        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<Object>(generalService.renderJsonResponse("500", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional("LcmTransactionManager")
    public ResponseEntity<Object> searchLeaveApplication(@RequestBody Map<String,Object> params){
        try{
            String empId =  generalService.getString(params.get("empId"));
            Date dateFrom = (Date) params.get("dateFrom");
            Date dateTo = (Date) params.get("dateTo");
            String transactionReferrenceId =  generalService.getString(params.get("transactionReferrenceId"));
            String department =  generalService.getString(params.get("department"));
            String position =  generalService.getString(params.get("position"));
            String employeeName =  generalService.getString(params.get("employeeName"));
            Long leaveTypeId =  generalService.toLong(params.get("leaveTypeId"));
            String leaveDetails =  generalService.getString(params.get("leaveDetails"));
            String leaveDate = generalService.getString(params.get("leaveDate"));
            String commutation =  generalService.getString(params.get("commutation"));
            String status =  generalService.getString(params.get("status"));
            System.out.println("empId"+empId);   
            System.out.println("dateFrom"+dateFrom); 
            System.out.println("dateTo"+dateTo); 
            System.out.println("transactionReferrenceId"+transactionReferrenceId); 
            System.out.println("department"+department);         
            System.out.println("position"+position); 
            System.out.println("employeeName"+employeeName); 
            System.out.println("leaveTypeId"+leaveTypeId); 
            System.out.println("leaveDetails"+leaveDetails); 
            System.out.println("leaveDate"+leaveDate); 
            System.out.println("commutation"+commutation); 
            System.out.println("status"+status); 
            //Paging
            int pageNo = (Integer)(params.get("pageNo"));
            int pageSize = (Integer)(params.get("pageSize"));

            Pageable limit = pageSize == 0?Pageable.unpaged():PageRequest.of(pageNo,pageSize,Sort.by("application_date").ascending());

            Page<Map<String,Object>> leaveApplications = leaveApplicationRepository.searchLeaveApplications_(
                empId,
                dateFrom,
                dateTo,
                transactionReferrenceId,
                department,
                position,
                employeeName,
                leaveTypeId,
                leaveDetails,
                leaveDate,
                commutation,
                status,
                limit
                );
            return new ResponseEntity<Object>(generalService.renderJsonResponse("200", "Success","leaveApplications",leaveApplications),
            HttpStatus.OK);

        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<Object>(generalService.renderJsonResponse("500", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }    

    @Transactional("LcmTransactionManager")
    public ResponseEntity<Object> setCertified(@RequestBody Map<String,Object> params, String user_email){
        try{
            Long id = Long.valueOf(params.get("id").toString());
            Double totalVlEarned = Double.valueOf(params.get("totalVlEarned").toString());
            Double totalSlEarned = Double.valueOf(params.get("totalSlEarned").toString());
            Double appliedVlAmount = Double.valueOf(params.get("appliedVlAmount").toString());
            Double appliedSlAmount = Double.valueOf(params.get("appliedSlAmount").toString());

            Optional<LeaveApplication> o = leaveApplicationRepository.findById(id);
            LeaveApplication l = o.get();
            if(!l.getStatus().equals(LeaveApplication.STATUS_APPLIED))
                return new ResponseEntity<Object>(generalService.renderJsonResponse("400", "Invalid Status value"),HttpStatus.BAD_REQUEST);              
            

            l.setTotalVlEarned(totalVlEarned);
            l.setTotalSlEarned(totalSlEarned);
            l.setAppliedVlAmount(appliedVlAmount);
            l.setAppliedSlAmount(appliedSlAmount);
            l.setStatus(LeaveApplication.STATUS_CERTIFIED);
            l.setCertifiedBy(user_email);
            l.setCertifiedOn(new Date());
            
            leaveApplicationRepository.save(l);

            return new ResponseEntity<Object>(generalService.renderJsonResponse("200", "Successfully Certified",l),
            HttpStatus.OK);

        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<Object>(generalService.renderJsonResponse("500", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } 

    @Transactional("LcmTransactionManager")
    public ResponseEntity<Object> setInvalid(@RequestBody Map<String,Object> params, String user_email){
        try{
            Long id = Long.valueOf(params.get("id").toString());
            Double totalVlEarned = Double.valueOf(params.get("totalVlEarned").toString());
            Double totalSlEarned = Double.valueOf(params.get("totalSlEarned").toString());
            Double appliedVlAmount = Double.valueOf(params.get("appliedVlAmount").toString());
            Double appliedSlAmount = Double.valueOf(params.get("appliedSlAmount").toString());
            String remarks = params.get("remarks").toString();

            Optional<LeaveApplication> o = leaveApplicationRepository.findById(id);
            LeaveApplication l = o.get();
            if(!l.getStatus().equals(LeaveApplication.STATUS_APPLIED))
                return new ResponseEntity<Object>(generalService.renderJsonResponse("400", "Invalid Status value"),HttpStatus.BAD_REQUEST);              
            

            l.setTotalVlEarned(totalVlEarned);
            l.setTotalSlEarned(totalSlEarned);
            l.setAppliedVlAmount(appliedVlAmount);
            l.setAppliedSlAmount(appliedSlAmount);
            l.setStatus(LeaveApplication.STATUS_INVALID);
            l.setCertifiedBy(user_email);
            l.setRemarks(remarks);
            l.setCertifiedOn(new Date());
            
            leaveApplicationRepository.save(l);

            return new ResponseEntity<Object>(generalService.renderJsonResponse("200", "Successfully Certified",l),
            HttpStatus.OK);

        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<Object>(generalService.renderJsonResponse("500", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Transactional("LcmTransactionManager")
    public ResponseEntity<Object> setRecommendation(@RequestBody Map<String,Object> params, String user_email){
        try{
            Long id = Long.valueOf(params.get("id").toString());            
            String recommendation = params.get("recommendation").toString();

            Optional<LeaveApplication> o = leaveApplicationRepository.findById(id);
            LeaveApplication l = o.get();
            if(!l.getStatus().equals(LeaveApplication.STATUS_CERTIFIED))
                return new ResponseEntity<Object>(generalService.renderJsonResponse("400", "Invalid Status value"),HttpStatus.BAD_REQUEST);              
            
            l.setRecommendationBy(user_email);
            l.setRecommendation(recommendation);
            l.setRecommendationDate(new Date());
            l.setStatus(LeaveApplication.STATUS_RECOMMENDED);
            
            leaveApplicationRepository.save(l);

            return new ResponseEntity<Object>(generalService.renderJsonResponse("200", "Successfully Recommended "+recommendation,l),
            HttpStatus.OK);

        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<Object>(generalService.renderJsonResponse("500", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional("LcmTransactionManager")
    public ResponseEntity<Object> setApproved(@RequestBody Map<String,Object> params, String user_email){
        try{
            Long id = Long.valueOf(params.get("id").toString());            
            String approvalRemarks = params.get("approvalRemarks").toString();

            Optional<LeaveApplication> o = leaveApplicationRepository.findById(id);
            LeaveApplication l = o.get();
            if(!l.getStatus().equals(LeaveApplication.STATUS_RECOMMENDED))
                return new ResponseEntity<Object>(generalService.renderJsonResponse("400", "Invalid Status value"),HttpStatus.BAD_REQUEST);              
            
            l.setRecommendationBy(user_email);
            l.setRecommendation(approvalRemarks);
            l.setStatus(LeaveApplication.STATUS_APPROVED);
            l.setApprovalDate(new Date());

             // checking if there is record
             Optional<LeaveCredit> fetch =  leaveCreditRepository.findByEmpId(l.getEmpId());
             if(!fetch.isPresent())
                 return new ResponseEntity<Object>(generalService.renderJsonResponse("404", "No employee record found in leave credit table"),HttpStatus.NOT_FOUND);
            
             LeaveCredit leaveCredit = fetch.get();
            leaveApplicationRepository.save(l);

            // Insert data to leaveCreditTransaction
            LeaveCreditTransaction creditTransaction = new LeaveCreditTransaction();
            creditTransaction.setDatePosted(new Date());
            creditTransaction.setEmpId(l.getEmpId());
            creditTransaction.setPostedBy(user_email);
            creditTransaction.setPrevSlBalance(l.getTotalSlEarned());
            creditTransaction.setPrevVlBalance(l.getTotalVlEarned());
            creditTransaction.setRemarks(l.getRemarks());
            creditTransaction.setSlAmount(l.getAppliedSlAmount());
            creditTransaction.setVlAmount(l.getAppliedVlAmount());
            creditTransaction.setTransactionReferrenceId(l.getTransactionReferrenceId());
            creditTransaction.setTransactionType(LeaveCreditTransaction.TYPE_DEDUCTION);
            
            leaveCreditTransactionRepository.save(creditTransaction);

            leaveCredit.setEmpId(l.getEmpId());
            leaveCredit.setTotalSlEarned(creditTransaction.getPrevSlBalance()-creditTransaction.getSlAmount());
            leaveCredit.setTotalVlEarned(creditTransaction.getPrevVlBalance()-creditTransaction.getVlAmount());
            
            leaveCreditRepository.save(leaveCredit);
           
            return new ResponseEntity<Object>(generalService.renderJsonResponse("200", "Successfully approved "+approvalRemarks,l),
            HttpStatus.OK);

        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<Object>(generalService.renderJsonResponse("500", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
 