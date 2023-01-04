package com.ems.lcm.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ems.lcm.DataSource_LCM.Service.GeneralService;
import com.ems.lcm.DataSource_LCM.Service.LeaveCreditServices;
import com.ems.lcm.DataSource_LCM.Service.LeaveTypeServices;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/leaveCredit/")
public class LeaveCreditController {
    @Autowired
    private LeaveCreditServices leaveCreditServices;

    @PostMapping(path = "search")
    public ResponseEntity<Object> searchLeaveType(@RequestBody Map<String,Object> params){
        //insert here for authentication from request header! value=authorization.
        return leaveCreditServices.searchLeaveCreditTransaction(params);
    }

    @PostMapping(path = "getDepartments")
    public ResponseEntity<Object> getDepartmentList(){
        //insert here for authentication from request header! value=authorization.
        return leaveCreditServices.getDepartmentList();
    }

    @PostMapping(path = "create")
    public ResponseEntity<Object> createLeaveCredit(@RequestBody Map<String,Object> params, @RequestHeader Map<String,Object> header){
        //insert here for authentication from request header! value=authorization.
        String u_email = header.get("authorization").toString();
        return leaveCreditServices.createLeaveCredit(params,u_email);
    }

    @PostMapping(path = "update")
    public ResponseEntity<Object> updateLeaveType(@RequestBody Map<String,Object> params,@RequestHeader Map<String,Object> header){
        //insert here for authentication from request header! value=authorization.
        String u_email = header.get("authorization").toString();
        return leaveCreditServices.updateLeaveCredit(params,u_email);
    }    

    // @PostMapping(path = "delete")
    // public ResponseEntity<Object> deleteLeaveType(@RequestBody Map<String,Object> params){
    //     //insert here for authentication from request header! value=authorization.
    //     return leaveCreditServices.deleteLeaveType(params);
    // }    
    
}
