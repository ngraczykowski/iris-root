package com.silenteight.adjudication.app;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.CloudApiModule;
import com.silenteight.commons.app.spring.ApplicationBuilderConfigurer;
import com.silenteight.commons.app.spring.ConfigurableApplicationBuilder;
import com.silenteight.commons.app.spring.DefaultSpringApplicationContextCallback;
import com.silenteight.commons.app.spring.SpringApplicationTemplate;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import reactor.core.scheduler.Schedulers;

import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = CloudApiModule.class)
@EnableIntegration
@EnableIntegrationManagement
@EnableTransactionManagement
@IntegrationComponentScan(basePackageClasses = CloudApiModule.class)
@Slf4j
class AdjudicationEngineApplication {

  public static void main(String[] args) {
    setUpSystemProperties();
    setUpSecuritySystemProperties();

    Schedulers.enableMetrics();
    new SpringApplicationTemplate(args, AdjudicationEngineApplication.class)
        .contextCallback(new DefaultSpringApplicationContextCallback())
        .runAndExit(new Configurer());
  }

  private static void setUpSystemProperties() {
    // NOTE(ahaczewski): Force use of fast random source.
    setProperty("java.security.egd", "file:/dev/./urandom");
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
    var environmentVariableValue = System.getenv(environmentVariable);

    if (getProperty(property) == null && isNotBlank(environmentVariableValue))
      setProperty(property, environmentVariableValue);
  }

  private static class Configurer implements ApplicationBuilderConfigurer {

    @Override
    public ConfigurableApplicationBuilder configure(ConfigurableApplicationBuilder builder) {
      return builder
          .bootstrapProperties("spring.application.name=cloudapi");
    }

    @Override
    public SpringApplicationBuilder customize(SpringApplicationBuilder springBuilder) {
      return springBuilder
          .bannerMode(Mode.OFF)
          .web(WebApplicationType.REACTIVE);
    }
  }
}
