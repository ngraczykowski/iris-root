package com.silenteight.payments.bridge.firco.datasource.service.process;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.name.v1.AlertedPartyName;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.datasource.api.name.v1.NameFeatureInput.EntityType;
import com.silenteight.datasource.api.name.v1.WatchlistName;
import com.silenteight.datasource.api.name.v1.WatchlistName.NameType;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import com.google.protobuf.Any;

import java.time.Duration;
import java.util.List;
import javax.annotation.Nonnull;

class NameMatchedTextAgentEtlProcess extends BaseAgentEtlProcess<NameFeatureInput> {

  private static final String NAME_MATCH_TEXT_FEATURE = "nameMatchedText";

  NameMatchedTextAgentEtlProcess(
      AgentInputServiceBlockingStub blockingStub, Duration timeout) {
    super(blockingStub, timeout);
  }


  @Override
  protected List<FeatureInput> createDataSourceFeatureInputs(HitData hitData) {
    var featureInput = FeatureInput
        .newBuilder()
        .setFeature(getFullFeatureName(NAME_MATCH_TEXT_FEATURE))
        .setAgentFeatureInput(Any.pack(createNameFeatureInput(hitData)))
        .build();

    return List.of(featureInput);
  }

  protected static NameFeatureInput createNameFeatureInput(HitData hitData) {

    var matchingText = hitData.getHitAndWlPartyData().getMatchingText();
    var watchlistName = hitData.getHitAndWlPartyData().getName();
    var alertedPartyType =
        mapWatchListTypeToEntityType(hitData.getHitAndWlPartyData().getWatchlistType());
    var matchingTexts = hitData.getHitAndWlPartyData().getAllMatchingTexts();

    var matchingTextAsAlertedPartyName =
        AlertedPartyName.newBuilder().setName(matchingText).build();

    var watchlistNameAsObject = WatchlistName.newBuilder()
        .setName(watchlistName)
        .setType(NameType.REGULAR)
        .build();

    return NameFeatureInput.newBuilder()
        .setFeature(getFullFeatureName(NAME_MATCH_TEXT_FEATURE))
        .addAlertedPartyNames(matchingTextAsAlertedPartyName)
        .addWatchlistNames(watchlistNameAsObject)
        .setAlertedPartyType(alertedPartyType)
        .addAllMatchingTexts(matchingTexts)
        .build();
  }

  @Nonnull
  private static EntityType mapWatchListTypeToEntityType(WatchlistType watchlistType) {

    switch (watchlistType) {
      case INDIVIDUAL:
        return EntityType.INDIVIDUAL;
      case COMPANY:
        return EntityType.ORGANIZATION;
      case ADDRESS:
      case VESSEL:
        return EntityType.ENTITY_TYPE_UNSPECIFIED;
      default:
        throw new UnsupportedOperationException();
    }
  }
}
