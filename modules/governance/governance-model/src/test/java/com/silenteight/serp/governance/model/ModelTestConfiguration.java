package com.silenteight.serp.governance.model;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;
import com.silenteight.serp.governance.changerequest.approval.ModelApprovalQuery;
import com.silenteight.serp.governance.changerequest.cancel.CancelChangeRequestUseCase;
import com.silenteight.serp.governance.changerequest.list.ListChangeRequestsQuery;
import com.silenteight.serp.governance.model.archive.amqp.ModelsArchivedMessageGateway;
import com.silenteight.serp.governance.model.provide.PolicyFeatureProvider;
import com.silenteight.serp.governance.policy.current.CurrentPolicyProvider;
import com.silenteight.serp.governance.policy.details.PolicyDetailsQuery;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.promote.PromotePolicyUseCase;
import com.silenteight.serp.governance.policy.step.logic.PolicyStepsMatchConditionsNamesProvider;
import com.silenteight.serp.governance.policy.transfer.export.ExportPolicyUseCase;
import com.silenteight.serp.governance.policy.transfer.importing.ImportPolicyUseCase;
import com.silenteight.serp.governance.strategy.CurrentStrategyProvider;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

import static org.springframework.context.annotation.FilterType.REGEX;

@Configuration
@ComponentScan(basePackageClasses = {
    ModelModule.class
}, excludeFilters = {
    @Filter(type = REGEX, pattern = "com.silenteight.serp.governance.model.archive.amqp.*")
})
public class ModelTestConfiguration {

  @MockBean
  AuditingLogger auditingLogger;

  @MockBean
  CurrentStrategyProvider currentStrategyProviderMock;

  @MockBean
  CurrentPolicyProvider currentPolicyProviderMock;

  @MockBean
  PolicyStepsMatchConditionsNamesProvider policyStepsFeaturesProvider;

  @MockBean
  PolicyFeatureProvider policyFeatureProvider;

  @MockBean
  PromotePolicyUseCase promotePolicyUseCase;

  @MockBean
  ImportPolicyUseCase importPolicyUseCase;

  @MockBean
  PolicyDetailsQuery policyDetailsQuery;

  @MockBean
  PolicyService policyService;

  @MockBean
  ExportPolicyUseCase exportPolicyUseCase;

  @MockBean
  ModelApprovalQuery modelApprovalQuery;

  @MockBean
  ListChangeRequestsQuery listChangeRequestsQuery;

  @MockBean
  CancelChangeRequestUseCase cancelChangeRequestUseCase;

  @MockBean
  ModelsArchivedMessageGateway modelsArchivedMessageGateway;

  @MockBean
  AmqpOutboundFactory outboundFactory;
}
