package com.silenteight.sear;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HelloWorldController {

  @GetMapping("/hello")
  public String helloWorld() {
    return "Hello World!";
  }
}
