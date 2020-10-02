package com.silenteight.sens.webapp.backend;

import com.silenteight.sens.webapp.audit.AuditModule;
import com.silenteight.sens.webapp.backend.application.logging.ApplicationLoggingModule;
import com.silenteight.sens.webapp.backend.bulkchange.BulkChangeModule;
import com.silenteight.sens.webapp.backend.changerequest.ChangeRequestModule;
import com.silenteight.sens.webapp.backend.circuitbreaker.CircuitBreakerModule;
import com.silenteight.sens.webapp.backend.config.WebModule;
import com.silenteight.sens.webapp.backend.configuration.ConfigurationModule;
import com.silenteight.sens.webapp.backend.decisiontree.DecisionTreeModule;
import com.silenteight.sens.webapp.backend.external.apps.ExternalAppsModule;
import com.silenteight.sens.webapp.backend.reasoningbranch.ReasoningBranchModule;
import com.silenteight.sens.webapp.backend.report.domain.ReportDomainModule;
import com.silenteight.sens.webapp.backend.user.rest.UserRestModule;
import com.silenteight.sens.webapp.common.app.SensWebAppApplicationTemplate;
import com.silenteight.sens.webapp.grpc.GrpcModule;
import com.silenteight.sens.webapp.notification.NotificationModule;
import com.silenteight.sens.webapp.report.ReportModule;
import com.silenteight.sens.webapp.user.UserModule;
import com.silenteight.sep.auth.authentication.AuthenticationModule;
import com.silenteight.sep.auth.authorization.AuthorizationModule;
import com.silenteight.sep.usermanagement.keycloak.UsermanagementKeycloakModule;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;

@SpringBootApplication(
    scanBasePackageClasses = {
        ApplicationLoggingModule.class,
        AuditModule.class,
        AuthenticationModule.class,
        AuthorizationModule.class,
        BulkChangeModule.class,
        ChangeRequestModule.class,
        CircuitBreakerModule.class,
        ConfigurationModule.class,
        DecisionTreeModule.class,
        ExternalAppsModule.class,
        GrpcModule.class,
        UsermanagementKeycloakModule.class,
        NotificationModule.class,
        ReasoningBranchModule.class,
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
    new SensWebAppApplicationTemplate("webapp", args, WebApplication.class).run();
  }
}
