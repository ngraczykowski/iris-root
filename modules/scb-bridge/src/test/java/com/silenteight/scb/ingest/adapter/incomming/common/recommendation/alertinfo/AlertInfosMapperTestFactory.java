package com.silenteight.scb.ingest.adapter.incomming.common.recommendation.alertinfo;

import com.silenteight.proto.serp.scb.v1.ScbAlertDetails;
import com.silenteight.proto.serp.scb.v1.ScbAlertedPartyDetails;
import com.silenteight.proto.serp.scb.v1.ScbWatchlistPartyDetails;
import com.silenteight.proto.serp.v1.alert.*;
import com.silenteight.proto.serp.v1.common.ObjectId;
import com.silenteight.protocol.utils.ObjectIds;
import com.silenteight.sep.base.common.protocol.AnyUtils;

import com.google.protobuf.Message;
import com.google.protobuf.Timestamp;

import javax.validation.constraints.NotNull;

class AlertInfosMapperTestFactory {

  static Alert createAlert() {
    return Alert
        .newBuilder()
        .setId(createObjectId("alert1"))
        .setDecisionGroup("decisionGroup")
        .setGeneratedAt(createTimestamp(1))
        .addDecisions(createDecision(AnalystSolution.ANALYST_OTHER, 1))
        .addDecisions(createDecision(AnalystSolution.ANALYST_FALSE_POSITIVE, 2))
        .addDecisions(createDecision(AnalystSolution.ANALYST_TRUE_POSITIVE, 3))
        .addMatches(createMatch(0))
        .addMatches(createMatch(1))
        .setDetails(AnyUtils.pack(createAlertDetails()))
        .setAlertedParty(createParty(createPartyDetails()))
        .build();
  }

  @NotNull
  private static Decision createDecision(AnalystSolution solution, int seconds) {
    return Decision
        .newBuilder()
        .setSolution(solution)
        .setCreatedAt(createTimestamp(seconds))
        .build();
  }

  @NotNull
  private static Match createMatch(int index) {
    return Match
        .newBuilder()
        .setId(createObjectId("match" + index))
        .setIndex(index)
        .setMatchedParty(createParty(createWatchlistPartyDetails()))
        .build();
  }

  @NotNull
  private static Timestamp createTimestamp(int seconds) {
    return Timestamp.newBuilder().setSeconds(seconds).build();
  }

  @NotNull
  private static ObjectId createObjectId(String sourceId) {
    return ObjectIds.randomFromSource(sourceId, "discriminator");
  }

  @NotNull
  private static Party createParty(Message partyDetails) {
    return Party
        .newBuilder()
        .setDetails(AnyUtils.pack(partyDetails))
        .build();
  }

  @NotNull
  private static ScbAlertDetails createAlertDetails() {
    return ScbAlertDetails
        .newBuilder()
        .setBatchId("batchId")
        .setUnit("unit")
        .setSystemId("systemId")
        .build();
  }

  @NotNull
  private static ScbAlertedPartyDetails createPartyDetails() {
    return ScbAlertedPartyDetails
        .newBuilder()
        .setApDbCountry("dbCountry")
        .setApSrcSysId("srcSysId")
        .build();
  }

  @NotNull
  private static ScbWatchlistPartyDetails createWatchlistPartyDetails() {
    return ScbWatchlistPartyDetails
        .newBuilder()
        .setWlId("wlId1")
        .addWlHitType("type1")
        .addWlHitType("type2")
        .build();
  }
}
