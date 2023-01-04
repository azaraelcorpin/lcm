package com.ems.lcm.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ems.lcm.DataSource_LCM.Service.GeneralService;
import com.ems.lcm.DataSource_LCM.Service.LeaveApplicationServices;
import com.ems.lcm.DataSource_LCM.model.LeaveApplication;
import com.ems.lcm.DataSource_LCM.model.LeaveDate;
import com.ems.lcm.DataSource_LCM.repository.LeaveApplicationRepository;
import com.ems.lcm.DataSource_LCM.repository.LeaveDateRepository;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/leaveApplication/")
public class LeaveApplicationController {
    @Autowired
    private GeneralService generalService;

    @Autowired
    private LeaveDateRepository leaveDateRepository;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private LeaveApplicationServices leaveApplicationServices;

    @GetMapping(path = "test")
    public ResponseEntity<Object> test(){
        try {
        System.out.println("Testing only");
        LeaveApplication temp = new LeaveApplication();
        temp.setApplicationDate(new Date());
        temp.setEmpId("1001");
        temp.setLeaveTypeId(Long.valueOf("2"));
        temp.setOthersLeaveType(null);
        temp.setLeaveDetails("Abroad");
        temp.setCommutation(null);
        temp.setTransactionReferrenceId("atest123442fdet");
        leaveApplicationRepository.save(temp);
        LeaveDate a = new LeaveDate();
        a.setDate(new Date());
        a.setOffsetTo("VL");
        a.setTransactionReferrenceId(temp.getTransactionReferrenceId());
        a.setLeaveApplicationId(temp.getId());
        leaveDateRepository.save(a);
        LeaveDate b = new LeaveDate();
        b.setDate(new Date());
        b.setOffsetTo("VL");
        b.setTransactionReferrenceId(temp.getTransactionReferrenceId());
        b.setLeaveApplicationId(temp.getId());
        leaveDateRepository.save(b);
        List<LeaveDate> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        
        


        // List<LeaveApplication>list = new ArrayList<>();
        // list.add(temp);

        
            
            return new ResponseEntity<Object>(generalService.renderJsonResponse("400", "ForeignKey Constraint - "+temp.getId()),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {            
            e.printStackTrace();
            return null;
        } 
    }

    @PostMapping(path = "search")
    public ResponseEntity<Object> searchLeaveApplication(@RequestBody Map<String,Object> params){
        //insert here for authentication from request header! value=authorization.
        return leaveApplicationServices.searchLeaveApplication(params);
    }

    @PostMapping(path = "create")
    public ResponseEntity<Object> createLeaveApplication(@RequestBody Map<String,Object> params){
        //insert here for authentication from request header! value=authorization.
        return leaveApplicationServices.createLeaveApplication(params);
    }

    @PostMapping(path = "update")
    public ResponseEntity<Object> updateLeaveApplication(@RequestBody Map<String,Object> params){
        //insert here for authentication from request header! value=authorization.
        return leaveApplicationServices.updateLeaveApplication(params);
    }    

    @PostMapping(path = "delete")
    public ResponseEntity<Object> deleteLeaveApplication(@RequestBody Map<String,Object> params){
        //insert here for authentication from request header! value=authorization.
        return leaveApplicationServices.deleteLeaveApplication(params);
    }  
    
    @PostMapping(path = "certify")
    public ResponseEntity<Object> setCertified(@RequestBody Map<String,Object> params){
        //insert here for authentication from request header! value=authorization.
        String user_email = ""; //from authorized user
        return leaveApplicationServices.setCertified(params,user_email);
    } 
    
    @PostMapping(path = "invalid")
    public ResponseEntity<Object> setInvalid(@RequestBody Map<String,Object> params){
        //insert here for authentication from request header! value=authorization.
        String user_email = ""; //from authorized user
        return leaveApplicationServices.setInvalid(params,user_email);
    } 

    @PostMapping(path = "recommendation")
    public ResponseEntity<Object> setRecommendation(@RequestBody Map<String,Object> params){
        //insert here for authentication from request header! value=authorization.
        String user_email = ""; //from authorized user
        return leaveApplicationServices.setRecommendation(params,user_email);
    } 

    @PostMapping(path = "approve")
    public ResponseEntity<Object> setApproved(@RequestBody Map<String,Object> params){
        //insert here for authentication from request header! value=authorization.
        String user_email = ""; //from authorized user
        return leaveApplicationServices.setApproved(params,user_email);
    } 
}
