package com.silenteight.scb.ingest.adapter.incomming.common.gnsparty;

import com.silenteight.scb.ingest.adapter.incomming.common.validation.ChineseCharactersValidator;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.ScreenableData;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Strings.nullToEmpty;

public class ChineseNamesResolver {

  public static Set<String> getChineseNames(ScreenableData data) {
    return toValidNames(List.of(
        nullToEmpty(data.getSupplementaryInformation1()),
        nullToEmpty(data.getAlternateName1())
    ));
  }

  public static Set<String> getChineseNames(GnsParty gnsParty) {
    return toValidNames(List.of(
        nullToEmpty((String) gnsParty.getValue("supplementaryInformation1")),
        nullToEmpty((String) gnsParty.getValue("alternateName1"))
    ));
  }

  private static Set<String> toValidNames(List<String> names) {
    return names.stream()
        .filter(ChineseCharactersValidator::isValid)
        .collect(Collectors.toSet());
  }
}
