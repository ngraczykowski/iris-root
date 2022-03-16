package com.silenteight.scb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ScbBridgeApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScbBridgeApplication.class, args);
  }
}
