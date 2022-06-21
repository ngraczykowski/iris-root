/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.data.AlertAggregate;
import com.silenteight.adjudication.engine.solving.data.FeatureAggregate;
import com.silenteight.adjudication.engine.solving.data.MatchAggregate;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;
import com.silenteight.agents.v1.api.exchange.AgentOutput;
import com.silenteight.agents.v1.api.exchange.AgentOutput.Feature;
import com.silenteight.agents.v1.api.exchange.AgentOutput.FeatureSolution;

import com.google.protobuf.Struct;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class AgentResponseProcessFixture {

  public static AlertAggregate alertOneAggregateWithTwoMatchesFixture() {
    var match1Aggregate =
        new MatchAggregate(
            1,
            UUID.randomUUID().toString(),
            Map.of(
                "features/name",
                new FeatureAggregate(
                    1, "features/name", "agents/name/versions/1.0.0/configs/1", null, null)),
            Map.of());
    var match2Aggregate =
        new MatchAggregate(
            2,
            UUID.randomUUID().toString(),
            Map.of(
                "features/name",
                new FeatureAggregate(
                    1, "features/name", "agents/name/versions/1.0.0/configs/1", null, null)),
            Map.of());
    return AlertAggregate.builder()
        .analysisId(1)
        .alertId(1)
        .priority(0)
        .policy("policies/5be5dbac-7646-4b4f-9229-4e489148b820")
        .strategy("strategies/USE_ANALYST_SOLUTION")
        .labels(new HashMap<>())
        .matches(Map.of(1L, match1Aggregate, 2L, match2Aggregate))
        .build();
  }

  public static AlertAggregate alertTwoAggregateWithOneMatcheFixture() {
    var match1Aggregate =
        new MatchAggregate(
            2,
            UUID.randomUUID().toString(),
            Map.of(
                "features/name",
                new FeatureAggregate(
                    1, "features/name", "agents/name/versions/1.0.0/configs/1", null, null)),
            Map.of());

    return AlertAggregate.builder()
        .analysisId(1)
        .alertId(2)
        .priority(0)
        .policy("policies/5be5dbac-7646-4b4f-9229-4e489148b820")
        .strategy("strategies/USE_ANALYST_SOLUTION")
        .labels(new HashMap<>())
        .matches(Map.of(3L, match1Aggregate))
        .build();
  }
  public static AgentExchangeResponse agentResponseFixture() {
    return AgentExchangeResponse.newBuilder()
        .addAgentOutputs(
            AgentOutput.newBuilder()
                .setMatch("alerts/1/matches/1")
                .addFeatures(
                    Feature.newBuilder()
                        .setFeatureSolution(
                            FeatureSolution.newBuilder()
                                .setSolution("NO_DATA")
                                .setReason(Struct.newBuilder().build())
                                .build())
                        .setFeature("features/name")
                        .build())
                .build())
        .addAgentOutputs(
            AgentOutput.newBuilder()
                .setMatch("alerts/1/matches/2")
                .addFeatures(
                    Feature.newBuilder()
                        .setFeatureSolution(
                            FeatureSolution.newBuilder()
                                .setSolution("MATCH")
                                .setReason(Struct.newBuilder().build())
                                .build())
                        .setFeature("features/name")
                        .build())
                .build())
        .addAgentOutputs(
            AgentOutput.newBuilder()
                .setMatch("alerts/2/matches/3")
                .addFeatures(
                    Feature.newBuilder()
                        .setFeatureSolution(
                            FeatureSolution.newBuilder()
                                .setSolution("NO_MATCH")
                                .setReason(Struct.newBuilder().build())
                                .build())
                        .setFeature("features/name")
                        .build())
                .build())
        .build();
  }
}
