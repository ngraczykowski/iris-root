package com.silenteight.serp.governance.policy.importing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest.FeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest.FeatureLogicConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest.StepConfiguration;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
    return policyService.doImport(createRequest(root.getPolicy()));
  }

  private static ImportPolicyRequest createRequest(@NonNull ImportedPolicy importedPolicy) {
    return ImportPolicyRequest
        .builder()
        .policyName(importedPolicy.getName())
        // TODO(mmastylo): use real user name
        .createdBy("import")
        .stepConfigurations(mapToStepConfigurations(importedPolicy.getSteps()))
        .build();
  }

  private static Collection<StepConfiguration> mapToStepConfigurations(
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
        .count(importedFeatureLogic.getCount())
        .featureConfigurations(mapToFeatureConfigurations(importedFeatureLogic.getFeatures()))
        .build();
  }

  private static Collection<FeatureConfiguration> mapToFeatureConfigurations(
      Map<String, List<String>> features) {

    return features
        .entrySet()
        .stream()
        .map(ImportPolicyUseCase::mapToFeatureConfiguration)
        .collect(toList());
  }

  private static FeatureConfiguration mapToFeatureConfiguration(
      Map.Entry<String, List<String>> feature) {

    return FeatureConfiguration
        .builder()
        .name(feature.getKey())
        .values(feature.getValue())
        .build();
  }
}
