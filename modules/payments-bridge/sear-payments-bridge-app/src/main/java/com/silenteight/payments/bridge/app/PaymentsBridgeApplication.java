package com.silenteight.payments.bridge.app;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.commons.app.spring.ApplicationBuilderConfigurer;
import com.silenteight.commons.app.spring.ConfigurableApplicationBuilder;
import com.silenteight.commons.app.spring.DefaultSpringApplicationContextCallback;
import com.silenteight.commons.app.spring.SpringApplicationTemplate;
import com.silenteight.payments.bridge.PaymentsBridgeModule;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.http.config.EnableIntegrationGraphController;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import reactor.core.scheduler.Schedulers;

import static com.silenteight.payments.common.app.EnvironmentUtils.setPropertyFromEnvironment;
import static java.lang.System.setProperty;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = PaymentsBridgeModule.class)
@EnableIntegration
@EnableIntegrationGraphController
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@EnableScheduling
@EntityScan(basePackageClasses = PaymentsBridgeModule.class)
@IntegrationComponentScan(basePackageClasses = PaymentsBridgeModule.class)
@Slf4j
public class PaymentsBridgeApplication {

  public static void main(String[] args) {
    setUpSystemProperties();
    setUpSecuritySystemProperties();

    Schedulers.enableMetrics();
    new SpringApplicationTemplate(args, PaymentsBridgeApplication.class)
        .contextCallback(new DefaultSpringApplicationContextCallback())
        .runAndExit(new Configurer());
  }

  private static void setUpSystemProperties() {
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

  private static class Configurer implements ApplicationBuilderConfigurer {

    @Override
    public ConfigurableApplicationBuilder configure(ConfigurableApplicationBuilder builder) {
      return builder
          .bootstrapProperties("spring.application.name=pb");
    }

    @Override
    public SpringApplicationBuilder customize(SpringApplicationBuilder springBuilder) {
      return springBuilder
          .bannerMode(Mode.OFF)
          .web(WebApplicationType.SERVLET);
    }
  }
}
