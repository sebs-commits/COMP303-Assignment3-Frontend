package com.example.sebastiandavid_301241956_assignment3_frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegistrationController {

    @Autowired
    RestTemplate restTemplate;

    private final String baseUrl = "http://localhost:8080/";

    @RequestMapping("/register-page")
    public String registerPage(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }

    @RequestMapping(value = "/register-process", method = RequestMethod.POST)
    public ModelAndView registerProcess(@ModelAttribute Customer customer,
                                        BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView();

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            mv.setViewName("register");
            return mv;
        }

        try {
            HttpEntity<Customer> request = new HttpEntity<>(customer);
            ResponseEntity<Customer> response = restTemplate.postForEntity(
                    baseUrl + "/api/register",
                    request,
                    Customer.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED) {
                mv.setViewName("login");
                mv.addObject("message", "Registration successful! Please login.");
            } else {
                mv.setViewName("register");
                mv.addObject("error", "Registration failed!");
            }
        } catch (HttpClientErrorException e) {
            // Handle specific error messages from the API
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                mv.setViewName("register");
                mv.addObject("error", e.getResponseBodyAsString());
            } else {
                mv.setViewName("register");
                mv.addObject("error", "Registration failed: " + e.getMessage());
            }
        } catch (Exception e) {
            mv.setViewName("register");
            mv.addObject("error", "Registration failed: " + e.getMessage());
        }

        return mv;
    }
}
