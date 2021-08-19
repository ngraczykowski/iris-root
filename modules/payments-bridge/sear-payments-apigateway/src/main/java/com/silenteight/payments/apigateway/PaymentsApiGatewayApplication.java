package com.silenteight.payments.apigateway;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactor.core.scheduler.Schedulers;

import static com.silenteight.payments.common.app.EnvironmentUtils.setPropertyFromEnvironment;
import static java.lang.System.setProperty;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = PaymentsApiGatewayApplication.class)
@EnableScheduling
@Slf4j
public class PaymentsApiGatewayApplication {

  public static void main(String[] args) {
    setUpSystemProperties();
    setUpSecuritySystemProperties();

    Schedulers.enableMetrics();

    //noinspection resource
    new SpringApplicationBuilder(PaymentsApiGatewayApplication.class)
        .bannerMode(Mode.OFF)
        .web(WebApplicationType.REACTIVE)
        .run(args);
  }

  private static void setUpSystemProperties() {
    setProperty("spring.application.name", "papigw");

    // NOTE(ahaczewski): Force use of fast random source.
    setProperty("java.security.egd", "file:/dev/urandom");
  }

  private static void setUpSecuritySystemProperties() {
    setPropertyFromEnvironment("javax.net.ssl.trustStore", "TRUSTSTORE_PATH");
    setPropertyFromEnvironment("javax.net.ssl.trustStorePassword", "TRUSTSTORE_PASSWORD");
    setPropertyFromEnvironment("javax.net.ssl.trustStoreType", "TRUSTSTORE_TYPE");
    setPropertyFromEnvironment("javax.net.ssl.keyStore", "KEYSTORE_PATH");
    setPropertyFromEnvironment("javax.net.ssl.keyStorePassword", "KEYSTORE_PASSWORD");
    setPropertyFromEnvironment("javax.net.ssl.keyStoreType", "KEYSTORE_TYPE");
  }
}
