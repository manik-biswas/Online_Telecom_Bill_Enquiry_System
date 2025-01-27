package com.otbs.service;

import com.otbs.model.Admin;
import com.otbs.model.HelpDeskExecutive;
import com.otbs.repository.HelpDeskExecutiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HelpDeskExecutiveService {

    @Autowired
    private HelpDeskExecutiveRepository helpdeskExecutiveRepository;
    
    public HelpDeskExecutiveService(HelpDeskExecutiveRepository hdeRepository) {
		this.helpdeskExecutiveRepository = hdeRepository;
	}

    // Add a new help desk executive
//    public HelpDeskExecutive addHelpDeskExecutive(HelpDeskExecutive executive) {
//        return helpdeskExecutiveRepository.save(executive);
//    }
//
//    // Retrieve all help desk executives
//    public List<HelpDeskExecutive> getAllHelpDeskExecutives() {
//        return helpdeskExecutiveRepository.findAll();
//    }
    public HelpDeskExecutive authenticate(String username, String password) {
    	HelpDeskExecutive helpdeskExecutive = helpdeskExecutiveRepository.findByUsernameAndPassword(username, password);
        if (helpdeskExecutive != null) {
            return helpdeskExecutive;
        }
        return null;
    }
}
