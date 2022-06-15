/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data;

import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverterConfiguration;
import com.silenteight.adjudication.engine.common.rabbit.MessageRegistryConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(
    classes = {
      ProtoMessageToObjectNodeConverterConfiguration.class,
      MessageRegistryConfiguration.class
    })
@SpringBootTest
class MatchSolutionEntityExtractorTest {

  @Autowired private ProtoMessageToObjectNodeConverter converter;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void shouldExtractEntity() {
    assertThat(converter).isNotNull();
    var matchSolutionEntityExtractor = new MatchSolutionEntityExtractor(converter, objectMapper);
    var matchSolution = MatchSolutionEntityGenerator.createMatchSolution();
    var extractedEntity = matchSolutionEntityExtractor.extract(matchSolution);

    assertThat(extractedEntity.reason()).isEqualTo("{\"reason\":\"something\"}");
    assertThat(extractedEntity.solution()).isEqualTo("NO_DATA");

    var featureKeys =
        matchSolution.features().keySet().stream().map(s -> "\"" + s + "\"" + ":{").toList();
    var categories =
        matchSolution.categories().entrySet().stream()
            .map(es -> "\"" + es.getKey() + "\":\"" + es.getValue().getCategoryValue() + "\"")
            .toList();
    assertThat(extractedEntity.matchContext())
        .contains("\"matchId\":\"" + matchSolution.clientMatchId() + "\"")
        .contains("\"reason\":" + matchSolution.reason())
        .contains("\"solution\":\"" + matchSolution.solution() + "\"")
        .contains(featureKeys)
        .contains(categories);
  }
}
