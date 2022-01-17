package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentRequest;
import com.silenteight.payments.bridge.agents.model.SpecificTermsRequest;
import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Value
@Builder
@Slf4j
public class EtlHit {

  AlertedPartyData alertedPartyData;

  Map<AlertedPartyKey, String> alertedPartyEntities;

  List<String> allMatchingFields;

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

  public String getNameMatchedTexts() {
    return hitComposite.getFkcoVNameMatchedText();
  }

  public List<String> getMatchedNames() {
    return hitComposite.getMatchedNames();
  }

  public NameAddressCrossmatchAgentRequest toNameAddressCrossmatchAgentRequest() {
    return NameAddressCrossmatchAgentRequest.builder().alertPartyEntities(alertedPartyEntities)
        .watchlistName(hitComposite.getFirstWatchlistName().orElse(""))
        .watchlistCountry(hitComposite.getFkcoVListCountry())
        .watchlistType(hitComposite.getFkcoVListType())
        .build();
  }

  public SpecificTermsRequest toSpecificTermsRequest() {
    return SpecificTermsRequest
        .builder()
        .allMatchFieldsValue(StringUtils.join(allMatchingFields, ", "))
        .build();
  }

  public SolutionType getSolutionType() {
    var hitType = hitComposite.getFkcoVHitType();
    var solutionType = hitType.replace(" ", "_");

    if (!EnumUtils.isValidEnum(SolutionType.class, solutionType)) {
      log.warn("Solution type: {} is not valid", solutionType);
      return SolutionType.UNKNOWN;
    }
    return SolutionType.valueOf(hitType);
  }

  public Optional<String> getAccountNumberOrFirstName() {
    return alertedPartyData.getAccountNumberOrFirstName();
  }

  public Optional<String> getFirstAlertedPartyName() {
    return getAlertedPartyNames().stream().map(String::trim).findFirst();
  }
}
