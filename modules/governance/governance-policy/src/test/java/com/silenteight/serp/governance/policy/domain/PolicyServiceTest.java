package com.silenteight.serp.governance.policy.domain;

import com.silenteight.proto.governance.v1.api.FeatureVectorSolution;
import com.silenteight.serp.governance.policy.domain.CreatePolicyRequest.FeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.CreatePolicyRequest.FeatureLogicConfiguration;
import com.silenteight.serp.governance.policy.domain.CreatePolicyRequest.StepConfiguration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.silenteight.proto.governance.v1.api.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.serp.governance.policy.domain.StepType.BUSINESS_LOGIC;
import static java.util.Collections.singletonList;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {

  @Mock
  private PolicyRepository repository;

  @InjectMocks
  private PolicyService underTest;

  @Test
  void importPolicy() {
    // given
    String featureName = "nameAgent";
    Collection<String> featureValues = List.of("EXACT_MATCH", "NEAR_MATCH");
    int logicCount = 1;
    FeatureVectorSolution stepSolution = SOLUTION_FALSE_POSITIVE;
    UUID stepId = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");
    String stepName = "step-1";
    String stepDescription = "This is test description";
    StepType stepType = BUSINESS_LOGIC;
    UUID policyId = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
    String policyName = "policy-name";
    String creator = "asmith";
    FeatureConfiguration featureConfiguration = FeatureConfiguration.builder()
        .name(featureName)
        .values(featureValues)
        .build();
    FeatureLogicConfiguration featureLogicConfiguration = FeatureLogicConfiguration.builder()
        .count(logicCount)
        .featureConfigurations(singletonList(featureConfiguration))
        .build();
    StepConfiguration stepConfiguration = StepConfiguration.builder()
        .solution(stepSolution)
        .stepId(stepId)
        .stepName(stepName)
        .stepDescription(stepDescription)
        .stepType(stepType)
        .featureLogicConfigurations(singletonList(featureLogicConfiguration))
        .build();
    CreatePolicyRequest request = CreatePolicyRequest.builder()
        .policyName(policyName)
        .policyId(policyId)
        .createdBy(creator)
        .stepConfigurations(singletonList(stepConfiguration))
        .build();

    // when
    underTest.doImport(request);

    // then
    var policyCaptor = ArgumentCaptor.forClass(Policy.class);

    verify(repository).save(policyCaptor.capture());

    var policy = policyCaptor.getValue();
    assertThat(policy.getName()).isEqualTo(policyName);
    assertThat(policy.getPolicyId()).isEqualTo(policyId);
    assertThat(policy.getCreatedBy()).isEqualTo(creator);
    assertThat(policy.getUpdatedBy()).isEqualTo(creator);
    assertThat(policy.getSteps()).hasSize(1);

    var step = policy.getSteps().iterator().next();
    assertThat(step.getSolution()).isEqualTo(stepSolution);
    assertThat(step.getStepId()).isEqualTo(stepId);
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
  }
}
