package com.silenteight.sens.webapp.backend;

import com.silenteight.sens.webapp.audit.AuditModule;
import com.silenteight.sens.webapp.backend.application.logging.ApplicationLoggingModule;
import com.silenteight.sens.webapp.backend.bulkchange.BulkChangeModule;
import com.silenteight.sens.webapp.backend.changerequest.ChangeRequestModule;
import com.silenteight.sens.webapp.backend.config.WebModule;
import com.silenteight.sens.webapp.backend.reasoningbranch.ReasoningBranchModule;
import com.silenteight.sens.webapp.backend.user.rest.UserRestModule;
import com.silenteight.sens.webapp.grpc.GrpcModule;
import com.silenteight.sens.webapp.keycloak.KeycloakModule;
import com.silenteight.sens.webapp.report.ReportModule;
import com.silenteight.sens.webapp.user.UserModule;
import com.silenteight.sep.base.common.app.SerpApplicationTemplate;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;

@SpringBootApplication(
    scanBasePackageClasses = {
        ApplicationLoggingModule.class,
        AuditModule.class,
        BulkChangeModule.class,
        ChangeRequestModule.class,
        GrpcModule.class,
        ChangeRequestModule.class,
        GrpcModule.class,
        KeycloakModule.class,
        ReasoningBranchModule.class,
        ReportModule.class,
        UserModule.class,
        UserRestModule.class,
        WebModule.class
    },
    exclude = {
        FreeMarkerAutoConfiguration.class
    })
public class WebApplication {

  public static void main(String[] args) {
    new SerpApplicationTemplate("webapp", args, WebApplication.class).run();
  }
}
