package com.silenteight.payments.bridge.app.controller;


import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HealthCheckController {

  @GetMapping("/status")
  public ResponseEntity<Object> getStatus() {
    return ResponseEntity.ok().build();
  }
}
