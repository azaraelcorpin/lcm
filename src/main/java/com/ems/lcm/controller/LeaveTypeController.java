package com.ems.lcm.controller;

import java.util.ArrayList;
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
import com.ems.lcm.DataSource_LCM.model.LeaveType;

import lombok.extern.slf4j.Slf4j;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/leaveType/")
public class LeaveTypeController {
    @Autowired
    private GeneralService generalService;

    @Autowired
    private LeaveTypeServices leaveTypeServices;


    @GetMapping(path = "test")
    public ResponseEntity<Object> test(){
        System.out.println("Testing only");
        LeaveType temp = new LeaveType();
        temp.setId(Long.valueOf("2"));
        temp.setCode("testCode");
        temp.setDescription("testDesc");
        temp.setNumberOfDays(10);
        List<LeaveType>list = new ArrayList<>();
        list.add(temp);

        try {
            
            return new ResponseEntity<Object>(generalService.renderJsonResponse("400", "ForeignKey Constraint",list),HttpStatus.BAD_REQUEST);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } 
    }

    @PostMapping(path = "search")
    public ResponseEntity<Object> searchLeaveType(@RequestBody Map<String,Object> params){
        //insert here for authentication from request header! value=authorization.
        return leaveTypeServices.searchLeaveType(params);
    }

    @PostMapping(path = "create")
    public ResponseEntity<Object> createLeaveType(@RequestBody Map<String,Object> params){
        //insert here for authentication from request header! value=authorization.
        return leaveTypeServices.createLeaveType(params);
    }

    @PostMapping(path = "update")
    public ResponseEntity<Object> updateLeaveType(@RequestBody Map<String,Object> params){
        //insert here for authentication from request header! value=authorization.
        return leaveTypeServices.updateLeaveType(params);
    }    

    @PostMapping(path = "delete")
    public ResponseEntity<Object> deleteLeaveType(@RequestBody Map<String,Object> params){
        //insert here for authentication from request header! value=authorization.
        return leaveTypeServices.deleteLeaveType(params);
    }    
    
}
