package com.ljk.example.company.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xkey  2020/1/6 15:55
 */
@RestController
public class TestController {


    @GetMapping()
    public ResponseEntity index() {
        return ResponseEntity.ok("hello company!!!!");
    }
}
