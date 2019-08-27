package com.epam.jpop.userservice.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class HealthCheckController {

    @GetMapping("/")
    @ApiOperation(value = "Health check API for user-service")
    public String health(){
        return "Hello World!";
    }
}
