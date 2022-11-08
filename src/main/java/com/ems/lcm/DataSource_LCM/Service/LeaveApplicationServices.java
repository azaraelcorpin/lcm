package com.ems.lcm.DataSource_LCM.Service;

import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.ems.lcm.LcmApplication;
import com.ems.lcm.DataSource_LCM.repository.LeaveApplicationRepository;

public class LeaveApplicationServices {
    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private Logger logger = LcmApplication.LOGGER;

    @Transactional("LcmTransactionManager")
    public ResponseEntity<Object> createLeaveApplication(@RequestBody Map<String,String> params){
        
        return null;
    }
}
