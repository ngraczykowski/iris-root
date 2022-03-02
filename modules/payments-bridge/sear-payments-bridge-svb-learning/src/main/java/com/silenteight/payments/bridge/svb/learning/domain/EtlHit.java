package com.silenteight.payments.bridge.svb.learning.domain;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.agents.model.CompanyNameSurroundingRequest;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentRequest;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.silenteight.payments.bridge.svb.learning.domain.WatchlistTerms.EMBARGO;
import static com.silenteight.payments.bridge.svb.learning.domain.WatchlistTerms.NA;
import static com.silenteight.payments.bridge.svb.learning.domain.WatchlistTerms.WILDCARD;

@Value
@Builder
@Slf4j
public class EtlHit {

  AlertedPartyData alertedPartyData;

  Map<AlertedPartyKey, String> alertedPartyEntities;

  HitComposite hitComposite;

  public String getMatchId() {
    return hitComposite.getMatchId();
  }

  public String getWatchlistLocation() {
    return hitComposite.getWatchlistLocation();
  }

  public String getAlertedPartyLocation() {
    return alertedPartyData.getCtryTowns().stream().findFirst().orElse("");
  }

  public String getMatchedTagContent() {
    return hitComposite.getFkcoVMatchedTagContent();
  }

  public String getFkcoVListFmmId() {
    return hitComposite.getFkcoVListFmmId();
  }

  public List<String> getMatchingTexts() {
    return hitComposite.getMatchingTexts();
  }

  public List<String> getSearchCodes() {
    return hitComposite.getSearchCodes();
  }

  public List<String> getWatchlistNames() {
    return hitComposite.getWatchlistNames();
  }

  public List<String> getAlertedPartyNames() {
    return alertedPartyData.getNames();
  }

  public WatchlistType getWatchlistType() {
    return hitComposite.getWatchlistType();
  }

  public NameAddressCrossmatchAgentRequest toNameAddressCrossmatchAgentRequest() {
    return NameAddressCrossmatchAgentRequest.builder()
        .alertPartyEntities(alertedPartyEntities)
        .watchlistName(getWlName())
        .watchlistCountry(hitComposite.getFkcoVListCountry())
        .watchlistType(hitComposite.getFkcoVListType())
        .build();
  }

  public Optional<String> getAccountNumberOrFirstName() {
    return alertedPartyData.getAccountNumberOrFirstName();
  }

  public Optional<String> getFirstAlertedPartyName() {
    return getAlertedPartyNames().stream().map(String::trim).findFirst();
  }

  public CompanyNameSurroundingRequest toCompanyNameSurroundingRequest() {
    return CompanyNameSurroundingRequest
        .builder()
        .allNames(getAlertedPartyNames())
        .build();
  }

  public String getTag() {
    return hitComposite.getFkcoVMatchedTag();
  }

  private String getWlName() {
    if (hitComposite.getFkcoVHitType().equals(EMBARGO)) {

      List<String> listOfNames = Arrays.asList(
          hitComposite.getFirstWatchlistName().orElse(""),
          hitComposite.getFkcoVListCity(),
          hitComposite.getFkcoVListState(),
          hitComposite.getFkcoVListCountry());

      return listOfNames.stream()
          .filter(EtlHit::isElemNotNotBlankAndNotContainsSpecificCharacters)
          .findFirst()
          .orElse("");
    } else {
      return hitComposite.getFkcoVListName();
    }
  }

  private static boolean isElemNotNotBlankAndNotContainsSpecificCharacters(String elem) {
    return StringUtils.isNotBlank(elem) &&
        !WILDCARD.equals(elem) &&
        !NA.equals(elem);
  }
}
