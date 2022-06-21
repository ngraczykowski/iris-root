package utils.datageneration.governance;

import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class GovernanceGenerationService {
  public static CreatePolicy createPolicy(String policyName) {
    return CreatePolicy.builder()
        .id(UUID.randomUUID().toString())
        .policyName(policyName)
        .state("DRAFT")
        .build();
  }

  public static CreatePolicyStep createPolicyStep(String name, String solution) {
    return CreatePolicyStep.builder()
        .id(UUID.randomUUID().toString())
        .name(name)
        .description("Lorem ipsum dolor sit amet")
        .solution(solution)
        .type("BUSINESS_LOGIC")
        .build();
  }

  @SuppressWarnings("unchecked")
  public static CreateFeatureLogic createFeatureLogic(String name, String condition, String value) {
    Feature feature = Feature.builder()
        .name(name)
        .condition(condition)
        .values(Arrays.asList(value))
        .build();

    //TODO(kkicza): make it able to process more than 1 feature at once
    ArrayList<Feature> featureList = new ArrayList<>();
    featureList.add(feature);

    FeaturesLogic featuresLogic = FeaturesLogic.builder()
        .features(featureList)
        .toFulfill(1)
        .build();

    ArrayList<FeaturesLogic> featuresLogicList = new ArrayList<>();
    featuresLogicList.add(featuresLogic);

    return CreateFeatureLogic.builder()
        .featuresLogic(featuresLogicList)
        .build();
  }

  public static CreateSolvingModel createSolvingModel(String policyUuid) {
    return CreateSolvingModel
        .builder()
        .id(UUID.randomUUID().toString())
        .policy("policies/"+policyUuid)
        .build();
  }

  public static ActivateSolvingModel activateSolvingModel(String modelUuid) {
    return ActivateSolvingModel
        .builder()
        .id(UUID.randomUUID().toString())
        .modelName("solvingModels/"+modelUuid)
        .comment("Lorem ipsum dolor sit amet")
        .build();
  }
}
