package com.silenteight.fab.dataprep.domain.feature;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class AdditionalInfoHelper {

  //TODO handle values with slash
  static String getValue(String additionalInfo, String fieldName) {
    Pattern pattern = Pattern.compile("^([^/]+/)*\\s*" + fieldName + ":([^/]+).*");
    Matcher matcher = pattern.matcher(additionalInfo);
    return matcher.matches() ? matcher.group(2).trim() : "";
  }
}
