package com.example.sebastiandavid_301241956_assignment3_frontend;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ManagerController {
    @Autowired
    private RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080/api";

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        try {
            // Call /customers endpoint to retrieve customer with account data
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    baseUrl + "/customers",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            List<Map<String, Object>> customers = response.getBody();
            model.addAttribute("customers", customers != null ? customers : new ArrayList<>());

            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to fetch customers: " + e.getMessage());
            return "dashboard";
        }
    }
}
