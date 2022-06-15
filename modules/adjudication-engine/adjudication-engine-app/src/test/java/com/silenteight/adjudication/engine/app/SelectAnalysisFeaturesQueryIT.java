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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = { JdbcAccessConfiguration.class,
                                  ProtoMessageToObjectNodeConverterConfiguration.class,
                                  MessageRegistryConfiguration.class })
@Sql
class SelectAnalysisFeaturesQueryIT extends BaseJdbcTest {

  @Autowired MatchFeatureDataAccess jdbcMatchFeaturesDataAccess;
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Test
  void shouldSelectAlertData() throws JsonProcessingException {
    var alertMap = jdbcMatchFeaturesDataAccess.findAnalysisFeatures(Set.of(1L), Set.of(1L));
    assertThat(alertMap.size()).isEqualTo(1);
    var alert = alertMap.get(1L);
    var expectedAlert = getAlertWithAllData();
    assertThat(alert)
        .usingRecursiveComparison()
        .isEqualTo(expectedAlert);
  }

  @Test
  void shouldSelectAlertDataForMultihit() throws JsonProcessingException {
    var alert = jdbcMatchFeaturesDataAccess.findAnalysisAlertAndAggregate(1L, 1L);
    assertThat(alert).isNotNull();
    var expectedAlert = getAlertWithAllData();
    assertThat(alert)
        .usingRecursiveComparison()
        .isEqualTo(expectedAlert);
  }

  private static AlertAggregate getAlertWithAllData() throws JsonProcessingException {
    var jsonString = """
        {
          "analysisId": 1,
          "alertId": 1,
          "matches": {
            "1": {
              "matchId": 1,
              "clientMatchId": "911",
              "features": {
                "features/name": {
                  "agentConfigFeatureId": 1,
                  "featureName": "features/name",
                  "agentConfig": "agents/name/versions/1.0.0/configs/1",
                  "featureValue": "INCONCLUSIVE",
                  "featureReason": "{}"
                },
                "features/geo": {
                  "agentConfigFeatureId": 2,
                  "featureName": "features/geo",
                  "agentConfig": "agents/geo/versions/1.0.0/configs/1",
                  "featureValue": "INCONCLUSIVE",
                  "featureReason": "{}"
                }
              },
              "categories": {
                "categories/test1": {
                  "categoryName": "categories/test1",
                  "categoryValue": "NO_DECISION"
                },
                "categories/test2": {
                  "categoryName": "categories/test2",
                  "categoryValue": "NO_DECISION"
                }
              }
            },
            "2": {
              "matchId": 2,
              "clientMatchId": "912",
              "features": {
                "features/name": {
                  "agentConfigFeatureId": 1,
                  "featureName": "features/name",
                  "agentConfig": "agents/name/versions/1.0.0/configs/1",
                  "featureValue": "INCONCLUSIVE",
                  "featureReason": "{}"
                },
                "features/geo": {
                  "agentConfigFeatureId": 2,
                  "featureName": "features/geo",
                  "agentConfig": "agents/geo/versions/1.0.0/configs/1",
                  "featureValue": "INCONCLUSIVE",
                  "featureReason": "{}"
                }
              },
              "categories": {
                "categories/test1": {
                  "categoryName": "categories/test1",
                  "categoryValue": "DECISION"
                },
                "categories/test2": {
                  "categoryName": "categories/test2",
                  "categoryValue": "DECISION"
                }
              }
            }
          },
          "policy": "policy",
          "strategy": "startegy",
          "agentFeatures": {
            "agents/name/versions/1.0.0/configs/1": [
              "features/name"
            ],
            "agents/geo/versions/1.0.0/configs/1": [
              "features/geo"
            ]
          },
          "labels": {
            "matchQuantity": "many"
          }
        }
        """;

    return OBJECT_MAPPER.readValue(jsonString, AlertAggregate.class);
  }
}
