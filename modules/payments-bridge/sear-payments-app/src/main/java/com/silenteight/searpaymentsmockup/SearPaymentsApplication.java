package com.silenteight.searpaymentsmockup;

import com.silenteight.commons.app.spring.ApplicationBuilderConfigurer;
import com.silenteight.commons.app.spring.ConfigurableApplicationBuilder;
import com.silenteight.commons.app.spring.DefaultSpringApplicationContextCallback;
import com.silenteight.commons.app.spring.SpringApplicationTemplate;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import reactor.core.scheduler.Schedulers;

import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SpringBootApplication
public class SearPaymentsApplication {

  public static void main(String[] args) {
    setUpSystemProperties();
    setUpSecuritySystemProperties();

    Schedulers.enableMetrics();
    new SpringApplicationTemplate(args, SearPaymentsApplication.class)
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

  private static void setPropertyFromEnvironment(String property, String environmentVariable) {
    var environmentVariableValue = System.getenv(environmentVariable);

    if (getProperty(property) == null && isNotBlank(environmentVariableValue))
      setProperty(property, environmentVariableValue);
  }


  private static class Configurer implements ApplicationBuilderConfigurer {

    @Override
    public ConfigurableApplicationBuilder configure(ConfigurableApplicationBuilder builder) {
      return builder
          .bootstrapProperties("spring.application.name=ae");
    }

    @Override
    public SpringApplicationBuilder customize(SpringApplicationBuilder springBuilder) {
      return springBuilder
          .bannerMode(Mode.OFF)
          .web(WebApplicationType.REACTIVE);
    }
  }
}
