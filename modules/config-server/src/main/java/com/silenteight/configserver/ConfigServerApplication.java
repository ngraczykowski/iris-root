/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Bean;

@EnableConfigServer
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ConfigServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConfigServerApplication.class, args);
  }

  @Bean
  public CustomEnvironmentRepository customEnvironmentRepository() {
    return new CustomEnvironmentRepository();
  }
}
