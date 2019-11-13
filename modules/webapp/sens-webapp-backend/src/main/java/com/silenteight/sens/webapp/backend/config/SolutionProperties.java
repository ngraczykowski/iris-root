package com.silenteight.sens.webapp.backend.config;

import lombok.Data;

import com.silenteight.sens.webapp.common.util.StringPredicate;
import com.silenteight.sens.webapp.common.util.WhitelistPredicate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Data
class SolutionProperties {

  private static final String VALUES_SEPARATOR = ",";
  private static final Pattern PATTERN = Pattern.compile(VALUES_SEPARATOR);

  private Map<String, String> predicates = new HashMap<>();

  private boolean syncCheck;
  private String outdatedMessagePrefix = "";

  Map<String, StringPredicate> getExposedAlertFilterMap() {
    return predicates
        .entrySet()
        .stream()
        .collect(toMap(Entry::getKey, entry -> parseValue(entry.getValue())));
  }

  private static WhitelistPredicate parseValue(String entry) {
    return new WhitelistPredicate(PATTERN
        .splitAsStream(entry)
        .map(String::trim)
        .map(String::toUpperCase)
        .collect(Collectors.toList()));
  }
}
