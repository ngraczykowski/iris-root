package com.silenteight.serp.governance.policy.domain;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.proto.governance.v1.api.FeatureVectorSolution;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest.FeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest.FeatureLogicConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest.StepConfiguration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.silenteight.proto.governance.v1.api.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.serp.governance.policy.domain.PolicyState.SAVED;
import static com.silenteight.serp.governance.policy.domain.StepType.BUSINESS_LOGIC;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {

  private PolicyRepository policyRepository = new InMemoryPolicyRepository();
  @Mock
  private AuditingLogger auditingLogger;
  @Mock
  private ApplicationEventPublisher eventPublisher;

  private PolicyService underTest;

  @BeforeEach
  void setUp() {
    underTest = new PolicyDomainConfiguration()
        .policyService(policyRepository, auditingLogger, eventPublisher);
  }

  @Test
  void createPolicy() {
    // given
    String featureName = "nameAgent";
    Collection<String> featureValues = List.of("EXACT_MATCH", "NEAR_MATCH");
    int logicCount = 1;
    FeatureVectorSolution stepSolution = SOLUTION_FALSE_POSITIVE;
    String stepName = "step-1";
    String stepDescription = "This is test description";
    StepType stepType = BUSINESS_LOGIC;
    String policyName = "policy-name";
    String creator = "asmith";
    FeatureConfiguration featureConfiguration =
        FeatureConfiguration.builder()
            .name(featureName)
            .values(featureValues)
            .build();
    FeatureLogicConfiguration featureLogicConfiguration = FeatureLogicConfiguration
        .builder()
        .count(logicCount)
        .featureConfigurations(singletonList(featureConfiguration))
        .build();
    StepConfiguration stepConfiguration = StepConfiguration
        .builder()
        .solution(stepSolution)
        .stepName(stepName)
        .stepDescription(stepDescription)
        .stepType(stepType)
        .featureLogicConfigurations(singletonList(featureLogicConfiguration))
        .build();
    ImportPolicyRequest request = ImportPolicyRequest
        .builder()
        .policyName(policyName)
        .createdBy(creator)
        .stepConfigurations(singletonList(stepConfiguration))
        .build();

    // when
    UUID policyId = underTest.doImport(request);

    // then
    var logCaptor = ArgumentCaptor.forClass(AuditDataDto.class);

    verify(auditingLogger).log(logCaptor.capture());

    var log = logCaptor.getValue();
    assertThat(log.getType()).isEqualTo("PolicyCreateRequested");
    assertThat(log.getEntityId()).isEqualTo(policyId.toString());
    assertThat(log.getEntityClass()).isEqualTo("Policy");
    assertThat(log.getEntityAction()).isEqualTo("CREATE");
    assertThat(log.getDetails()).isEqualTo(request.toString());

    var policy = policyRepository.getByPolicyId(policyId);
    assertThat(policy.getName()).isEqualTo(policyName);
    assertThat(policy.getState()).isEqualTo(SAVED);
    assertThat(policy.getPolicyId()).isEqualTo(policyId);
    assertThat(policy.getCreatedBy()).isEqualTo(creator);
    assertThat(policy.getUpdatedBy()).isEqualTo(creator);
    assertThat(policy.getSteps()).hasSize(1);

    var step = policy.getSteps().iterator().next();
    assertThat(step.getSolution()).isEqualTo(stepSolution);
    assertThat(step.getName()).isEqualTo(stepName);
    assertThat(step.getDescription()).isEqualTo(stepDescription);
    assertThat(step.getType()).isEqualTo(stepType);
    assertThat(step.getFeatureLogics()).hasSize(1);

    var featureLogic = step.getFeatureLogics().iterator().next();
    assertThat(featureLogic.getCount()).isEqualTo(logicCount);
    assertThat(featureLogic.getFeatures()).hasSize(1);

    var feature = featureLogic.getFeatures().iterator().next();
    assertThat(feature.getName()).isEqualTo(featureName);
    assertThat(feature.getValues()).isEqualTo(featureValues);

    var eventCaptor = ArgumentCaptor.forClass(PolicyCreatedEvent.class);

    verify(eventPublisher).publishEvent(eventCaptor.capture());

    var event = eventCaptor.getValue();
    assertThat(event.getPolicyId()).isEqualTo(policyId);
  }
}
