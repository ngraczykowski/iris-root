package com.silenteight.searpayments.scb.mapper;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class NameAddressCrossmatchAgentRequest {

  private final Map<String, String> alertPartyEntities;
  private final String watchlistName;
  private final String watchlistCountry;
  private final String watchlistType;
}
