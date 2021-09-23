package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

import com.silenteight.datasource.api.name.v1.NameFeatureInput.EntityType;
import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentRequest;
import com.silenteight.payments.bridge.svb.etl.model.AlertedPartyData;
import com.silenteight.payments.bridge.svb.etl.model.MessageFieldStructure;

import java.util.List;
import java.util.Map;

@Value
@Builder
public class LearningMatch {

  AlertedPartyData alertedPartyData;

  String matchId;

  @Setter
  @NonFinal
  String matchName;

  List<String> watchlistNames;

  EntityType entityType;

  String watchlistLocation;

  String watchlistCountry;

  String matchedFieldValue;

  String matchNameSynonym;

  String matchType;

  List<String> matchingTexts;

  Map<AlertedPartyKey, String> alertedPartyEntity;


  public NameAddressCrossmatchAgentRequest toCrossmatchRequest() {
    return NameAddressCrossmatchAgentRequest
        .builder()
        .alertPartyEntities(getAlertedPartyEntity())
        .watchlistName(getWatchlistNames().get(0))
        .watchlistCountry(getWatchlistCountry())
        .watchlistType(getMatchType())
        .build();
  }

  public String toName(String alert) {
    return "alerts/" + alert + "/matches/" + getMatchName();
  }

  public List<String> getAlertedPartyNames() {
    return alertedPartyData.getNames();
  }

  public String getAlertedPartyLocation() {
    return alertedPartyData.getAddresses().stream().findFirst().orElse("");
  }

  public MessageFieldStructure getMessageFieldStructure() {
    return alertedPartyData.getMessageFieldStructure();
  }
}
