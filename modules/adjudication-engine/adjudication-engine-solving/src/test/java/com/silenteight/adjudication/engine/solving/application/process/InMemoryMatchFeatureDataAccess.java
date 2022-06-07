/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.data.AlertAggregate;
import com.silenteight.adjudication.engine.solving.data.MatchFeatureDataAccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class InMemoryMatchFeatureDataAccess implements MatchFeatureDataAccess {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static Map<Long, String> alerts = Map.of(1L, """
        {
          "analysisId": %s,
          "alertId": %s,
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
                }
              },
              "categories": {
                "categories/test1": {
                  "categoryName": "categories/test1",
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
                }
              },
              "categories": {
                "categories/test1": {
                  "categoryName": "categories/test1",
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
            ]
          },
          "labels": {
            "matchQuantity": "many"
          }
        }
        """,
      2L, """
        {
          "analysisId": %s,
          "alertId": %s,
          "matches": {
            "1": {
              "matchId": 1,
              "clientMatchId": "911",
              "features": {
                "features/name": {
                  "agentConfigFeatureId": 1,
                  "featureName": "features/name",
                  "agentConfig": "agents/name/versions/1.0.0/configs/1"
                }
              },
              "categories": {
                "categories/test1": {
                  "categoryName": "categories/test1",
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
                  "agentConfig": "agents/name/versions/1.0.0/configs/1"
                }
              },
              "categories": {
                "categories/test1": {
                  "categoryName": "categories/test1"
                }
              }
            }
          },
          "policy": "policy",
          "strategy": "startegy",
          "agentFeatures": {
            "agents/name/versions/1.0.0/configs/1": [
              "features/name"
            ]
          },
          "labels": {
            "matchQuantity": "many"
          }
        }
        """);

  @Override
  public Map<Long, AlertAggregate> findAnalysisFeatures(Set<Long> analysis, Set<Long> alerts) {
    var alertsAggregate = new HashMap<Long, AlertAggregate>();
    for (int i = 0; i < alerts.size(); i++) {
      try {
        alertsAggregate.put(alerts.toArray(new Long[0])[i],
            getAlertWithAllData(analysis.toArray(new Long[0])[i], alerts.toArray(new Long[0])[i]));
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    }
    return alertsAggregate;
  }

  private static AlertAggregate getAlertWithAllData(Long analysisId, Long alertId)
      throws JsonProcessingException {
    var jsonString = alerts.get(alertId).formatted(analysisId, alertId);

    return OBJECT_MAPPER.readValue(jsonString, AlertAggregate.class);
  }
}
