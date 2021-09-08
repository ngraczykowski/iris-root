package com.silenteight.rabbitcommonschema.initializr.app;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class RabbitCommonSchemaInitializrApplication {

  @Bean
  ApplicationRunner rabbitConnectionInitializer(ConnectionFactory factory) {
    return args -> factory.createConnection().close();
  }

  public static void main(String[] args) {
    log.info("Starting RabbitCommonSchemaInitializrApplication");
    try (var app = SpringApplication.run(RabbitCommonSchemaInitializrApplication.class, args)) {
      log.info("Closing application after context has started: isRunning={}", app.isRunning());
    }
  }
}
