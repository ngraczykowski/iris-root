package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.Value;

import org.apache.commons.collections4.list.SetUniqueList;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
}
