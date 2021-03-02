package com.silenteight.adjudication.app.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

  @GetMapping("/status")
  public ResponseEntity<Void> getStatus() {
    return ResponseEntity.ok().build();
  }
}
