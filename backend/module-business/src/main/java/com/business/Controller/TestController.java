package com.business.Controller;

import com.core.config.ApplicationConfig;
import com.core.model.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private ApplicationConfig.MessageSourceVi messageSourceVi;

    @GetMapping("/")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(ResponseBody.builder().message(messageSourceVi.getMessageVi("OK002"))
                .body("Hello World")
                .build());
    }
}

