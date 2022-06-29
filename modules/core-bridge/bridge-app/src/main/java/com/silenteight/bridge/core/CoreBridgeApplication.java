package com.silenteight.bridge.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@EnableJdbcAuditing
@SpringBootApplication
public class CoreBridgeApplication {

  public static void main(String[] args) {
    SpringApplication.run(CoreBridgeApplication.class, args);
  }

}
