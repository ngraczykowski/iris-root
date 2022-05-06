package com.silenteight.adjudication.engine.app.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

  @GetMapping("/status")
  public ResponseEntity<String> getStatus() {
    return ResponseEntity.ok().body("ok");
  }
}
