package com.otbs.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.otbs.model.Outlet;

@Controller
public class OutletClientController {

    private static final Logger logger = LoggerFactory.getLogger(OutletClientController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${backend.api.outlet.base.url}")
    private String outletBaseUrl;

    @GetMapping("/add-outlet")
    public String showOutletPage(Model model) {
        model.addAttribute("outlet", new Outlet());
        return "add-outlet";
    }

    @PostMapping("/add-outlet")
    public String addOutlet(Outlet outlet, Model model) {
        try {
            restTemplate.postForObject(outletBaseUrl + "/add", outlet, Outlet.class);
            model.addAttribute("message", "Outlet added successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Failed to add outlet: " + e.getMessage());
        }
        return "addOutletSuccess";
    }

    @GetMapping("/view-all-outlets")
    public String viewAllOutlets(Model model) {
        try {
            Outlet[] outletsArray = restTemplate.getForObject(outletBaseUrl + "/all", Outlet[].class);
            List<Outlet> outlets = Arrays.asList(outletsArray);
            model.addAttribute("outlets", outlets);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to retrieve outlets: " + e.getMessage());
        }
        return "viewAllOutlets";
    }

    @GetMapping("/findNearestOutlet")
    public String findNearestOutlet(@RequestParam(value = "location", required = false) String location, Model model) {
        if (location == null || location.isEmpty()) {
            model.addAttribute("error", "Location parameter is missing.");
            logger.error("Location parameter is missing.");
            return "findNearestOutlet"; // Return the view with the error message
        }

        logger.info("Received request to find nearest outlet for location: {}", location);
        try {
            ResponseEntity<Outlet> response = restTemplate.getForEntity(outletBaseUrl + "/nearest/" + location, Outlet.class);
            model.addAttribute("outlet", response.getBody());
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
        return "findNearestOutlet";
    }
}
