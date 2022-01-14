package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.apache.commons.collections4.list.SetUniqueList;
import org.apache.commons.lang3.ObjectUtils;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Value
@Builder
public class HitComposite {

  long hitId;

  String fkcoVMatchedTag;

  String fkcoISequence;

  String fkcoVListFmmId;

  String fkcoVListCity;

  String fkcoVListState;

  String fkcoVListCountry;

  String fkcoVMatchedTagContent;

  String fkcoVCityMatchedText;

  String fkcoVStateMatchedText;

  String fkcoVCountryMatchedText;

  String fkcoVAddressMatchedText;

  String fkcoVListMatchedName;

  String fkcoVHitType;

  String fkcoVListType;

  String fkcoVListName;

  String fkcoVNameMatchedText;

  public String getMatchId() {
    return fkcoVListFmmId + "(" + fkcoVMatchedTag + ", #" + fkcoISequence
        + ")";
  }

  public String getWatchlistLocation() {
    return String.join(", ", List.of(fkcoVListCountry, fkcoVListState, fkcoVListCity));
  }

  public List<String> getMatchingTexts() {
    var matchingText = SetUniqueList.setUniqueList(new ArrayList<String>());

    matchingText.add(fkcoVCityMatchedText);
    matchingText.add(fkcoVAddressMatchedText);
    matchingText.add(fkcoVStateMatchedText);
    matchingText.add(fkcoVCountryMatchedText);

    return matchingText
        .stream()
        .filter(s -> !s.equalsIgnoreCase("N/A"))
        .collect(toList());
  }

  public List<String> getSearchCodes() {
    var matchedNames = new ArrayList<>(Arrays.asList(fkcoVListMatchedName.split(",")));
    var codes = ObjectUtils.defaultIfNull(matchedNames, new ArrayList<String>());

    if (fkcoVHitType.contains("EMBARGO")) {
      codes.addAll(List.of(fkcoVListCountry.split(",")));
    }

    return codes;
  }

  public List<String> getWatchlistNames() {
    return List.of(getFkcoVListName().split(","));
  }

  public WatchlistType getWatchlistType() {
    return WatchlistType.ofCode(fkcoVListType);
  }

  public List<String> getMatchedNames() {
    return new ArrayList<>(Arrays.asList(fkcoVListMatchedName.split(",")));
  }

  public EtlHit toEtlHit(
      AlertedPartyData alertedPartyData, Map<AlertedPartyKey, String> alertedPartyEntities,
      List<String> allMatchingFields) {
    return EtlHit
        .builder()
        .hitComposite(this)
        .alertedPartyData(alertedPartyData)
        .alertedPartyEntities(alertedPartyEntities)
        .allMatchingFields(allMatchingFields)
        .build();
  }

  Optional<String> getFirstWatchlistName() {
    return getWatchlistNames().stream().findFirst();
  }
}
