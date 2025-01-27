package com.otbs.controller;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.otbs.model.Plan;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/plan")
public class PlanClientController {
	
	 @Autowired
	    private RestTemplate restTemplate;

	    @Value("${backend.api.plan.based.url}")
	    private String planBasedUrl;	
	
	
	@GetMapping("/")
	public String home() {
		return "index";
	}
	
	@GetMapping("/manage")
	public String planManagement(Model model, HttpSession session) {
		String role=(String)session.getAttribute("role");
		model.addAttribute("role",role);
		return "managePlan";
	}
	
	
	@GetMapping("/adminDashboard")
	public String moveToDashboard(Model model, HttpSession session) {
		String role=(String)session.getAttribute("role");
		model.addAttribute("role",role);
		return "admindashboard";
	}
	
	
	@GetMapping("/customerDashboard")
	public String customerDashboard(Model model, HttpSession session) {
		String role=(String)session.getAttribute("role");
		model.addAttribute("role",role);
		return "customerdashboard";
	}
	
	@GetMapping("/viewAllPlans")
    public String listPlans(Model model) {
        try {
            ResponseEntity<List> response = restTemplate.getForEntity(planBasedUrl + "/getAll", List.class);
            List plans = response.getBody();
            model.addAttribute("listOfPlans", plans);
        } catch (HttpClientErrorException |HttpServerErrorException e) {
        	
        	Map<String, String> errors=null;
        	try {
        			errors = new ObjectMapper().readValue(e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
        		} catch (JsonProcessingException e1) {
        					e1.printStackTrace();
        			}
        	           model.addAttribute("error", errors);
        	
//	        String responseBody = e.getResponseBodyAsString();
//	        // Extract the message part manually (basic approach)
//	        String errorMessage = responseBody.replace("{\"message\":\"", "").replace("\"}", "");
//	        model.addAttribute("error","Failed to fetch plans : "+ errorMessage);
//	    }  
//        catch (Exception e) {
//            model.addAttribute("error", "Failed to fetch plans: " + e.getMessage());
        }
        return "viewAllPlans";
    }

	
	
//	@GetMapping("/searchById")
//    public String searchPlanById(@RequestParam("planId") int planId, Model model) {
//        try {
//            ResponseEntity<Plan> response = restTemplate.getForEntity(planBasedUrl + "/getId/" + planId, Plan.class);
//            model.addAttribute("plan", response.getBody());
//        } catch (Exception e) {
//            model.addAttribute("error", "Plan not found: " + e.getMessage());
//        }
//        return "searchResultPlan";
//    }
	
	
	@GetMapping("/searchById")
	public String searchPlanById(@RequestParam("planId") int planId, Model model) {
	    try {
	        ResponseEntity<Plan> response = restTemplate.getForEntity(planBasedUrl + "/getId/" + planId, Plan.class);
	        model.addAttribute("plan", response.getBody());
	    } 
	        catch (HttpClientErrorException |HttpServerErrorException e) {
	        	Map<String, String> errors=null;
	        	try {
	        			errors = new ObjectMapper().readValue(e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
	        		} catch (JsonProcessingException e1) {
	        					e1.printStackTrace();
	        			}
	        	           model.addAttribute("error",errors);
	        	
//	        model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
	    }
	    return "searchResultPlan";
	}


	
	@GetMapping("/searchByName")
    public String searchPlanByName(@RequestParam("planName") String planName, Model model) {
        try {
            ResponseEntity<Plan> response = restTemplate.getForEntity(planBasedUrl + "/getName/" + planName, Plan.class);
            model.addAttribute("plan", response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
        	Map<String, String> errors=null;
        	try {
        			errors = new ObjectMapper().readValue(e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
        		} catch (JsonProcessingException e1) {
        					e1.printStackTrace();
        			}
        	          model.addAttribute("error",errors);
	    }  
        return "searchResultPlan";
    }
	
	
	@GetMapping("/searchByFixedRate")
    public String searchPlanByName(@RequestParam("fixedRate") double fixedRate, Model model) {
        try {
            ResponseEntity<List> response = restTemplate.getForEntity(planBasedUrl + "/fixed-rate/"+fixedRate, List.class);
            List plans = response.getBody();
            model.addAttribute("listOfPlans", plans);
            model.addAttribute("searchCriteria", "Fixed Rate: " + fixedRate);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
        	Map<String, String> errors=null;
        	try {
        			errors = new ObjectMapper().readValue(e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
        		} catch (JsonProcessingException e1) {
        					e1.printStackTrace();
        			}
        	           model.addAttribute("error",errors);
	    }  
        return "viewAllPlans";
    }
	

	@GetMapping("/searchByPlanGroup")
    public String searchPlanByGroup(@RequestParam("planGroup") String planGroup, Model model) {
        try {
            ResponseEntity<List> response = restTemplate.getForEntity(planBasedUrl + "/plan-group/"+planGroup, List.class);
            List plans = response.getBody();
            model.addAttribute("listOfPlans", plans);
            model.addAttribute("searchCriteria", "Fixed Rate: " + planGroup);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
        	Map<String, String> errors=null;
        	try {
        			errors = new ObjectMapper().readValue(e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
        		} catch (JsonProcessingException e1) {
        					e1.printStackTrace();
        			}
        	           model.addAttribute("error",errors);
	    }  
        return "viewAllPlans";
    }
	
	@GetMapping("/searchByDataLimit")
    public String searchPlanByDataLimit(@RequestParam("dataLimit") String dataLimit, Model model) {
        try {
            ResponseEntity<List> response = restTemplate.getForEntity(planBasedUrl + "/data-limit/"+dataLimit, List.class);
            List plans = response.getBody();
            model.addAttribute("listOfPlans", plans);
            model.addAttribute("searchCriteria", "Fixed Rate: " + dataLimit);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
        	Map<String, String> errors=null;
        	try {
        			errors = new ObjectMapper().readValue(e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
        		} catch (JsonProcessingException e1) {
        					e1.printStackTrace();
        			}
        	           model.addAttribute("error",errors);
	    }  
        return "viewAllPlans";
    }
	
	
	@GetMapping("/searchByNumberOfDay")
    public String searchPlanByNumberOfDays(@RequestParam("numberOfDay") int numberOfDay, Model model) {
        try {
            ResponseEntity<List> response = restTemplate.getForEntity(planBasedUrl + "/days/"+numberOfDay, List.class);
            List plans = response.getBody();
            model.addAttribute("listOfPlans", plans);
            model.addAttribute("searchCriteria", "Fixed Rate: " + numberOfDay);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
        	Map<String, String> errors=null;
        	try {
        			errors = new ObjectMapper().readValue(e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
        		} catch (JsonProcessingException e1) {
        					e1.printStackTrace();
        			}
        	           model.addAttribute("error",errors);
	    }  
        return "viewAllPlans";
    }
	
	
	@GetMapping("/searchByStatus")
    public String searchPlanByStatus(@RequestParam("status") String status,Model model) {
        try {
            ResponseEntity<List> response = restTemplate.getForEntity(planBasedUrl + "/status/"+status, List.class);
            List plans = response.getBody();
            model.addAttribute("listOfPlans", plans);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
        	Map<String, String> errors=null;
        	try {
        			errors = new ObjectMapper().readValue(e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
        		} catch (JsonProcessingException e1) {
        					e1.printStackTrace();
        			}
        	           model.addAttribute("error",errors);
        }
        return "viewAllPlans";
    }
	
	
	@PostMapping("/addForm")
	public String From() {
		return "addPlan";
	}
	
//	@PostMapping("/addNewPlan")
//    public String addNewPlan(@ModelAttribute Plan plan, Model model) {
//        try {
//            ResponseEntity<Plan> response = restTemplate.postForEntity(planBasedUrl + "/add", plan, Plan.class);
//            model.addAttribute("message", "Plan added successfully! Plan ID: " + response.getBody().getPlanId());
//        } catch (HttpClientErrorException | HttpServerErrorException e) {
//        	Map<String, String> errors=null;
//        	try {
//        			errors = new ObjectMapper().readValue(e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
//        		} catch (JsonProcessingException e1) {
//        					e1.printStackTrace();
//        			}
//        	           model.addAttribute("error", "Failed to fetch plan: " +errors);
//	    } 
//        return "addPlan";
//    }
	
	
	
	@PostMapping("/addNewPlan")
    public String addNewPlan(@ModelAttribute Plan plan, Model model) {
        try {
            ResponseEntity<Plan> response = restTemplate.postForEntity(planBasedUrl + "/add", plan, Plan.class);
            model.addAttribute("message", "Plan added successfully! Plan ID: " + response.getBody().getPlanId());
        } catch (HttpClientErrorException |HttpServerErrorException e) {
        	Map<String, String> errors=null;
        	try {
        			errors = new ObjectMapper().readValue(e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
        		} catch (JsonProcessingException e1) {
        					e1.printStackTrace();
        		}
        	
        	           model.addAttribute("error", errors);
	    } 
        return "addPlan";
    }
	
	
	@PostMapping("/updateForm")
	public String updateYourPlan() {
		return "updatePlan";
	}
	
	
	@GetMapping("/fetchPlanById")
    public String fetchPlanById(@RequestParam("planId") int planId, Model model) {
        try {
            ResponseEntity<Plan> response = restTemplate.getForEntity(planBasedUrl + "/getId/" + planId, Plan.class);
            model.addAttribute("plan", response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
//        	catch (HttpClientErrorException |HttpServerErrorException e) {
        	
            	Map<String, String> errors=null;
            	try {
            			errors = new ObjectMapper().readValue(e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
            		} catch (JsonProcessingException e1) {
            					e1.printStackTrace();
            		}
            	           model.addAttribute("error", errors);
    	    } 
//        	Map<String, String> errors=null;
//        	try {
//        			errors = new ObjectMapper().readValue(e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
//        		} catch (JsonProcessingException e1) {
//        					e1.printStackTrace();
//        			}
//        	model.addAttribute("error", errors);
//	    } 
        return "updatePlan";
    }
	
	
	
	@PostMapping("/updatePresentPlan")
    public String updatePlan(@RequestParam int planId, @ModelAttribute Plan plan, Model model) {
        try {
            String url = planBasedUrl + "/updateById/" + planId;
            ResponseEntity<Plan> response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(plan), Plan.class);
            model.addAttribute("message", "Plan updated successfully!");
        } 
        catch (HttpClientErrorException | HttpServerErrorException e) {
        	Map<String, String> errors=null;
        	try {
        			errors = new ObjectMapper().readValue(e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
        		} catch (JsonProcessingException e1) {
        					e1.printStackTrace();
        			}
        	           model.addAttribute("error", errors);
        }
        return "updatePlan";
    }
	
	
	@PostMapping("/deletePlan")
    public String deletePlan(@RequestParam int planId, Model model) {
        try {
            String url = planBasedUrl + "/deleteById/" + planId;
            restTemplate.delete(url);
            model.addAttribute("message", "Plan deleted successfully!");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
        	Map<String, String> errors=null;
        	try {
        			errors = new ObjectMapper().readValue(e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
        		} catch (JsonProcessingException e1) {
        					e1.printStackTrace();
        			}
        	           model.addAttribute("error",errors);
	    } 
        return "deletePlan";
    }
		
}
