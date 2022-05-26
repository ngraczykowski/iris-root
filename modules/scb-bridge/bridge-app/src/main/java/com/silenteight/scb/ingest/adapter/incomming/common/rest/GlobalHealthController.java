package com.silenteight.scb.ingest.adapter.incomming.common.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/info")
@AllArgsConstructor
@Slf4j
// remove this once GlobalHealthEndpoint actuator endpoint is exposed
public class GlobalHealthController {

  private final GlobalHealthEndpoint globalHealthEndpoint;

  @GetMapping(value = "global", produces = APPLICATION_JSON_VALUE)
  public Map<String, List<Map<String, String>>> health() {
    return globalHealthEndpoint.health();
  }

}
