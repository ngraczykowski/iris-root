package com.silenteight.searpayments.scb.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.model.WatchlistType;
import com.silenteight.proto.agent.name.v1.api.CompareNamesInput;
import com.silenteight.proto.agent.name.v1.api.CompareNamesRequest;
import com.silenteight.proto.agent.name.v1.api.WatchlistName;
import com.silenteight.searpayments.scb.etl.response.HitAndWatchlistPartyData;
import com.silenteight.searpayments.scb.etl.response.HitData;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@RequiredArgsConstructor
class CreateCompareNamesRequest {

  private static final String INSTANCE_NAME_ADVNAME_INDV = "advname-indv";
  private static final String INSTANCE_NAME_ADVNAME_ORG = "advname-org";
  @NonNull private final HitData requestHitDto;

  private static final JsonFormat.Printer PROTO_JSON_PRINTER =
      JsonFormat.printer().includingDefaultValueFields().omittingInsignificantWhitespace();

  String create() {
    CompareNamesRequest compareNamesRequest = CompareNamesRequest.newBuilder()
        .setInstanceName(getInstanceName())
        .setExcludeReason(false)
        .addAllInputs(List.of(buildCompareNamesInput()))
        .build();

    return serializeCompareNamesRequest(compareNamesRequest);
  }

  @NotNull
  private CompareNamesInput buildCompareNamesInput() {
    return CompareNamesInput.newBuilder()
        .addAllAlertedNames(getNamesIfExist())
        .addWatchlistNames(WatchlistName.newBuilder()
            .setName(Optional.of(requestHitDto)
                .map(HitData::getHitAndWlPartyData)
                .map(HitAndWatchlistPartyData::getName)
                .orElse(""))
            .setType(requestHitDto.getHitAndWlPartyData().getWatchlistType().getName())
            .build())
        .addAllMatchingTexts(
            requestHitDto.getHitAndWlPartyData().getAllMatchingTexts())
        .build();
  }

  private List<String> getNamesIfExist() {
    List<String> names = requestHitDto.getAlertedPartyData().getNames();
    return names.isEmpty() ? emptyList() : names;
  }

  @NotNull
  private String getInstanceName() {
    WatchlistType watchlistType = requestHitDto.getHitAndWlPartyData().getWatchlistType();
    switch (watchlistType) {
      case INDIVIDUAL:
        return INSTANCE_NAME_ADVNAME_INDV;
      case ADDRESS:
      case COMPANY:
      case VESSEL:
        return INSTANCE_NAME_ADVNAME_ORG;
      default:
        throw new IllegalArgumentException("watchlistType " + watchlistType + " not supported");
    }
  }

  private String serializeCompareNamesRequest(CompareNamesRequest request) {
    try {
      return PROTO_JSON_PRINTER.print(request);
    } catch (InvalidProtocolBufferException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
