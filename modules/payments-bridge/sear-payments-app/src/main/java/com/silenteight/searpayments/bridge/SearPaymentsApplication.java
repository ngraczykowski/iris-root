package com.silenteight.searpayments.bridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.silenteight.searpayments")
public class SearPaymentsApplication {

  public static void main(String[] args) {
    SpringApplication.run(SearPaymentsApplication.class, args);
  }
}
