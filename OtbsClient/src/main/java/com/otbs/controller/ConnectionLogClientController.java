package com.otbs.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.otbs.model.ConnectionLog;

@Controller
public class ConnectionLogClientController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${backend.api.connectionLog.base.url}")
    private String connectionLogBaseUrl;

    @GetMapping("/view-all-connectionLogs")
    public String getViewAllConnectionLogsPage(Model model) {
        try {
            ConnectionLog[] connectionLogsArray = restTemplate.getForObject(connectionLogBaseUrl + "/all", ConnectionLog[].class);
            List<ConnectionLog> connectionLogs = Arrays.asList(connectionLogsArray);
            model.addAttribute("connectionLogs", connectionLogs);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to fetch connection logs: " + e.getMessage());
        }
        return "view-all-connectionLogs";
    }

    @GetMapping("/view-connection-log")
    public String getViewConnectionLogByIdPage(@RequestParam(value = "connectionId", required = false) Integer connectionId, Model model) {
        if (connectionId == null) {
            model.addAttribute("error", "Connection ID parameter is missing.");
            return "view-connection-log"; // Return the view with the error message
        }

        try {
            ResponseEntity<ConnectionLog[]> response = restTemplate.getForEntity(connectionLogBaseUrl + "/getlogbyId/" + connectionId, ConnectionLog[].class);
            ConnectionLog[] connectionLogsArray = response.getBody();
            if (connectionLogsArray != null && connectionLogsArray.length > 0) {
                model.addAttribute("connectionLog", connectionLogsArray[0]);
            } else {
                model.addAttribute("connectionLog", null);
            }
        } catch (Exception e) {
            model.addAttribute("error", "Failed to fetch connection log by ID: " + e.getMessage());
        }
        return "view-connection-log";
    }
}
