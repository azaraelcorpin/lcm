package com.ems.lcm.DataSource_LCM.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.ems.lcm.LcmApplication;
import com.ems.lcm.DataSource_LCM.model.LeaveApplication;
import com.ems.lcm.DataSource_LCM.model.LeaveDate;
import com.ems.lcm.DataSource_LCM.repository.LeaveApplicationRepository;
import com.ems.lcm.DataSource_LCM.repository.LeaveDateRepository;
import com.ems.lcm.DataSource_LCM.repository.LeaveTypeRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LeaveApplicationServices {
    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

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
            String department = params.get("department").toString();
            String lastname = params.get("lastname").toString();
            String firstname = params.get("firstname").toString();
            String middlename = params.get("middlename").toString();
            String position = params.get("position").toString();
            BigDecimal salary = BigDecimal.valueOf(Double.parseDouble( params.get("salary").toString()));
            Long leaveTypeId = Long.valueOf(params.get("leaveTypeId").toString());
            String othersLeaveType = params.get("othersLeaveType").toString();
            String leaveDetails = params.get("leaveDetails").toString();
            List<Date> list = new Gson().fromJson(params.get("leaveDates").toString(), new TypeToken<List<Date>>() {}.getType());
            String commutation = params.get("commutation").toString();
            String transactionReferrenceId = params.get("transactionReferrenceId").toString();

            LeaveApplication leaveApplication = new LeaveApplication();
            leaveApplication.setEmpId(empId);
            leaveApplication.setDepartment(department);
            leaveApplication.setLastname(lastname);
            leaveApplication.setFirstname(firstname);
            leaveApplication.setMiddlename(middlename);
            leaveApplication.setPosition(position);
            leaveApplication.setSalary(salary);
            leaveApplication.setLeaveTypeId(leaveTypeId);
            leaveApplication.setOthersLeaveType(othersLeaveType);
            leaveApplication.setLeaveDetails(leaveDetails);
            leaveApplication.setCommutation(commutation);
            leaveApplication.setTransactionReferrenceId(transactionReferrenceId);
            leaveApplicationRepository.save(leaveApplication);
            //saving leavedates
            List<LeaveDate> leaveDates = new ArrayList<>();
            for (Date date : list) {
                LeaveDate leaveDate = new LeaveDate();
                leaveDate.setDate(date);
                leaveDate.setOffsetTo(getOffset(leaveTypeId));
                leaveDate.setTransactionReferrenceId(transactionReferrenceId);
                leaveDate.setLeaveApplicationId(leaveApplication.getId());
                leaveDateRepository.save(leaveDate);
            }

            return new ResponseEntity<Object>(generalService.renderJsonResponse("201", "Success",leaveApplication),
            HttpStatus.CREATED);

        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<Object>(generalService.renderJsonResponse("500", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @Transactional("LcmTransactionManager")
    // public ResponseEntity<Object> updateLeaveType(@RequestBody Map<String,Object> params){
    //     try{
    //         Long id = Long.valueOf(params.get("id").toString());
    //         String code = generalService.getString(params.get("code"));        
    //         String description = generalService.getString(params.get("description"));
    //         int numberOfDays = (Integer)(params.get("numberOfDays"));
    //         if(code == null | description == null)
    //             return new ResponseEntity<Object>(generalService.renderJsonResponse("400", "Invalid value"),HttpStatus.BAD_REQUEST);              

    //         LeaveType leaveType = new LeaveType();
    //         leaveType.setId(id);
    //         leaveType.setCode(code);
    //         leaveType.setDescription(description);
    //         leaveType.setNumberOfDays(numberOfDays);
    //         leaveTypeRepository.save(leaveType);

    //         return new ResponseEntity<Object>(generalService.renderJsonResponse("201", "Success",leaveType),
    //         HttpStatus.CREATED);

    //     }catch(Exception e){
    //         log.error(e.getMessage());
    //         return new ResponseEntity<Object>(generalService.renderJsonResponse("500", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    // @Transactional("LcmTransactionManager")
    // public ResponseEntity<Object> deleteLeaveType(@RequestBody Map<String,Object> params){
    //     try{
    //         Long id = Long.valueOf(params.get("id").toString());
            
    //         if(!leaveApplicationRepository.hasLeaveType(id).isEmpty())
    //             return new ResponseEntity<Object>(generalService.renderJsonResponse("400", "ForeignKey Constraint"),HttpStatus.BAD_REQUEST);              

    //         leaveTypeRepository.deleteById(id);

    //         return new ResponseEntity<Object>(generalService.renderJsonResponse("200", "Success"),
    //         HttpStatus.OK);

    //     }catch(Exception e){
    //         log.error(e.getMessage());
    //         return new ResponseEntity<Object>(generalService.renderJsonResponse("500", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    // @Transactional("LcmTransactionManager")
    // public ResponseEntity<Object> searchLeaveType(@RequestBody Map<String,Object> params){
    //     try{
    //         String code = generalService.getString(params.get("code"));        
    //         String description = generalService.getString(params.get("description"));
            
    //         //Paging
    //         int pageNo = (Integer)(params.get("pageNo"));
    //         int pageSize = (Integer)(params.get("pageSize"));

    //         Pageable limit = pageSize == 0?Pageable.unpaged():PageRequest.of(pageNo,pageSize,Sort.by("code").ascending());

    //         Page<LeaveType> leaveTypes = leaveTypeRepository.searchLeaveType(code, description, limit);
    //         return new ResponseEntity<Object>(generalService.renderJsonResponse("200", "Success","leaveTypes",leaveTypes),
    //         HttpStatus.OK);

    //     }catch(Exception e){
    //         log.error(e.getMessage());
    //         return new ResponseEntity<Object>(generalService.renderJsonResponse("500", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }    

}
