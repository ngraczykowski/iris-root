package com.silenteight.serp.governance.policy.importing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureLogicConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.StepConfiguration;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class ImportPolicyUseCase {

  @NonNull
  private final ImportedPolicyRootParser importedPolicyRootParser;

  @NonNull
  private final PolicyService policyService;

  public UUID apply(@NonNull ImportPolicyCommand command) {
    ImportedPolicyRoot root = importedPolicyRootParser.parse(command.getInputStream());
    return policyService.doImport(createRequest(root.getPolicy(), command.getImportedBy()));
  }

  private static ConfigurePolicyRequest createRequest(
      @NonNull ImportedPolicy importedPolicy, @NonNull String createdBy) {

    return ConfigurePolicyRequest
        .builder()
        .policyName(importedPolicy.getName())
        .description(importedPolicy.getDescription())
        .createdBy(createdBy)
        .stepConfigurations(mapToStepConfigurations(importedPolicy.getSteps()))
        .build();
  }

  private static List<StepConfiguration> mapToStepConfigurations(
      List<ImportedStep> importedSteps) {

    return importedSteps
        .stream()
        .map(ImportPolicyUseCase::mapToStepConfiguration)
        .collect(toList());
  }

  private static StepConfiguration mapToStepConfiguration(ImportedStep importedStep) {
    return StepConfiguration
        .builder()
        .solution(importedStep.getSolution())
        .stepName(importedStep.getName())
        .stepDescription(importedStep.getDescription())
        .stepType(importedStep.getType())
        .featureLogicConfigurations(
            mapToFeatureLogicConfigurations(importedStep.getFeatureLogics()))
        .build();
  }

  private static Collection<FeatureLogicConfiguration> mapToFeatureLogicConfigurations(
      List<ImportedFeatureLogic> importedFeatureLogics) {

    return importedFeatureLogics
        .stream()
        .map(ImportPolicyUseCase::mapToFeatureLogicConfiguration)
        .collect(toList());
  }

  private static FeatureLogicConfiguration mapToFeatureLogicConfiguration(
      ImportedFeatureLogic importedFeatureLogic) {

    return FeatureLogicConfiguration
        .builder()
        .toFulfill(importedFeatureLogic.getToFulfill())
        .featureConfigurations(
            mapToFeatureConfigurations(importedFeatureLogic.getMatchConditions()))
        .build();
  }

  private static Collection<FeatureConfiguration> mapToFeatureConfigurations(
      Collection<MatchCondition> matchConditions) {

    return matchConditions
        .stream()
        .map(ImportPolicyUseCase::mapToFeatureConfiguration)
        .collect(toList());
  }

  private static FeatureConfiguration mapToFeatureConfiguration(
      MatchCondition matchCondition) {

    return FeatureConfiguration
        .builder()
        .name(matchCondition.getName())
        .condition(matchCondition.getCondition())
        .values(matchCondition.getValues())
        .build();
  }
}
