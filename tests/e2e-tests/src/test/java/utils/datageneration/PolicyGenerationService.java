package utils.datageneration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class PolicyGenerationService {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final TemplateService templateService = new TemplateService();

  public Policy generatePolicy(String name, String state, List<PolicyStep> policySteps) {
    String uuid = String.valueOf(UUID.randomUUID());

    return Policy.builder()
        .uuid(uuid)
        .name(name)
        .state(state)
        .steps(policySteps)
        .creationPayload(generatePayloadForPolicyCreation(uuid, name))
        .build();
  }

  public PolicyStep generatePolicyStep(String name, String solution, List<Feature> featureList) {
    return PolicyStep.builder()
        .id(String.valueOf(UUID.randomUUID()))
        .name(name)
        .solution(solution)
        .featureList(featureList)
        .templatedFeatureList(templateFeatureList(featureList))
        .build();
  }

  public Feature generateFeature(String name, String condition, List<String> values) {
    return Feature.builder()
        .name(name)
        .condition(condition)
        .values(values)
        .build();
  }

  @SneakyThrows
  private String generatePayloadForPolicyCreation(String uuid, String name) {
    Map<String, String> map = new HashMap<>(Collections.emptyMap());
    map.put("policyUuid", uuid);
    map.put("policyName", name);
    map.put("state", "DRAFT");

    return templateService.template(getJsonTemplate("newPolicy"), map);
  }

  @SneakyThrows
  private String templateFeatureList(List<Feature> featureList) {
    List<ObjectNode> features =
        featureList.stream()
            .map(
                feature ->
                    objectMapper.convertValue(feature, new FeatureDataTypeRef()))
            .map(featuresDataMap -> templateService.template(getJsonTemplate("featureTemplate"),
                featuresDataMap))
            .map(this::asObjectNode)
            .collect(toList());

    ArrayNode jsonNodes = objectMapper.createArrayNode().addAll(features);

    return objectMapper.writeValueAsString(jsonNodes);
  }

  @SneakyThrows
  private String getJsonTemplate(String jsonName) {
    return new String(
        Files.readAllBytes(
            Paths.get(String.format("src/test/resources/policiesAPI/%s.json", jsonName))));
  }

  @SneakyThrows
  private ObjectNode asObjectNode(String body) {
    return (ObjectNode) objectMapper.readTree(body);
  }

  private static class FeatureDataTypeRef extends TypeReference<Map<String, String>> {
    // Intentionally left empty.
  }
}
