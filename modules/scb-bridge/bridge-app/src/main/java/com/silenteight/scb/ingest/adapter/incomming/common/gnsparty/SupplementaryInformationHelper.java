package com.silenteight.scb.ingest.adapter.incomming.common.gnsparty;

import com.silenteight.scb.ingest.adapter.incomming.common.validation.ChineseCharactersValidator;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.ScreenableData;

import java.util.Optional;

import static com.google.common.base.Strings.nullToEmpty;
import static java.util.Optional.empty;
import static java.util.Optional.of;

public class SupplementaryInformationHelper {

  private final String supplementaryInformation1;

  public SupplementaryInformationHelper(ScreenableData data) {
    supplementaryInformation1 = nullToEmpty(data.getSupplementaryInformation1());
  }

  public SupplementaryInformationHelper(GnsParty gnsParty) {
    supplementaryInformation1 =
        nullToEmpty((String) gnsParty.getValue("supplementaryInformation1"));
  }

  public Optional<String> getChineseNameFromSupplementaryInformation1() {
    if (containsChineseCharactersOnly()) {
      return of(supplementaryInformation1);
    }

    return empty();
  }

  private boolean containsChineseCharactersOnly() {
    return ChineseCharactersValidator.isValid(supplementaryInformation1);
  }
}