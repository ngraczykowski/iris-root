package com.silenteight.searpayments.scb.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.agent.name.v1.api.CompareFreeTextRequest;
import com.silenteight.searpayments.scb.etl.response.HitAndWatchlistPartyData;
import com.silenteight.searpayments.scb.etl.response.HitData;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@RequiredArgsConstructor
class CreateCompareFreeTextRequest {

  @NonNull
  private final HitData requestHitDto;

  private static final JsonFormat.Printer PROTO_JSON_PRINTER =
      JsonFormat.printer().includingDefaultValueFields().omittingInsignificantWhitespace();

  String create() {
    CompareFreeTextRequest compareFreeTextRequest = CompareFreeTextRequest.newBuilder()
        .setMatchedType(requestHitDto.getHitAndWlPartyData().getWatchlistType().getName())
        .setFreetext(extractHitAndWatchlistPartyData())
        .addAllMatchingTexts(
            requestHitDto.getHitAndWlPartyData().getAllMatchingTexts())
        .build();

    return serializeCompareFreeTextRequest(compareFreeTextRequest);
  }

  @NotNull
  private String extractHitAndWatchlistPartyData() {
    return Optional.of(requestHitDto)
        .map(HitData::getHitAndWlPartyData)
        .map(HitAndWatchlistPartyData::getFieldValue)
        .orElse("");
  }

  private String serializeCompareFreeTextRequest(CompareFreeTextRequest request) {
    try {
      return PROTO_JSON_PRINTER.print(request);
    } catch (InvalidProtocolBufferException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
