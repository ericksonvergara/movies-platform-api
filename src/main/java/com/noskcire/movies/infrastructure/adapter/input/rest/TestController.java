package com.noskcire.movies.infrastructure.adapter.input.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping
    public String test(){
        return "Endpoint protegisdo funcionando correctamente.";
    }
}
