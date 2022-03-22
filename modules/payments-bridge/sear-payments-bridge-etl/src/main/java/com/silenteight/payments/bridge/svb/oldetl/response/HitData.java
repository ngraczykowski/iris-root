package com.silenteight.payments.bridge.svb.oldetl.response;

import lombok.Value;

import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueStructured;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueStructured.AlertedData;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueStructured.CategoryValueStructuredBuilder;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueStructured.WatchlistData;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Value
public class HitData {

  String matchId;
  AlertedPartyData alertedPartyData;
  HitAndWatchlistPartyData hitAndWlPartyData;

  public Optional<String> getAlertedPartyAccountNumberOrFirstName() {
    return alertedPartyData.getAccountNumberOrFirstName();
  }

  public String getTag() {
    return hitAndWlPartyData.getTag();
  }

  public String getFieldValue() {
    return Optional.ofNullable(hitAndWlPartyData.getTag())
        .orElse("");
  }

  public String getMatchingText() {
    return Optional.ofNullable(hitAndWlPartyData.getMatchingText())
        .orElse("");
  }

  public SolutionType getSolutionType() {
    return hitAndWlPartyData.getSolutionType();
  }

  public List<String> getSearchCodes() {
    return Optional
        .ofNullable(hitAndWlPartyData.getSearchCodes())
        .orElseGet(Collections::emptyList);
  }

  public List<String> getPassports() {
    return Optional.ofNullable(hitAndWlPartyData.getPassports())
        .orElseGet(Collections::emptyList);
  }

  public List<String> getNatIds() {
    return Optional.ofNullable(hitAndWlPartyData.getNatIds())
        .orElseGet(Collections::emptyList);
  }

  public List<String> getBics() {
    return Optional.ofNullable(hitAndWlPartyData.getBics())
        .orElseGet(Collections::emptyList);
  }

  public CategoryValueStructuredBuilder toCategoryValueStructuredModelBuilder(
      Map<AlertedPartyKey, String> alertedPartyEntities) {

    var alertedData = createAlertedData(alertedPartyEntities);
    var watchlistData = createWatchlistData();

    return CategoryValueStructured.builder()
        .alertedData(alertedData)
        .watchlistData(watchlistData);
  }

  private AlertedData createAlertedData(Map<AlertedPartyKey, String> alertedPartyEntities) {
    return AlertedData.builder()
        .names(getAlertedPartyData().getNames())
        .accountNumber(getAlertedPartyData().getAccountNumber())
        .alertPartyEntities(alertedPartyEntities)
        .build();
  }

  private WatchlistData createWatchlistData() {
    return WatchlistData.builder()
        .ofacId(hitAndWlPartyData.getId())
        .watchlistName(hitAndWlPartyData.getName())
        .watchlistType(hitAndWlPartyData.getWatchlistType().toString())
        .country(getWatchlistCountryIfExists())
        .build();
  }

  private String getWatchlistCountryIfExists() {
    var countries = hitAndWlPartyData.getCountries();
    return countries.isEmpty() ? "" : countries.get(0);
  }

}
