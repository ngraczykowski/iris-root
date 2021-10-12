package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

import com.silenteight.datasource.api.name.v1.NameFeatureInput.EntityType;
import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentRequest;
import com.silenteight.payments.bridge.agents.model.OneLinerAgentRequest;
import com.silenteight.payments.bridge.agents.model.SpecificTermsRequest;
import com.silenteight.payments.bridge.svb.etl.model.AbstractMessageStructure;
import com.silenteight.payments.bridge.svb.etl.model.GetAccountNumberRequest;
import com.silenteight.payments.bridge.svb.etl.response.AlertedPartyData;

import org.apache.commons.lang3.StringUtils;

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

  String messageData;

  String applicationCode;

  String hitTag;

  AbstractMessageStructure messageStructure;

  List<String> matchingTexts;

  List<String> allMatchFieldsValue;

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

  public SpecificTermsRequest toSpecificTermsRequest() {
    return SpecificTermsRequest
        .builder()
        .allMatchFieldsValue(StringUtils.join(allMatchFieldsValue, ", "))
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

  public List<String> getAddresses() {
    return alertedPartyData.getAddresses();
  }

  public OneLinerAgentRequest toOneLinerAgentRequest() {
    return OneLinerAgentRequest
        .builder()
        .noAcctNumFlag(alertedPartyData.isNoAcctNumFlag())
        .noOfLines(alertedPartyData.getNumOfLines())
        .messageLength(alertedPartyData.getMessageLength())
        .build();
  }

  public GetAccountNumberRequest toGetAccountNumberRequest() {
    return GetAccountNumberRequest
        .builder()
        .applicationCode(applicationCode)
        .tag(hitTag)
        .message(messageData)
        .matchingFields(allMatchFieldsValue)
        .build();
  }
}
