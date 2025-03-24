package com.example.sebastiandavid_301241956_assignment3_frontend;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
    @GetMapping("/customer/{id}")
    public String getCustomerDetails(@PathVariable int id, Model model) {
        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    baseUrl + "/customers/" + id,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            model.addAttribute("accounts", response.getBody());
            model.addAttribute("customerId", id);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load customer data");
            model.addAttribute("accounts", new ArrayList<>());
        }

        return "customer-details";
    }

    @PostMapping("/customer/{customerId}/update-account")
    public String updateAccount(@PathVariable int customerId,
                                @RequestParam int accountNumber,
                                @RequestParam BigDecimal balance,
                                Model model) {
        try {
            // Create the request body
            Map<String, Object> accountDetails = new HashMap<>();
            accountDetails.put("accountNumber", accountNumber);
            accountDetails.put("balance", balance);

            // Make PUT request to backend
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(accountDetails);
            ResponseEntity<Map> response = restTemplate.exchange(
                    baseUrl + "/customers/" + customerId + "/accounts",
                    HttpMethod.PUT,
                    requestEntity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("success", "Account updated");
            } else {
                model.addAttribute("error", "Failed to update");
            }

            return getCustomerDetails(customerId, model);
        } catch (Exception e) {
            model.addAttribute("error", "Could not update the account " + e.getMessage());
            return getCustomerDetails(customerId, model);
        }
    }
    @PostMapping("/customer/{id}/update-details")
    public String updateCustomerDetails(
            @PathVariable int id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String postalCode,
            @RequestParam(required = false) String city,
            Model model) {
        try {
            Map<String, Object> customerDetails = new HashMap<>();

            if (username != null && !username.trim().isEmpty()) {
                customerDetails.put("username", username);
            }
            if (customerName != null && !customerName.trim().isEmpty()) {
                customerDetails.put("customerName", customerName);
            }
            if (password != null && !password.trim().isEmpty()) {
                customerDetails.put("password", password);
            }
            if (address != null && !address.trim().isEmpty()) {
                customerDetails.put("address", address);
            }
            if (postalCode != null && !postalCode.trim().isEmpty()) {
                customerDetails.put("postalCode", postalCode);
            }
            if (city != null && !city.trim().isEmpty()) {
                customerDetails.put("city", city);
            }

            // Make PUT request to backend
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(customerDetails);
            ResponseEntity<Map> response = restTemplate.exchange(
                    baseUrl + "/customers/" + id,
                    HttpMethod.PUT,
                    requestEntity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("customerUpdateSuccess", "Updated!");
            } else {
                model.addAttribute("customerUpdateError", "Failed to update information");
            }
        } catch (Exception e) {
            model.addAttribute("customerUpdateError", "Error updating customer details: " + e.getMessage());
        }

        // Return to the same page
        return getCustomerDetails(id, model);
    }


}
