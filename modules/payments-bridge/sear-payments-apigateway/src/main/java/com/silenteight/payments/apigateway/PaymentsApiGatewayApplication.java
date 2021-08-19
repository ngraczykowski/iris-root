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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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

  private static void setPropertyFromEnvironment(String property, String environmentVariable) {
    // XXX(ahaczewski): Refactor that a little.
    // Do not override system properties with environment variables.
    if (getProperty(property) == null) {
      // Read the value from a file pointed by the `<env_var>_FILE` environment variable.
      var environmentVariableFileValue = System.getenv(environmentVariable + "_FILE");
      if (isNotBlank(environmentVariableFileValue)) {
        var variableFilePath = Paths.get(environmentVariableFileValue);
        String variableValue = null;
        try {
          variableValue = Files.readString(variableFilePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
          log.warn(
              "Failed to read variable value from file: variable={}, file={}, error={}, message={}",
              environmentVariable, variableFilePath, e.getClass().getName(), e.getMessage());
        }

        if (isNotBlank(variableValue)) {
          setProperty(property, variableValue);
          return;
        }
      }

      // Read the value directly from the environment variable.
      var environmentVariableValue = System.getenv(environmentVariable);
      if (isNotBlank(environmentVariableValue))
        setProperty(property, environmentVariableValue);
    }
  }
}
