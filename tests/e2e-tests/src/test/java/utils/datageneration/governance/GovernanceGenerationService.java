package utils.datageneration.governance;

import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.regex.Pattern;

@UtilityClass
public class GovernanceGenerationService {

  private static final Pattern VALUES_PATTERN = Pattern.compile("\\s*,\\s*");

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

  public static CreateFeatureLogic createFeatureLogic(List<Map<String, String>> features) {
    List<Feature> mappedFeatures = features.stream().map(feature -> createFeature(
            feature.get("name"),
            feature.get("condition"),
            feature.get("values")))
        .toList();

    FeaturesLogic featuresLogic = FeaturesLogic.builder()
        .features(mappedFeatures)
        .toFulfill(mappedFeatures.size())
        .build();

    List<FeaturesLogic> featuresLogicList = new ArrayList<>();
    featuresLogicList.add(featuresLogic);

    return CreateFeatureLogic.builder()
        .featuresLogic(featuresLogicList)
        .build();
  }

  private static Feature createFeature(String name, String condition, String value) {
    return Feature.builder()
        .name(name)
        .condition(condition)
        .values(Arrays.asList(VALUES_PATTERN.split(value)))
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
