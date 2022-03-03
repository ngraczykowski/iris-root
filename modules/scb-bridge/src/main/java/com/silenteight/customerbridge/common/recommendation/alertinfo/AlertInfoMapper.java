package com.silenteight.customerbridge.common.recommendation.alertinfo;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.common.protocol.AlertWrapper;
import com.silenteight.customerbridge.common.protocol.MatchWrapper;
import com.silenteight.proto.serp.scb.v1.*;
import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.reporter.AlertInfo;
import com.silenteight.proto.serp.v1.reporter.AnalystDecisionInfo;
import com.silenteight.proto.serp.v1.reporter.MatchInfo;

import java.util.Optional;

//TODO(iwnek) make it package private, currently is not, because it
// is used by CbsAlertHandlingReactiveMessageListener and ReportDataBroadcaster
@RequiredArgsConstructor
public class AlertInfoMapper {

  public AlertInfoMapResult map(Alert alert) {
    Mapper mapper = new Mapper(alert);
    return new AlertInfoMapResult(mapper.getAlertInfo(), mapper.getScbAlertInfo());
  }

  @RequiredArgsConstructor
  private static class Mapper {

    private final AlertWrapper alertWrapper;

    public Mapper(Alert alert) {
      this.alertWrapper = new AlertWrapper(alert);
    }

    public AlertInfo getAlertInfo() {
      AlertInfo.Builder builder = AlertInfo
          .newBuilder()
          .setAlertId(alertWrapper.getId())
          .setGeneratedAt(alertWrapper.getGeneratedAt())
          .setDecisionGroup(alertWrapper.getDecisionGroup());

      alertWrapper.getLatestAnalystDecision().ifPresent(d -> builder.setLatestAnalystDecision(
          AnalystDecisionInfo
              .newBuilder()
              .setSolution(d.getSolution().name())
              .setComment(d.getComment())
              .setCreatedAt(d.getCreatedAt())
              .build()));

      alertWrapper.iterateMatches((index, matchWrapper) -> builder.addMatches(
          MatchInfo
              .newBuilder()
              .setMatchId(matchWrapper.getId())
              .setIndex(index)
              .build()));

      return builder.build();
    }

    public ScbAlertInfo getScbAlertInfo() {
      ScbAlertDetails alertDetails = unpackAlertDetails();
      ScbAlertedPartyDetails alertedPartyDetails = unpackAlertedPartyDetails();

      ScbAlertInfo.Builder builder = ScbAlertInfo
          .newBuilder()
          .setAlertId(alertWrapper.getId())
          .setUnit(alertDetails.getUnit())
          .setBatchId(alertDetails.getBatchId())
          .setApDbCountry(alertedPartyDetails.getApDbCountry())
          .setApSrcSysId(alertedPartyDetails.getApSrcSysId())
          .setSystemId(alertDetails.getSystemId())
          .setWatchlistId(alertDetails.getWatchlistId());

      getLastIntermediateStateName().ifPresent(builder::setLastIntermediateStateName);

      alertWrapper.iterateMatches(
          (index, match) -> builder.addHitDetails(buildHitDetails(index, match)));

      return builder.build();
    }

    private ScbAlertDetails unpackAlertDetails() {
      return alertWrapper
          .unpackDetails(ScbAlertDetails.class)
          .orElse(ScbAlertDetails.newBuilder().build());
    }

    private ScbAlertedPartyDetails unpackAlertedPartyDetails() {
      return alertWrapper
          .unpackAlertedPartyDetails(ScbAlertedPartyDetails.class)
          .orElse(ScbAlertedPartyDetails.newBuilder().build());
    }

    private static HitDetails buildHitDetails(Integer index, MatchWrapper match) {
      ScbWatchlistPartyDetails partyDetails = unpackMatchedPartyDetails(match);

      return HitDetails
          .newBuilder()
          .setMatchIndex(index)
          .setWlId(partyDetails.getWlId())
          .addAllWlHitType(partyDetails.getWlHitTypeList())
          .build();
    }

    private static ScbWatchlistPartyDetails unpackMatchedPartyDetails(MatchWrapper accessor) {
      return accessor
          .unpackMatchedPartyDetails(ScbWatchlistPartyDetails.class)
          .orElse(ScbWatchlistPartyDetails.newBuilder().build());
    }

    private Optional<String> getLastIntermediateStateName() {
      var decisions = alertWrapper.getAlert().getDecisionsList();
      return DecisionInfoRetriever.getLastIntermediateStateName(decisions);
    }
  }
}
