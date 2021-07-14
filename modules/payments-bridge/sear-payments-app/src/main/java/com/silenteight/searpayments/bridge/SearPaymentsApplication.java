package com.silenteight.searpayments.bridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import static java.lang.System.getProperty;

@SpringBootApplication
@ComponentScan("com.silenteight.searpayments.scb.etl")
public class SearPaymentsApplication {

  public static void main(String[] args) {
    SpringApplication.run(SearPaymentsApplication.class, args);
  }
}
