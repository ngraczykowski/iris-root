package com.silenteight.adjudication.engine.analysis.matchsolution.dto;

import lombok.Value;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MatchContextTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void serializationTest() {
    var context = MatchContext.builder()
        .matchId(("2"))
        .solution("solution1")
        .reason(convertToNode(new TestReason("match_reason")))
        .category("category1", "categoryValue1")
        .feature("feature1", FeatureContext.builder()
            .agentConfig("feature1Conf")
            .solution("featureSolution1")
            .reason(convertToNode(new TestReason("feature_reason1")))
            .build())
        .build();

    var converted = convertToNode(context);

    assertThat(converted.toPrettyString()).isEqualTo("{\n"
        + "  \"matchId\" : \"2\",\n"
        + "  \"solution\" : \"solution1\",\n"
        + "  \"reason\" : {\n"
        + "    \"name\" : \"match_reason\"\n"
        + "  },\n"
        + "  \"categories\" : {\n"
        + "    \"category1\" : \"categoryValue1\"\n"
        + "  },\n"
        + "  \"features\" : {\n"
        + "    \"feature1\" : {\n"
        + "      \"solution\" : \"featureSolution1\",\n"
        + "      \"reason\" : {\n"
        + "        \"name\" : \"feature_reason1\"\n"
        + "      },\n"
        + "      \"agentConfig\" : \"feature1Conf\"\n"
        + "    }\n"
        + "  }\n"
        + "}");
  }

  private ObjectNode convertToNode(Object context) {
    return objectMapper.convertValue(context, ObjectNode.class);
  }

  @Value
  static class TestReason {

    String name;
  }
}
