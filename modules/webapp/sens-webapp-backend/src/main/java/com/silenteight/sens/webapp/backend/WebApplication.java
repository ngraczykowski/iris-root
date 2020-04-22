package com.silenteight.sens.webapp.backend;

import com.silenteight.sens.webapp.audit.slf4j.Slf4jBasedAuditLogModule;
import com.silenteight.sens.webapp.backend.application.logging.ApplicationLoggingModule;
import com.silenteight.sens.webapp.backend.changerequest.ChangeRequestModule;
import com.silenteight.sens.webapp.backend.chromeextension.ChromeExtensionModule;
import com.silenteight.sens.webapp.backend.config.WebModule;
import com.silenteight.sens.webapp.backend.reasoningbranch.ReasoningBranchModule;
import com.silenteight.sens.webapp.backend.report.config.ReportConfigModule;
import com.silenteight.sens.webapp.backend.report.rest.ReportRestModule;
import com.silenteight.sens.webapp.backend.user.rest.UserRestModule;
import com.silenteight.sens.webapp.common.app.SensWebAppApplicationTemplate;
import com.silenteight.sens.webapp.grpc.GrpcModule;
import com.silenteight.sens.webapp.keycloak.KeycloakModule;
import com.silenteight.sens.webapp.user.UserModule;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;

@SpringBootApplication(
    scanBasePackageClasses = {
        ApplicationLoggingModule.class,
        ChangeRequestModule.class,
        ChromeExtensionModule.class,
        GrpcModule.class,
        KeycloakModule.class,
        ReasoningBranchModule.class,
        ReportConfigModule.class,
        ReportRestModule.class,
        Slf4jBasedAuditLogModule.class,
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
