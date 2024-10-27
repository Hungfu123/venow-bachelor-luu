package org.venow.eventorganizer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {
    @GetMapping("/")
    public String index() {
        return  "Hello World!";
    }
}
