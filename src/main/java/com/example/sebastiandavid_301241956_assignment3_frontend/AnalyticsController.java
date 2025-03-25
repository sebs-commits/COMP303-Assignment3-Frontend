package com.example.sebastiandavid_301241956_assignment3_frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Controller
public class AnalyticsController {
    @Autowired
    private RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080/api";

    @GetMapping("/analytics")
    public String getAnalyticsDashboard(Model model) {
        try {
            // Fetch account types with high overdraft endpoint
            ResponseEntity<List<Map<String, Object>>> overdraftResponse = restTemplate.exchange(
                    baseUrl + "/account-types/overdraft",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            model.addAttribute("highOverdraftAccounts", overdraftResponse.getBody());

            // Fetch account types summary endpoint
            ResponseEntity<List<Map<String, Object>>> summaryResponse = restTemplate.exchange(
                    baseUrl + "/account-types/summary",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            model.addAttribute("accountTypesSummary", summaryResponse.getBody());

        } catch (Exception e) {
            model.addAttribute("error", "Failed to fetch analytics data: " + e.getMessage());
        }
        return "analytics-dashboard";
    }
}
