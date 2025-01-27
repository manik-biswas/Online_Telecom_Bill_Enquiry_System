package com.otbs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConfigurationProperties(prefix = "backend.api")
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    
        private String customerBaseUrl;
        private String adminBaseUrl;
        private String billBaseUrl;
        private String complaintBaseUrl;
        private String planBasedUrl;
        private String helpdeskexecutiveBaseUrl;

        // Getters and Setters
        
        public String getPlanBasedUrl() {
			return planBasedUrl;
		}

		public void setPlanBasedUrl(String planBasedUrl) {
			this.planBasedUrl = planBasedUrl;
		}
        
        public String getCustomerBaseUrl() {
            return customerBaseUrl;
        }

		public void setCustomerBaseUrl(String customerBaseUrl) {
            this.customerBaseUrl = customerBaseUrl;
        }

        public String getAdminBaseUrl() {
            return adminBaseUrl;
        }

        public void setAdminBaseUrl(String adminBaseUrl) {
            this.adminBaseUrl = adminBaseUrl;
        }

    public String getBillBaseUrl() {
        return billBaseUrl;
    }

    public void setBillBaseUrl(String billBaseUrl) {
        this.billBaseUrl = billBaseUrl;
    }

	public String getComplaintBaseUrl() {
		return complaintBaseUrl;
	}

	public void setComplaintBaseUrl(String complaintBaseUrl) {
		this.complaintBaseUrl = complaintBaseUrl;
	}

	public String getHelpdeskexecutiveBaseUrl() {
		return helpdeskexecutiveBaseUrl;
	}

	public void setHelpdeskexecutiveBaseUrl(String helpdeskexecutiveBaseUrl) {
		this.helpdeskexecutiveBaseUrl = helpdeskexecutiveBaseUrl;
	}
	
	
    
}

