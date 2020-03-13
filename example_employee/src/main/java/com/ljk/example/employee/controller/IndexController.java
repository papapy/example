package com.ljk.example.employee.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author xkey  2020/1/6 16:07
 */
@RestController
public class IndexController {

    @Resource
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity index() {
        return ResponseEntity.ok(restTemplate.getForEntity("http://company/", String.class).getBody());
    }
}
