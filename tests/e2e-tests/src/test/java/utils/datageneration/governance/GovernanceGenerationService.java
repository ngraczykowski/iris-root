package utils.datageneration.governance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.SneakyThrows;
import utils.datageneration.CommonUtils;

import java.util.*;

public class GovernanceGenerationService {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final CommonUtils commonUtils = new CommonUtils();

  public Policy generatePolicy(String name, String state, List<PolicyStep> policySteps) {
    String uuid = String.valueOf(UUID.randomUUID());

    return Policy
        .builder()
        .uuid(uuid)
        .name(name)
        .state(state)
        .steps(policySteps)
        .creationPayload(generatePayloadForPolicyCreation(uuid, name))
        .build();
  }

  public PolicyStep generatePolicyStep(String name, String solution, List<Feature> featureList) {
    return PolicyStep
        .builder()
        .uuid(String.valueOf(UUID.randomUUID()))
        .name(name)
        .solution(solution)
        .featureList(featureList)
        .templatedFeatureList(templateFeatureList(featureList))
        .build();
  }

  public Feature generateFeature(String name, String condition, String values) {
    return Feature.builder().name(name).condition(condition).values(values).build();
  }

  public SolvingModel generateSolvingModel(String policyUuid) {
    String uuid = String.valueOf(UUID.randomUUID());

    return SolvingModel
        .builder()
        .uuid(uuid)
        .policyUuid(policyUuid)
        .creationPayload(generatePayloadForSolvingModelCreation(policyUuid, uuid))
        .build();
  }

  @SneakyThrows
  private String generatePayloadForPolicyCreation(String uuid, String name) {
    Map<String, String> map = new HashMap<>(Collections.emptyMap());
    map.put("uuid", uuid);
    map.put("name", name);
    map.put("state", "DRAFT");

    return commonUtils.template(commonUtils.getJsonTemplate("newPolicy"), map);
  }

  @SneakyThrows
  private String generatePayloadForSolvingModelCreation(String policyUuid, String uuid) {
    Map<String, String> map = new HashMap<>(Collections.emptyMap());
    map.put("uuid", uuid);
    map.put("policyUuid", policyUuid);

    return commonUtils.template(commonUtils.getJsonTemplate("newSolvingModel"), map);
  }

  @SneakyThrows
  @SuppressWarnings("unchecked")
  public List<String> generatePayloadsForStepsAddition(List<PolicyStep> stepList) {
    List<String> stepAdditionPayloads = new ArrayList<>(Collections.emptyList());

    stepList.forEach(step -> {
      stepAdditionPayloads.add(commonUtils.template(
          commonUtils.getJsonTemplate("newPolicyStep"),
          objectMapper.convertValue(step, Map.class)));
    });

    return stepAdditionPayloads;
  }

  //TODO MAKE IT ABLE TO PROCESS MORE THAN 1 FEATURE PER REQUEST
  @SneakyThrows
  private String templateFeatureList(List<Feature> featureList) {
    List<ObjectNode> features = featureList
        .stream()
        .map(feature -> objectMapper.convertValue(feature, new FeatureDataTypeRef()))
        .map(featuresDataMap -> commonUtils.templateObject(
            commonUtils.getJsonTemplate("logicForStep"),
            featuresDataMap))
        .map(this::asObjectNode)
        .toList();

    return objectMapper.writeValueAsString(features.get(0));
  }

  @SneakyThrows
  private ObjectNode asObjectNode(String body) {
    return (ObjectNode) objectMapper.readTree(body);
  }

  private static class FeatureDataTypeRef extends TypeReference<Map<String, Object>> {
    // Intentionally left empty.
  }
}
