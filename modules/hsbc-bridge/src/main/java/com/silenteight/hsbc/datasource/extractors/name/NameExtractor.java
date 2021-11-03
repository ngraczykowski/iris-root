package com.silenteight.hsbc.datasource.extractors.name;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.extractors.name.CountryLanguageMatcher.matches;
import static com.silenteight.hsbc.datasource.util.StreamUtils.toDistinctList;
import static java.lang.String.join;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class NameExtractor {

  private static final Pattern EXTRACT_NAME =
      compile("(?<firstNamePart>.*)(\\((?<alias>[^A-Za-z0-9]+)\\))(?<secondNamePart>.*)");
  private static final Pattern IS_ORIGINAL_SCRIPT = compile("[^A-Za-z0-9]+");
  private static final Pattern IS_NUMERIC_SCRIPT_NAME = compile("^[0-9]{4}(\\s+[0-9]{4})*$");

  static Stream<String> collectNames(Stream<String> names) {
    var map = new LinkedHashMap<String, String>();
    names.forEach(name -> appendName(map, name));
    return map.values().stream();
  }

  static String joinNameParts(String... names) {
    if (names == null) {
      return "";
    }
    return Stream.of(names)
        .map(name -> (name == null) ? "" : name.strip())
        .filter(name -> !name.equals(""))
        .collect(joining(" "));
  }

  static Stream<String> extractNameAndOriginalScriptAliases(String name) {
    var names = new ArrayList<String>();

    if (name != null && !name.strip().isEmpty()) {
      var matcher = EXTRACT_NAME.matcher(name);

      if (matcher.matches()) {
        var firstNamePart = matcher.group("firstNamePart").strip();
        var secondNamePart = matcher.group("secondNamePart").strip();
        var alias = matcher.group("alias").strip();

        names.add(join(" ", firstNamePart, secondNamePart).strip());
        names.add(alias);
      } else {
        names.add(name);
      }
    }
    return names.stream();
  }

  static String removeUnnecessaryAsterisk(String originalName) {
    if (originalName == null) {
      return "";
    }
    var tmp = "";
    var names = originalName.split(" ");

    for (var i = 0; i < names.length; i++) {
      tmp = names[i].replaceFirst("^\\*+", "");
      tmp = tmp.replaceFirst("\\*+$", "");
      names[i] = tmp;
    }
    return join(" ", names);
  }

  static List<String> collectCountryMatchingAliases(
      List<ForeignAliasDto> foreignAliases, List<String> countries) {
    var values = new ArrayList<String>();

    if (foreignAliases != null && !foreignAliases.isEmpty()) {
      foreignAliases.stream().filter(Objects::nonNull).forEach(foreignAlias -> {
        if (matches(of(foreignAlias.getLanguage()), countries)) {
          values.add(foreignAlias.getName());
        }
      });
    }
    return values;
  }

  static Party applyOriginalScriptEnhancements(List<String> alertedParty, List<String> matchParty) {
    var party = keepOnlyOriginalScriptNamesIfAvailable(alertedParty, matchParty);

    var apWithoutChineseNumeric =
        removeRedundantNumericChineseScript(party.getAlertedPartyIndividuals());

    var wlWithoutChineseNumeric =
        removeRedundantNumericChineseScript(party.getWatchlistPartyIndividuals());

    return new Party(apWithoutChineseNumeric, wlWithoutChineseNumeric);
  }

  private static Party keepOnlyOriginalScriptNamesIfAvailable(
      List<String> apNames, List<String> mpNames) {
    if (apNames != null && mpNames != null) {
      var apOriginalScriptNames =
          apNames.stream().filter(NameExtractor::isOriginalScript).collect(toList());
      var mpOriginalScriptNames =
          mpNames.stream().filter(NameExtractor::isOriginalScript).collect(toList());

      if (!apOriginalScriptNames.isEmpty() && !mpOriginalScriptNames.isEmpty()) {
        return new Party(apOriginalScriptNames, mpOriginalScriptNames);
      }
    }
    return new Party(apNames, mpNames);
  }

  private static List<String> removeRedundantNumericChineseScript(List<String> names) {
    if (names == null || names.isEmpty()) {
      return emptyList();
    }

    if (names.stream().anyMatch((NameExtractor::isOriginalScript))) {
      var filteredNames = names.stream()
          .filter(Objects::nonNull)
          .filter(name -> !isNumericScriptName(name))
          .collect(toList());

      if (!filteredNames.isEmpty()) {
        return filteredNames;
      }
    }
    return names.stream().filter(Objects::nonNull).collect(toList());
  }

  private static boolean isOriginalScript(String name) {
    return name != null && IS_ORIGINAL_SCRIPT.matcher(name).matches();
  }

  private static boolean isNumericScriptName(String name) {
    return name != null && IS_NUMERIC_SCRIPT_NAME.matcher(name).matches();
  }

  private static void appendName(LinkedHashMap<String, String> map, String name) {
    if (name == null || name.strip().isEmpty()) {
      return;
    }

    var value = name.strip();
    var key = value.toLowerCase();

    if (!map.containsKey(key)) {
      map.put(key, value);
    } else {
      var currentPriority = detectCaseType(value).value;
      var previousPriority = detectCaseType(map.get(key)).value;

      if (currentPriority < previousPriority) {
        map.put(key, value);
      }
    }
  }

  private static NameCaseType detectCaseType(String value) {
    if (value.equals(value.toLowerCase())) {
      return NameCaseType.LOWER_CASE;
    } else if (value.equals(value.toUpperCase())) {
      return NameCaseType.UPPER_CASE;
    } else {
      return NameCaseType.MIXED_CASE;
    }
  }

  @RequiredArgsConstructor
  private enum NameCaseType {
    MIXED_CASE(0),
    UPPER_CASE(1),
    LOWER_CASE(2);

    private final int value;
  }
}
