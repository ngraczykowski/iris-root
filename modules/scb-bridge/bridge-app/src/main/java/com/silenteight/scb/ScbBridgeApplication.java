package com.silenteight.scb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableRetry
public class ScbBridgeApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScbBridgeApplication.class, args);
  }
}
