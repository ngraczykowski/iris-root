package com.silenteight.customerbridge.common.gnsparty;

import com.silenteight.customerbridge.common.validation.ChineseCharactersValidator;
import com.silenteight.customerbridge.gnsrt.model.request.ScreenableData;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Strings.nullToEmpty;
import static java.util.Optional.empty;
import static java.util.Optional.of;

public class SupplementaryInformationHelper {

  private static final List<String> SOURCE_SYSTEM_VALID_VALUES =
      List.of("CUPD", "EBBS", "IBNK", "PANDA");

  private final String supplementaryInformation1;
  private final String sourceSystemField;
  private final String bookingLocationField;

  public SupplementaryInformationHelper(ScreenableData data) {
    supplementaryInformation1 = nullToEmpty(data.getSupplementaryInformation1());
    sourceSystemField = nullToEmpty(data.getSourceSystemIdentifier());
    bookingLocationField = data.getAmlCountry();
  }

  public SupplementaryInformationHelper(GnsParty gnsParty) {
    supplementaryInformation1 =
        nullToEmpty((String) gnsParty.getValue("supplementaryInformation1"));
    sourceSystemField = nullToEmpty((String) gnsParty.getValue("sourceSystemIdentifier"));
    bookingLocationField = (String) gnsParty.getValue("bookingLocation");
  }

  public Optional<String> getChineseNameFromSupplementaryInformation1() {
    if (containsChineseCharactersOnly() && hasProperSourceSystemAndBookingLocation()) {
      return of(supplementaryInformation1);
    }

    return empty();
  }

  private boolean hasProperSourceSystemAndBookingLocation() {
    return (isTndmSourceSystem() && isBookingLocationEqualTo("TW")) ||
        (isSourceSystemBelongsToCnGroup() && isBookingLocationEqualTo("CN"));
  }

  private boolean isSourceSystemBelongsToCnGroup() {
    return SOURCE_SYSTEM_VALID_VALUES.contains(sourceSystemField.toUpperCase());
  }

  private boolean isTndmSourceSystem() {
    return "TNDM".equalsIgnoreCase(sourceSystemField);
  }

  private boolean isBookingLocationEqualTo(String value) {
    return value.equalsIgnoreCase(bookingLocationField);
  }

  private boolean containsChineseCharactersOnly() {
    return ChineseCharactersValidator.isValid(supplementaryInformation1);
  }
}