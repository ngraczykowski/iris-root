package com.silenteight.serp.governance;

import com.silenteight.sep.auth.authentication.AuthenticationModule;
import com.silenteight.sep.auth.authorization.AuthorizationModule;
import com.silenteight.sep.base.common.app.SerpApplicationContextCallback;
import com.silenteight.sep.base.common.app.SerpApplicationTemplate;
import com.silenteight.serp.governance.analytics.AnalyticsModule;
import com.silenteight.serp.governance.branch.BranchModule;
import com.silenteight.serp.governance.changerequest.ChangeRequestModule;
import com.silenteight.serp.governance.common.integration.AmqpCommonModule;
import com.silenteight.serp.governance.common.signature.SignatureModule;
import com.silenteight.serp.governance.common.web.WebModule;
import com.silenteight.serp.governance.mailer.MailerModule;
import com.silenteight.serp.governance.model.ModelModule;
import com.silenteight.serp.governance.notifier.NotifierModule;
import com.silenteight.serp.governance.policy.PolicyModule;
import com.silenteight.serp.governance.solutiondiscrepancy.SolutionDiscrepancyModule;
import com.silenteight.serp.governance.strategy.StrategyModule;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;

@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {
    // NOTE(ahaczewski): Keep list of modules alphabetically sorted within section.
    // Domain modules
    AnalyticsModule.class,
    BranchModule.class,
    ChangeRequestModule.class,
    ModelModule.class,
    NotifierModule.class,
    PolicyModule.class,
    SolutionDiscrepancyModule.class,
    StrategyModule.class,
    SignatureModule.class,
    // Interface modules
    AmqpCommonModule.class,
    AuthenticationModule.class,
    AuthorizationModule.class,
    MailerModule.class,
    WebModule.class,
})
@EnableIntegration
@EnableIntegrationManagement
public class GovernanceApplication {

  public static void main(String[] args) {
    new SerpApplicationTemplate("governance", args, GovernanceApplication.class)
        .profiles("database", "rabbitmq", "messaging")
        .contextCallback(new SerpApplicationContextCallback())
        .runAndExit();
  }
}
