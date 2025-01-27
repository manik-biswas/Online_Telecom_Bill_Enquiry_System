package com.otbs.controller;

import com.otbs.model.ConnectionLog;
import com.otbs.service.ConnectionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8090")
@RestController
@RequestMapping("/connection-log")
public class ConnectionLogController {

    @Autowired
    private ConnectionLogService connectionLogService;

    // Get all logs
    @GetMapping("/all")
    public List<ConnectionLog> getAllLogs() {
        return connectionLogService.getAllLogs();
    }

    // Get logs for a specific connection
    @GetMapping("/getlogbyId/{connectionId}")
    public List<ConnectionLog> getLogsByConnectionId(@PathVariable int connectionId) {
        return connectionLogService.getLogsByConnectionId(connectionId);
    }
}
