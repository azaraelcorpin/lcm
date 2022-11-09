package com.ems.lcm.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ems.lcm.DataSource_LCM.Service.GeneralService;
import com.ems.lcm.DataSource_LCM.Service.LeaveTypeServices;
import com.ems.lcm.DataSource_LCM.model.LeaveApplication;
import com.ems.lcm.DataSource_LCM.model.LeaveDate;
import com.ems.lcm.DataSource_LCM.model.LeaveType;
import com.ems.lcm.DataSource_LCM.repository.LeaveApplicationRepository;
import com.ems.lcm.DataSource_LCM.repository.LeaveDateRepository;
import com.ems.lcm.DataSource_LCM.repository.LeaveTypeRepository;

import lombok.extern.slf4j.Slf4j;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/leaveApplication/")
public class LeaveApplicationController {
    @Autowired
    private GeneralService generalService;

    @Autowired
    private LeaveDateRepository leaveDateRepository;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @GetMapping(path = "test")
    public ResponseEntity<Object> test(){
        try {
        System.out.println("Testing only");
        LeaveApplication temp = new LeaveApplication();
        temp.setApplicationDate(new Date());
        temp.setEmpId("1001");
        temp.setDepartment("testDept");
        temp.setLastname("dela Cruz");
        temp.setFirstname("Juan");
        temp.setMiddlename("Pinoy");
        temp.setPosition("Janitor");
        temp.setSalary(BigDecimal.valueOf(12000.00));
        temp.setLeaveTypeId(Long.valueOf("2"));
        temp.setOthersLeaveType(null);
        temp.setLeaveDetails("Abroad");
        temp.setCommutation(null);
        temp.setTransactionReferrenceId("atest123442fde4");
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
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } 
    }

    // @PostMapping(path = "search")
    // public ResponseEntity<Object> searchLeaveType(@RequestBody Map<String,Object> params){
    //     //insert here for authentication from request header! value=authorization.
    //     return leaveTypeServices.searchLeaveType(params);
    // }

    // @PostMapping(path = "create")
    // public ResponseEntity<Object> createLeaveType(@RequestBody Map<String,Object> params){
    //     //insert here for authentication from request header! value=authorization.
    //     return leaveTypeServices.createLeaveType(params);
    // }

    // @PostMapping(path = "update")
    // public ResponseEntity<Object> updateLeaveType(@RequestBody Map<String,Object> params){
    //     //insert here for authentication from request header! value=authorization.
    //     return leaveTypeServices.updateLeaveType(params);
    // }    

    // @PostMapping(path = "delete")
    // public ResponseEntity<Object> deleteLeaveType(@RequestBody Map<String,Object> params){
    //     //insert here for authentication from request header! value=authorization.
    //     return leaveTypeServices.deleteLeaveType(params);
    // }    
    
}
