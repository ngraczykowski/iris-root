package com.silenteight.sens.webapp.backend;

import com.silenteight.commons.app.spring.ApplicationBuilderConfigurer;
import com.silenteight.commons.app.spring.ConfigurableApplicationBuilder;
import com.silenteight.commons.app.spring.DefaultSpringApplicationContextCallback;
import com.silenteight.commons.app.spring.SpringApplicationTemplate;
import com.silenteight.sens.webapp.audit.AuditModule;
import com.silenteight.sens.webapp.backend.application.logging.ApplicationLoggingModule;
import com.silenteight.sens.webapp.backend.config.WebModule;
import com.silenteight.sens.webapp.backend.configuration.ConfigurationModule;
import com.silenteight.sens.webapp.backend.external.apps.ExternalAppsModule;
import com.silenteight.sens.webapp.backend.report.domain.ReportDomainModule;
import com.silenteight.sens.webapp.backend.user.rest.UserRestModule;
import com.silenteight.sens.webapp.grpc.GrpcModule;
import com.silenteight.sens.webapp.notification.NotificationModule;
import com.silenteight.sens.webapp.report.ReportModule;
import com.silenteight.sens.webapp.user.UserModule;
import com.silenteight.sep.auth.authentication.AuthenticationModule;
import com.silenteight.sep.auth.authorization.AuthorizationModule;
import com.silenteight.sep.usermanagement.keycloak.UsermanagementKeycloakModule;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SpringBootApplication(
    scanBasePackageClasses = {
        ApplicationLoggingModule.class,
        AuditModule.class,
        AuthenticationModule.class,
        AuthorizationModule.class,
        ConfigurationModule.class,
        ExternalAppsModule.class,
        GrpcModule.class,
        UsermanagementKeycloakModule.class,
        NotificationModule.class,
        ReportModule.class,
        ReportDomainModule.class,
        UserModule.class,
        UserRestModule.class,
        WebModule.class
    },
    exclude = {
        FreeMarkerAutoConfiguration.class
    })
public class WebApplication {

  public static void main(String[] args) {
    setUpSystemProperties();
    setUpSecuritySystemProperties();

    new SpringApplicationTemplate(args, WebApplication.class)
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
      return builder
          .bootstrapProperties("spring.application.name=webapp");
    }

    @Override
    public SpringApplicationBuilder customize(SpringApplicationBuilder springBuilder) {
      return springBuilder
          .bannerMode(Mode.OFF);
    }
  }
}
