package com.silenteight.payments.bridge.datasource.agent.dto;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.common.dto.common.WatchlistType;

import java.util.List;

@Value
@Builder
public class FeatureInputStructured {

  String matchName;

  String alertName;

  GeoAgentData geoAgentData;

  NameAgentData nameAgentData;

  IdentificationMismatchAgentData identificationMismatchAgentData;

  HistoricalAgentData historicalAgentData;


  @Value
  @Builder
  public static class NameAgentData {

    WatchlistType watchlistType;

    List<String> alertedPartyNames;

    List<String> watchlistPartyName;

    List<String> matchingTexts;
  }

  @Value
  @Builder
  public static class GeoAgentData {

    String alertedPartyLocation;

    String watchListLocation;
  }

  @Value
  @Builder
  public static class IdentificationMismatchAgentData {

    String alertedPartyMatchingField;

    String matchingText;

    List<String> watchlistSearchCodes;

  }

  @Value
  @Builder
  public static class HistoricalAgentData {

    String matchId;

    String ofacId;

    WatchlistType watchlistType;

    String accountNumber;

    String customerName;

  }

}
