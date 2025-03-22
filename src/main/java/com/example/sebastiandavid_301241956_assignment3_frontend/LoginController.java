package com.example.sebastiandavid_301241956_assignment3_frontend;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    RestTemplate restTemplate;

    private final String baseUrl = "http://localhost:8080/";

    @RequestMapping("/login-page")
    public String loginPage(Model model) {
        model.addAttribute("customer", new Customer());
        return "login";
    }

    @RequestMapping(value = "/login-process", method = RequestMethod.POST)
    public ModelAndView loginProcess(@ModelAttribute Customer customer, HttpSession session) {
        ModelAndView mv = new ModelAndView();

        try {
            HttpEntity<Customer> request = new HttpEntity<>(customer);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    baseUrl + "/api/login",
                    request,
                    Map.class
            );

            // If login successful, store information in session
            if (response.getStatusCode() == HttpStatus.OK) {
                session.setAttribute("customerId", customer.getCustomerId());
                session.setAttribute("username", customer.getUsername());
                mv.setViewName("dashboard");
                mv.addObject("message", "Login successful!");
            } else {
                mv.setViewName("login");
                mv.addObject("error", "Login failed!");
            }
        } catch (Exception e) {
            mv.setViewName("login");
            mv.addObject("error", "Login failed: " + e.getMessage());
        }

        return mv;
    }

    @PostMapping("/logout-process")
    public String logoutProcess(HttpSession session) {
        try {
            restTemplate.postForObject(baseUrl + "/api/logout", null, String.class);
            session.invalidate();
            return "redirect:/login-page?logout=success";
        } catch (Exception e) {
            return "redirect:/login-page?logout=error";
        }
    }
}
