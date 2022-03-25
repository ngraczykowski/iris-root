package com.silenteight.connector.ftcc.app;

import com.silenteight.commons.app.spring.ApplicationBuilderConfigurer;
import com.silenteight.commons.app.spring.ConfigurableApplicationBuilder;
import com.silenteight.commons.app.spring.DefaultSpringApplicationContextCallback;
import com.silenteight.commons.app.spring.SpringApplicationTemplate;
import com.silenteight.connector.ftcc.app.amqp.AmqpListenerModule;
import com.silenteight.connector.ftcc.app.grpc.GrpcModule;
import com.silenteight.connector.ftcc.callback.CallbackModule;
import com.silenteight.connector.ftcc.common.integration.AmqpCommonModule;
import com.silenteight.connector.ftcc.ingest.IngestModule;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@EnableAutoConfiguration
@EnableRetry
@EnableAsync
@ComponentScan(basePackageClasses = {
    // Callback module
    CallbackModule.class,
    // Domain modules
    IngestModule.class,
    // Interface modules
    AmqpCommonModule.class,
    GrpcModule.class,
    AmqpListenerModule.class
})
public class FtccApplication {

  public static void main(String[] args) {
    setUpSystemProperties();
    setUpSecuritySystemProperties();

    new SpringApplicationTemplate(args, FtccApplication.class)
        .contextCallback(new DefaultSpringApplicationContextCallback())
        .runAndExit(new Configurer());
  }

  private static void setUpSystemProperties() {
    setProperty("java.security.egd", "file:/dev/./urandom");
  }

  private static void setUpSecuritySystemProperties() {
    setSystemPropertyFromEnvironment("javax.net.ssl.trustStore", "TRUSTSTORE_PATH");
    setSystemPropertyFromEnvironment("javax.net.ssl.trustStorePassword", "TRUSTSTORE_PASSWORD");
    setSystemPropertyFromEnvironment("javax.net.ssl.trustStoreType", "TRUSTSTORE_TYPE");
    setSystemPropertyFromEnvironment("javax.net.ssl.keyStore", "KEYSTORE_PATH");
    setSystemPropertyFromEnvironment("javax.net.ssl.keyStorePassword", "KEYSTORE_PASSWORD");
    setSystemPropertyFromEnvironment("javax.net.ssl.keyStoreType", "KEYSTORE_TYPE");
  }

  private static void setSystemPropertyFromEnvironment(
      String property, String environmentVariable) {

    var environmentVariableValue = System.getenv(environmentVariable);

    if (getProperty(property) == null && isNotBlank(environmentVariableValue))
      System.setProperty(property, environmentVariableValue);
  }

  private static class Configurer implements ApplicationBuilderConfigurer {

    @Override
    public ConfigurableApplicationBuilder configure(ConfigurableApplicationBuilder builder) {
      return builder;
    }

    @Override
    public SpringApplicationBuilder customize(SpringApplicationBuilder springBuilder) {
      return springBuilder
          .bannerMode(Mode.OFF);
    }
  }
}
