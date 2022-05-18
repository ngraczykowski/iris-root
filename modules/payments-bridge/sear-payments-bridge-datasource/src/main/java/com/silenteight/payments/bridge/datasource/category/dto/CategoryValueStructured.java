package com.silenteight.payments.bridge.datasource.category.dto;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;

import java.util.List;
import java.util.Map;

@Value
@Builder
public class CategoryValueStructured {

  String matchName;

  String alertName;

  AlertedData alertedData;

  WatchlistData watchlistData;

  @Value
  @Builder
  public static class AlertedData {

    List<String> names;

    String accountNumber;

    Map<AlertedPartyKey, String> alertPartyEntities;
  }

  @Value
  @Builder
  public static class WatchlistData {

    String ofacId;

    String watchlistName;

    String watchlistType;

    String country;
  }

}
