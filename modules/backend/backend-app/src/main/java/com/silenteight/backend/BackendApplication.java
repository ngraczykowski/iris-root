/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.backend;

import com.silenteight.backend.frontend.FrontendConfigurationModule;
import com.silenteight.commons.app.spring.ApplicationBuilderConfigurer;
import com.silenteight.commons.app.spring.ConfigurableApplicationBuilder;
import com.silenteight.commons.app.spring.DefaultSpringApplicationContextCallback;
import com.silenteight.commons.app.spring.SpringApplicationTemplate;
import com.silenteight.sep.auth.authentication.AuthenticationModule;
import com.silenteight.sep.auth.authorization.AuthorizationModule;
import com.silenteight.sep.filestorage.minio.S3FileStorageMinioModule;
import com.silenteight.serp.governance.agent.AgentModule;
import com.silenteight.serp.governance.changerequest.ChangeRequestModule;
import com.silenteight.serp.governance.common.grpc.GrpcCommonModule;
import com.silenteight.serp.governance.common.integration.AmqpCommonModule;
import com.silenteight.serp.governance.common.signature.SignatureModule;
import com.silenteight.serp.governance.common.web.WebModule;
import com.silenteight.serp.governance.file.FileModule;
import com.silenteight.serp.governance.ingest.IngestModule;
import com.silenteight.serp.governance.model.ModelModule;
import com.silenteight.serp.governance.policy.PolicyModule;
import com.silenteight.serp.governance.qa.QaModule;
import com.silenteight.serp.governance.strategy.StrategyModule;
import com.silenteight.serp.governance.vector.FeatureVectorModule;
import com.silenteight.simulator.common.time.TimeModule;
import com.silenteight.simulator.dataset.DatasetModule;
import com.silenteight.simulator.management.ManagementModule;
import com.silenteight.simulator.processing.ProcessingModule;
import com.silenteight.simulator.retention.RetentionModule;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;

import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {
    // NOTE(ahaczewski): Keep list of modules alphabetically sorted within section.
    // Domain modules
    AgentModule.class,
    ChangeRequestModule.class,
    FileModule.class,
    FeatureVectorModule.class,
    IngestModule.class,
    ModelModule.class,
    PolicyModule.class,
    QaModule.class,
    StrategyModule.class,
    SignatureModule.class,
    // Interface modules
    AmqpCommonModule.class,
    AuthenticationModule.class,
    AuthorizationModule.class,
    FrontendConfigurationModule.class,
    S3FileStorageMinioModule.class,
    GrpcCommonModule.class,
    WebModule.class,
    // Simulator Domain modules
    DatasetModule.class,
    ManagementModule.class,
    com.silenteight.simulator.model.ModelModule.class,
    ProcessingModule.class,
    RetentionModule.class,
    // Interface modules
    com.silenteight.simulator.common.integration.AmqpCommonModule.class,
    TimeModule.class,
})
@EnableIntegration
@EnableIntegrationManagement
public class BackendApplication {

  public static void main(String[] args) {
    setUpSystemProperties();
    setUpSecuritySystemProperties();

    new SpringApplicationTemplate(args, BackendApplication.class)
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
          .bootstrapProperties("spring.application.name=backend");
    }

    @Override
    public SpringApplicationBuilder customize(SpringApplicationBuilder springBuilder) {
      return springBuilder
          .bannerMode(Mode.OFF);
    }
  }
}
