package com.healthcaremanagement.server_health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/server_health")
    public String health() {
        System.out.println("this is health check");
        return "Server health is good";
    }

}
