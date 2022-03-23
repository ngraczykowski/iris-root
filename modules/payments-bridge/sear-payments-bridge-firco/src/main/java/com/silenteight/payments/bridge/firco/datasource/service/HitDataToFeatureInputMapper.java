package com.silenteight.payments.bridge.firco.datasource.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured.GeoAgentData;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured.HistoricalAgentData;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured.NameAgentData;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
class HitDataToFeatureInputMapper {

  private final String matchId;
  private final AlertedPartyData alertedPartyData;
  private final HitAndWatchlistPartyData hitAndWlPartyData;

  FeatureInputStructured mapToFeatureInputStructured(String alertName, String matchName) {
    return FeatureInputStructured.builder()
        .alertName(alertName)
        .matchName(matchName)
        .geoAgentData(getGeoAgentData())
        .nameAgentData(getNameAgentData())
        .historicalAgentData(getHistoricalAgentData())
        .build();
  }

  private GeoAgentData getGeoAgentData() {
    return GeoAgentData.builder()
        .alertedPartyLocation(getCountryTown())
        .watchListLocation(getWatchListLocation())
        .build();
  }

  private String getCountryTown() {
    return getSpecifiedAlertedPartyLocation(
        alertedPartyData, AlertedPartyData::getCtryTowns);
  }

  private static String getSpecifiedAlertedPartyLocation(
      AlertedPartyData alertedPartyData,
      Function<AlertedPartyData, List<String>> getSpecifiedLocation) {
    return Optional.of(alertedPartyData)
        .map(getSpecifiedLocation)
        .orElseGet(Collections::emptyList)
        .stream()
        .findFirst()
        .orElse("");
  }

  private String getWatchListLocation() {
    var country = getSpecifiedWatchListLocation(HitAndWatchlistPartyData::getCountries);
    var state = getSpecifiedWatchListLocation(HitAndWatchlistPartyData::getStates);
    var city = getSpecifiedWatchListLocation(HitAndWatchlistPartyData::getCities);
    return String.join(", ", country, state, city);
  }

  private String getSpecifiedWatchListLocation(
      Function<HitAndWatchlistPartyData, List<String>> getSpecifiedLocation) {
    return Optional.of(hitAndWlPartyData)
        .map(getSpecifiedLocation)
        .orElseGet(Collections::emptyList)
        .stream()
        .findFirst()
        .orElse("");
  }

  private NameAgentData getNameAgentData() {
    return NameAgentData.builder()
        .watchlistType(hitAndWlPartyData.getWatchlistType())
        .alertedPartyNames(alertedPartyData.getNames())
        .watchlistPartyName(List.of(hitAndWlPartyData.getName()))
        .matchingTexts(hitAndWlPartyData.getAllMatchingTexts())
        .build();
  }

  private String getFieldValue() {
    var tag = hitAndWlPartyData.getTag();
    return tag == null ? "" : tag;
  }

  private String getMatchingText() {
    var matchingText = hitAndWlPartyData.getMatchingText();
    return matchingText == null ? "" : matchingText;
  }

  private List<String> setWatchlistSearchCodes() {
    switch (getSolutionType()) {
      case SEARCH_CODE:
        return getSearchCodes();
      case PASSPORT:
        return getPassports();
      case NATIONAL_ID:
        return getNatIds();
      case BIC:
        return getBics();
      default:
        return Collections.emptyList();
    }
  }

  private SolutionType getSolutionType() {
    return hitAndWlPartyData.getSolutionType();
  }

  private List<String> getSearchCodes() {
    return Optional
        .ofNullable(hitAndWlPartyData.getSearchCodes())
        .orElseGet(Collections::emptyList);
  }

  private List<String> getPassports() {
    return Optional.ofNullable(hitAndWlPartyData.getPassports())
        .orElseGet(Collections::emptyList);
  }

  private List<String> getNatIds() {
    return Optional.ofNullable(hitAndWlPartyData.getNatIds())
        .orElseGet(Collections::emptyList);
  }

  private List<String> getBics() {
    return Optional.ofNullable(hitAndWlPartyData.getBics())
        .orElseGet(Collections::emptyList);
  }

  private HistoricalAgentData getHistoricalAgentData() {
    return HistoricalAgentData.builder()
        .matchId(matchId)
        .ofacId(getOfacId())
        .watchlistType(hitAndWlPartyData.getWatchlistType())
        .accountNumber(getAccountNumber())
        .customerName(getCustomerName())
        .build();
  }


  private String getOfacId() {
    return hitAndWlPartyData.getId().toUpperCase().trim();
  }

  private String getCustomerName() {
    return alertedPartyData.getNames().stream()
        .map(String::trim)
        .findFirst()
        .orElse("");
  }

  private String getAccountNumber() {
    var accountNumber = alertedPartyData.getAccountNumber();
    if (StringUtils.isBlank(accountNumber)) {
      return "";
    }
    return accountNumber.toUpperCase().trim();
  }
}
