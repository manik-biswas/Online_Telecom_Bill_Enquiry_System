package com.otbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {
   @GetMapping("/clintdashboard")
    public String loginPage() {
        return "customerdashboard";
    }
}
