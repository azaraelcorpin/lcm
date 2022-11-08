package com.ems.lcm.DataSource_LCM.Service;

import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.ems.lcm.LcmApplication;
import com.ems.lcm.DataSource_LCM.model.LeaveType;
import com.ems.lcm.DataSource_LCM.repository.LeaveApplicationRepository;
import com.ems.lcm.DataSource_LCM.repository.LeaveTypeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LeaveTypeServices {
    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private GeneralService generalService;


    @Transactional("LcmTransactionManager")
    public ResponseEntity<Object> createLeaveType(@RequestBody Map<String,Object> params){
        try{
            String code = generalService.getString(params.get("code"));        
            String description = generalService.getString(params.get("description"));
            int numberOfDays = (Integer)(params.get("numberOfDays"));
            if(code == null | description == null)
                return new ResponseEntity<Object>(generalService.renderJsonResponse("400", "Invalid value"),HttpStatus.BAD_REQUEST);              

            LeaveType leaveType = new LeaveType();
            leaveType.setCode(code);
            leaveType.setDescription(description);
            leaveType.setNumberOfDays(numberOfDays);
            leaveTypeRepository.save(leaveType);

            return new ResponseEntity<Object>(generalService.renderJsonResponse("201", "Success",leaveType),
            HttpStatus.CREATED);

        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<Object>(generalService.renderJsonResponse("500", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional("LcmTransactionManager")
    public ResponseEntity<Object> updateLeaveType(@RequestBody Map<String,Object> params){
        try{
            Long id = (Long)(params.get("id"));
            String code = generalService.getString(params.get("code"));        
            String description = generalService.getString(params.get("description"));
            int numberOfDays = (Integer)(params.get("numberOfDays"));
            if(code == null | description == null)
                return new ResponseEntity<Object>(generalService.renderJsonResponse("400", "Invalid value"),HttpStatus.BAD_REQUEST);              

            LeaveType leaveType = new LeaveType();
            leaveType.setId(id);
            leaveType.setCode(code);
            leaveType.setDescription(description);
            leaveType.setNumberOfDays(numberOfDays);
            leaveTypeRepository.save(leaveType);

            return new ResponseEntity<Object>(generalService.renderJsonResponse("201", "Success",leaveType),
            HttpStatus.CREATED);

        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<Object>(generalService.renderJsonResponse("500", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional("LcmTransactionManager")
    public ResponseEntity<Object> deleteLeaveType(@RequestBody Map<String,Object> params){
        try{
            Long id = (Long)(params.get("id"));
            
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

    @Transactional("LcmTransactionManager")
    public ResponseEntity<Object> searchLeaveType(@RequestBody Map<String,Object> params){
        try{
            String code = generalService.getString(params.get("code"));        
            String description = generalService.getString(params.get("description"));
            
            //Paging
            int pageNo = (Integer)(params.get("pageNo"));
            int pageSize = (Integer)(params.get("pageSize"));

            Pageable limit = pageSize == 0?Pageable.unpaged():PageRequest.of(pageNo,pageSize,Sort.by("code").ascending());

            Page<LeaveType> leaveTypes = leaveTypeRepository.searchLeaveType(code, description, limit);
            return new ResponseEntity<Object>(generalService.renderJsonResponse("200", "Success","leaveTypes",leaveTypes),
            HttpStatus.OK);

        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<Object>(generalService.renderJsonResponse("500", e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }    
}
