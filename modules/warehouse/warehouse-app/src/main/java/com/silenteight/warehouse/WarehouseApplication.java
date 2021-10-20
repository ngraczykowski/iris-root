package com.silenteight.warehouse;

import com.silenteight.commons.app.spring.ApplicationBuilderConfigurer;
import com.silenteight.commons.app.spring.ConfigurableApplicationBuilder;
import com.silenteight.commons.app.spring.DefaultSpringApplicationContextCallback;
import com.silenteight.commons.app.spring.SpringApplicationTemplate;
import com.silenteight.sep.auth.authentication.AuthenticationModule;
import com.silenteight.sep.auth.authorization.AuthorizationModule;
import com.silenteight.sep.auth.token.TokenModule;
import com.silenteight.sep.filestorage.minio.FileStorageMinioModule;
import com.silenteight.warehouse.backup.BackupModule;
import com.silenteight.warehouse.common.elastic.ElasticsearchRestClientModule;
import com.silenteight.warehouse.common.environment.EnvironmentModule;
import com.silenteight.warehouse.common.integration.AmqpCommonModule;
import com.silenteight.warehouse.common.opendistro.OpendistroModule;
import com.silenteight.warehouse.common.time.TimeModule;
import com.silenteight.warehouse.common.web.WebModule;
import com.silenteight.warehouse.indexer.alert.AlertModule;
import com.silenteight.warehouse.indexer.production.ProductionMessageHandlerModule;
import com.silenteight.warehouse.indexer.production.indextracking.IndexTrackingModule;
import com.silenteight.warehouse.indexer.query.QueryAlertModule;
import com.silenteight.warehouse.indexer.simulation.SimulationMessageHandlerModule;
import com.silenteight.warehouse.indexer.simulation.analysis.AnalysisModule;
import com.silenteight.warehouse.management.ManagementModule;
import com.silenteight.warehouse.report.accuracy.AccuracyReportModule;
import com.silenteight.warehouse.report.billing.BillingReportModule;
import com.silenteight.warehouse.report.billing.v1.DeprecatedBillingReportModule;
import com.silenteight.warehouse.report.metrics.MetricsReportModule;
import com.silenteight.warehouse.report.metrics.v1.DeprecatedMetricsReportModule;
import com.silenteight.warehouse.report.rbs.RbsReportModule;
import com.silenteight.warehouse.report.reasoning.AiReasoningReportModule;
import com.silenteight.warehouse.report.remove.ReportsRemovalModule;
import com.silenteight.warehouse.report.reporting.ReportingModule;
import com.silenteight.warehouse.report.simulation.SimulationModule;
import com.silenteight.warehouse.report.simulation.v1.DeprecatedSimulationModule;
import com.silenteight.warehouse.report.statistics.ReportStatisticsModule;
import com.silenteight.warehouse.report.storage.StorageModule;
import com.silenteight.warehouse.sampling.SamplingModule;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.scheduling.annotation.EnableScheduling;

import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {
    // NOTE(ahaczewski): Keep list of modules alphabetically sorted within section.
    // Domain modules
    AccuracyReportModule.class,
    AiReasoningReportModule.class,
    AlertModule.class,
    AnalysisModule.class,
    BillingReportModule.class,
    BackupModule.class,
    DeprecatedBillingReportModule.class,
    DeprecatedMetricsReportModule.class,
    IndexTrackingModule.class,
    ManagementModule.class,
    MetricsReportModule.class,
    ProductionMessageHandlerModule.class,
    RbsReportModule.class,
    ReportStatisticsModule.class,
    QueryAlertModule.class,
    SamplingModule.class,
    SimulationMessageHandlerModule.class,
    // Interface modules
    AmqpCommonModule.class,
    AuthenticationModule.class,
    AuthorizationModule.class,
    DeprecatedSimulationModule.class,
    ElasticsearchRestClientModule.class,
    EnvironmentModule.class,
    FileStorageMinioModule.class,
    OpendistroModule.class,
    ReportingModule.class,
    ReportsRemovalModule.class,
    SimulationModule.class,
    StorageModule.class,
    TokenModule.class,
    TimeModule.class,
    WebModule.class,
})
@EnableIntegration
@EnableIntegrationManagement
@EnableScheduling
public class WarehouseApplication {

  public static void main(String[] args) {
    setUpSystemProperties();
    setUpSecuritySystemProperties();

    new SpringApplicationTemplate(args, WarehouseApplication.class)
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
          .bootstrapProperties("spring.application.name=warehouse");
    }

    @Override
    public SpringApplicationBuilder customize(SpringApplicationBuilder springBuilder) {
      return springBuilder
          .bannerMode(Mode.OFF);
    }
  }
}
