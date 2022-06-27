/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.configserver;

import com.silenteight.configserver.dynamic.IrisDynamicPropertiesGenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.cloud.config.server.environment.PassthruEnvironmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.Map;

@EnableConfigServer
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ConfigServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConfigServerApplication.class, args);
  }

  @Bean
  public EnvironmentRepository irisDynamicEnvironmentRepository(
      Map<String, IrisDynamicPropertiesGenerator> irisDynamicPropertiesGenerators) {
    return (application, profile, label) -> {
      var env = new StandardEnvironment();
      irisDynamicPropertiesGenerators.entrySet().stream()
          .map(
              e ->
                  new MapPropertySource(
                      e.getKey(), e.getValue().generate(application, profile, label)))
          .forEach(x -> env.getPropertySources().addLast(x));
      return new PassthruEnvironmentRepository(env).findOne(application, profile, label);
    };
  }
}
