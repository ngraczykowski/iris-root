package com.silenteight.payments.bridge.app.controller;


import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestAuthenticationController {

  @GetMapping("/test-authentication")
  public String getTest() {
    return "Authenticated\n";
  }
}
