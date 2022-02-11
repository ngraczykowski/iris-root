package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.name.v1.NameFeatureInput.EntityType;
import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.agents.model.CompanyNameSurroundingRequest;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentRequest;
import com.silenteight.payments.bridge.agents.model.SpecificTermsRequest;
import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
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

  List<String> matchingTexts;

  List<String> allMatchFieldsValue;

  List<String> matchedNames;

  List<String> matchedCountries;

  List<String> nameMatchedTexts;

  Map<AlertedPartyKey, String> alertedPartyEntity;

  WatchlistType watchlistType;

  String hitType;

  String ofacId;


  public NameAddressCrossmatchAgentRequest toCrossmatchRequest() {
    return NameAddressCrossmatchAgentRequest
        .builder()
        .alertPartyEntities(getAlertedPartyEntity())
        .watchlistName(getFirstWatchlistName().orElse(""))
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

  public List<String> getAlertedPartyNames() {
    return alertedPartyData.getNames();
  }

  public String getAlertedPartyLocation() {
    return alertedPartyData.getCtryTowns().stream().findFirst().orElse("");
  }

  public CompanyNameSurroundingRequest toCompanyNameSurroundingRequest() {
    return CompanyNameSurroundingRequest
        .builder()
        .allNames(getAlertedPartyNames())
        .build();
  }

  public Optional<String> getAccountNumberOrFirstName() {
    return alertedPartyData.getAccountNumberOrFirstName();
  }

  public List<String> getSearchCodes() {
    var codes = ObjectUtils.defaultIfNull(matchedNames, new ArrayList<String>());

    if (hitType.contains("EMBARGO")) {
      codes.addAll(matchedCountries);
    }

    return codes;
  }

  public SolutionType getSolutionType() {
    var solutionType = hitType.replace(" ", "_");

    if (!EnumUtils.isValidEnum(SolutionType.class, solutionType)) {
      log.warn("Solution type: {} is not valid", solutionType);
      return SolutionType.UNKNOWN;
    }
    return SolutionType.valueOf(solutionType);
  }

  public Optional<String> getFirstAlertedPartyName() {
    return getAlertedPartyNames().stream().map(String::trim).findFirst();
  }

  private Optional<String> getFirstWatchlistName() {
    return getWatchlistNames().stream().findFirst();
  }
}
