package com.otbs.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.otbs.model.Connection;
import com.otbs.model.Customer;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

@Controller
public class ConnectionClientController {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionClientController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${backend.api.connection.base.url}")
    private String connectionBaseUrl;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/connection-management")
    public String connectionManagementPage() {
        return "connection-management";
    }

    @GetMapping("/add-connection")
    public String showActivatePage(Model model, HttpSession session) {
    	int customerId= (int)session.getAttribute("customerId");
    	model.addAttribute("customerId",customerId);
        model.addAttribute("connection", new Connection());
        return "add-connection";
    }

    @PostMapping("/add-connection")
    public String addNewConnection(@ModelAttribute Connection connection, Model model) {
    	System.out.println("print connection");
        try {
            ResponseEntity<Connection> response = restTemplate.postForEntity(connectionBaseUrl + "/activate", connection, Connection.class);
            model.addAttribute("message", "Connection activated successfully! Connection ID: " + response.getBody().getConnectionId());
        } catch (Exception e) {
        	e.printStackTrace();
            model.addAttribute("error", "Failed to activate connection: " + e.getMessage());
        }
        return "activation-success";
    }

    @GetMapping("/upgrade-connection")
    public String getUpgradeConnectionPage() {
        return "upgrade-connection";
    }

    @PostMapping("/upgrade-connection")
    public String upgradeConnection(@RequestParam int connectionId, @ModelAttribute Connection connection, Model model) {
        try {
            String url = connectionBaseUrl + "/upgrade/" + connectionId;
            ResponseEntity<Connection> response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(connection), Connection.class);
            model.addAttribute("message", "Connection updated successfully!");
            return "upgrade-success";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update connection: " + e.getMessage());
        }
        return "upgrade-success";
    }

    @GetMapping("/delete-connection")
    public String getDeleteConnectionPage() {
        return "delete-connection";
    }

    @PostMapping("/delete-connection")
    public String deleteConnection(@RequestParam int connectionId, @ModelAttribute Connection connection, Model model) {
        try {
            String url = connectionBaseUrl + "/deletebyId/" + connectionId;
            ResponseEntity<Connection> response = restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(connection), Connection.class);
            model.addAttribute("message", "Connection deleted successfully!");
            return "delete-success";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to delete connection: " + e.getMessage());
        }
        return "delete-success";
    }

    @GetMapping("/view-all-connections")
    public String getViewAllConnectionsPage(Model model) {
        Connection[] connectionsArray = restTemplate.getForObject(connectionBaseUrl + "/all", Connection[].class);
        List<Connection> connections = Arrays.asList(connectionsArray);
        model.addAttribute("connections", connections);
        return "view-all-connections";
    }

    @GetMapping("/viewconnectionbyId")
    public String viewConnectionById(@RequestParam(value = "connectionId", required = false) Integer connectionId, Model model) {
        if (connectionId == null) {
            model.addAttribute("error", "Connection ID parameter is missing.");
            logger.error("Connection ID parameter is missing.");
            return "viewconnectionbyId"; // Return the view with the error message
        }

        logger.info("Received request to view connection with connectionId: {}", connectionId);
        try {
            ResponseEntity<Connection> response = restTemplate.getForEntity(connectionBaseUrl + "/getbyId/" + connectionId, Connection.class);
            model.addAttribute("connection", response.getBody());
        } catch (HttpClientErrorException e) {
            logger.error("Client error: {}", e.getMessage());
            model.addAttribute("error", "Client error: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            logger.error("Server error: {}", e.getMessage());
            model.addAttribute("error", "Server error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("An error occurred: {}", e.getMessage());
            model.addAttribute("error", "An error occurred: " + e.getMessage());
        }
        return "viewconnectionbyId";
    }
    
    @GetMapping("/connections-near-expiry")
    public String getConnectionsNearExpiry(@RequestParam(defaultValue = "7") int daysThreshold, Model model) {
        try {
            // Call the backend endpoint
            String url = connectionBaseUrl + "/connections-nearing-expiry?daysThreshold=" + daysThreshold;
            Connection[] connectionsArray = restTemplate.getForObject(url, Connection[].class);
            List<Connection> connections = Arrays.asList(connectionsArray);

            // Pass the data to the frontend
            model.addAttribute("connections", connections);
            model.addAttribute("daysThreshold", daysThreshold);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to fetch connections near expiry: " + e.getMessage());
        }
        return "connections-near-expiry";
    }
    
    
    @GetMapping("/view-my-connections")
    public String viewMyConnections(HttpSession session, Model model) {
        try {
            // Retrieve customer ID from session
            Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
            if (loggedInCustomer == null) {
                model.addAttribute("error", "Please log in to view your connections.");
                return "login"; // Redirect to login page if no customer is in session
            }

            int customerId = loggedInCustomer.getCustomerId();
            logger.info("Fetching connections for customer ID: {}", customerId);

            // Call the backend API to fetch connections for the specific customer
            String url = connectionBaseUrl + "/customer/" + customerId + "/connections";
            ResponseEntity<Connection[]> response = restTemplate.getForEntity(url, Connection[].class);
            List<Connection> connections = Arrays.asList(response.getBody());

            // Add connections to the model
            model.addAttribute("connections", connections);
        } catch (Exception e) {
            logger.error("Error fetching connections: {}", e.getMessage());
            model.addAttribute("error", "Unable to fetch connections at this time. Please try again later.");
        }
        return "view-my-connections"; // Corresponding Thymeleaf template
    }
    
    
}
