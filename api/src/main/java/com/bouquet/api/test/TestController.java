package com.bouquet.api.test;

import com.bouquet.api.config.NoAuth;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @NoAuth
    @GetMapping
    public ResponseEntity<Object> responseTest() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("response", "success");
        return ResponseEntity.ok().body(result);
    }
}
