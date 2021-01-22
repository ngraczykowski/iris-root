package com.silenteight.serp.governance;

import com.silenteight.sep.auth.authentication.AuthenticationModule;
import com.silenteight.sep.auth.authorization.AuthorizationModule;
import com.silenteight.sep.base.common.app.SerpApplicationContextCallback;
import com.silenteight.sep.base.common.app.SerpApplicationTemplate;
import com.silenteight.serp.governance.activation.ActivationModule;
import com.silenteight.serp.governance.app.amqp.RabbitModule;
import com.silenteight.serp.governance.app.grpc.GrpcModule;
import com.silenteight.serp.governance.app.rest.RestModule;
import com.silenteight.serp.governance.autoactivation.AutoActivationModule;
import com.silenteight.serp.governance.branch.BranchModule;
import com.silenteight.serp.governance.branchquery.BranchQueryModule;
import com.silenteight.serp.governance.branchsolution.BranchSolutionModule;
import com.silenteight.serp.governance.bulkchange.BulkChangeModule;
import com.silenteight.serp.governance.common.CommonModule;
import com.silenteight.serp.governance.common.web.config.WebModule;
import com.silenteight.serp.governance.decisiongroup.DecisionGroupModule;
import com.silenteight.serp.governance.decisiontree.DecisionTreeModule;
import com.silenteight.serp.governance.decisiontreesummaryquery.DecisionTreeSummaryQueryModule;
import com.silenteight.serp.governance.featuregroup.FeatureGroupModule;
import com.silenteight.serp.governance.featureset.FeatureSetModule;
import com.silenteight.serp.governance.featurevector.FeatureVectorModule;
import com.silenteight.serp.governance.mailer.MailerModule;
import com.silenteight.serp.governance.migrate.MigrationModule;
import com.silenteight.serp.governance.model.ModelModule;
import com.silenteight.serp.governance.notifier.NotifierModule;
import com.silenteight.serp.governance.policy.PolicyModule;
import com.silenteight.serp.governance.solutiondiscrepancy.SolutionDiscrepancyModule;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;

@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {
    // NOTE(ahaczewski): Keep list of modules alphabetically sorted within section.
    // Domain modules
    ActivationModule.class,
    AutoActivationModule.class,
    BranchModule.class,
    BranchQueryModule.class,
    BranchSolutionModule.class,
    BulkChangeModule.class,
    CommonModule.class,
    DecisionGroupModule.class,
    DecisionTreeModule.class,
    DecisionTreeSummaryQueryModule.class,
    FeatureGroupModule.class,
    FeatureSetModule.class,
    FeatureVectorModule.class,
    MigrationModule.class,
    ModelModule.class,
    NotifierModule.class,
    PolicyModule.class,
    SolutionDiscrepancyModule.class,
    // Interface modules
    AuthenticationModule.class,
    AuthorizationModule.class,
    GrpcModule.class,
    MailerModule.class,
    RabbitModule.class,
    RestModule.class,
    WebModule.class,
})
@EnableIntegration
@EnableIntegrationManagement
public class GovernanceApplication {

  public static void main(String[] args) {
    new SerpApplicationTemplate("governance", args, GovernanceApplication.class)
        .profiles("database", "rabbitmq","messaging")
        .contextCallback(new SerpApplicationContextCallback())
        .runAndExit();
  }
}
