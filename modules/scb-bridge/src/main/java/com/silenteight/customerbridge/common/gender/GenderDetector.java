package com.silenteight.customerbridge.common.gender;

import java.util.List;

public class GenderDetector {

  private final NameSeparatingGenderDetector nameSeparatingGenderDetector;

  public GenderDetector() {
    nameSeparatingGenderDetector = NameSeparatingGenderDetector.getDefault();
  }

  public static String determineApGenderFromName(String typeOfRec, List<String> names) {
    if ("I".equals(typeOfRec))
      return TitleGenderDetector.detect(names).name();
    else
      return "UNKNOWN";
  }

  public String determineWlGenderFromName(String typeOfRec, List<String> names) {
    if ("I".equals(typeOfRec))
      return nameSeparatingGenderDetector.detect(names).name();
    else
      return "UNKNOWN";
  }
}