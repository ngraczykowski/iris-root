/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.app;

import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverterConfiguration;
import com.silenteight.adjudication.engine.common.rabbit.MessageRegistryConfiguration;
import com.silenteight.adjudication.engine.solving.data.AlertAggregate;
import com.silenteight.adjudication.engine.solving.data.MatchFeatureDataAccess;
import com.silenteight.adjudication.engine.solving.data.jdbc.JdbcAccessConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = { JdbcAccessConfiguration.class,
                                  ProtoMessageToObjectNodeConverterConfiguration.class,
                                  MessageRegistryConfiguration.class })
@Sql
class SelectAnalysisFeaturesQueryIT extends BaseJdbcTest {

  private static final Map<Long, String> alerts =
      Map.of(
          1L,
          "withAllValues.json",
          2L,
          "withoutFeatures.json");

  @Autowired MatchFeatureDataAccess jdbcMatchFeaturesDataAccess;
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Test
  void shouldSelectAlertData() {
    var alertMap = jdbcMatchFeaturesDataAccess.findAnalysisFeatures(Set.of(1L), Set.of(1L));
    assertThat(alertMap.size()).isEqualTo(1);
    var alert = alertMap.get(1L);
    var expectedAlert = getAlertWithAllData(1L);
    assertThat(alert)
        .usingRecursiveComparison()
        .isEqualTo(expectedAlert);
  }

  @Test
  void shouldSelectAlertDataForMultihit() {
    var alert = jdbcMatchFeaturesDataAccess.findAnalysisAlertAndAggregate(1L, 1L);
    assertThat(alert).isNotNull();
    var expectedAlert = getAlertWithAllData(1L);
    assertThat(alert)
        .usingRecursiveComparison()
        .isEqualTo(expectedAlert);
  }

  @Test
  void shouldSelectAlertWithoutFeatures() {
    var alert = jdbcMatchFeaturesDataAccess.findAnalysisAlertAndAggregate(2L, 2L);
    assertThat(alert).isNotNull();
    var expectedAlert = getAlertWithAllData(2L);
    assertThat(alert)
        .usingRecursiveComparison()
        .isEqualTo(expectedAlert);
  }


  private static AlertAggregate getAlertWithAllData(Long alertId) {

    var classLoader = SelectAnalysisFeaturesQueryIT.class.getClassLoader();
    var file =
        new File(
            Objects.requireNonNull(classLoader.getResource("alerts/" + alerts.get(alertId)))
                .getFile());

    try {
      return OBJECT_MAPPER.readValue(file, AlertAggregate.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
