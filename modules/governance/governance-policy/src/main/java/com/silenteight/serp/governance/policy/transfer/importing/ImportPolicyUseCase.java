package com.silenteight.serp.governance.policy.transfer.importing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.ConfigurePolicyRequestBuilder;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureLogicConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.StepConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.StepConfiguration.StepConfigurationBuilder;
import com.silenteight.serp.governance.policy.transfer.dto.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class ImportPolicyUseCase {

  @NonNull
  private final TransferredPolicyRootDtoParser transferredPolicyRootParser;

  @NonNull
  private final PolicyService policyService;

  public UUID apply(@NonNull ImportPolicyCommand command) {
    TransferredPolicyRootDto root = transferredPolicyRootParser.parse(command.getInputStream());
    return policyService.doImport(
        createRequest(root.getPolicy(), command.getCreatedBy(), command.getUpdatedBy()));
  }

  private static ConfigurePolicyRequest createRequest(
      @NonNull TransferredPolicyDto transferredPolicy,
      @NonNull String createdBy,
      String updatedBy) {

    ConfigurePolicyRequestBuilder builder = ConfigurePolicyRequest.builder()
        .policyName(transferredPolicy.getName())
        .description(transferredPolicy.getDescription())
        .createdBy(createdBy)
        .updatedBy(updatedBy)
        .stepConfigurations(mapToStepConfigurations(transferredPolicy.getSteps()));

    if (transferredPolicy.getPolicyId() != null)
      builder.policyId(transferredPolicy.getPolicyId());

    return builder.build();
  }

  private static List<StepConfiguration> mapToStepConfigurations(
      List<TransferredStepDto> transferredSteps) {

    return transferredSteps
        .stream()
        .map(ImportPolicyUseCase::mapToStepConfiguration)
        .collect(toList());
  }

  private static StepConfiguration mapToStepConfiguration(TransferredStepDto transferredStep) {
    StepConfigurationBuilder builder = StepConfiguration.builder()
        .solution(transferredStep.getSolution())
        .stepName(transferredStep.getName())
        .stepDescription(transferredStep.getDescription())
        .stepType(transferredStep.getType())
        .featureLogicConfigurations(
            mapToFeatureLogicConfigurations(transferredStep.getFeatureLogics()));

    if (transferredStep.getStepId() != null)
      builder.stepId(transferredStep.getStepId());

    return builder.build();
  }

  private static Collection<FeatureLogicConfiguration> mapToFeatureLogicConfigurations(
      List<TransferredFeatureLogicDto> transferredFeatureLogics) {

    return transferredFeatureLogics
        .stream()
        .map(ImportPolicyUseCase::mapToFeatureLogicConfiguration)
        .collect(toList());
  }

  private static FeatureLogicConfiguration mapToFeatureLogicConfiguration(
      TransferredFeatureLogicDto transferredFeatureLogics) {

    return FeatureLogicConfiguration.builder()
        .toFulfill(transferredFeatureLogics.getToFulfill())
        .featureConfigurations(
            mapToFeatureConfigurations(transferredFeatureLogics.getMatchConditions()))
        .build();
  }

  private static Collection<FeatureConfiguration> mapToFeatureConfigurations(
      Collection<TransferredMatchConditionDto> transferredMatchConditions) {

    return transferredMatchConditions
        .stream()
        .map(ImportPolicyUseCase::mapToFeatureConfiguration)
        .collect(toList());
  }

  private static FeatureConfiguration mapToFeatureConfiguration(
      TransferredMatchConditionDto transferredMatchCondition) {

    return FeatureConfiguration.builder()
        .name(transferredMatchCondition.getName())
        .condition(transferredMatchCondition.getCondition())
        .values(transferredMatchCondition.getValues())
        .build();
  }
}
