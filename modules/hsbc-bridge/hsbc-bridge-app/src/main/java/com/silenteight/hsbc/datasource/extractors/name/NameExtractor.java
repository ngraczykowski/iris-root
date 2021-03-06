package com.silenteight.hsbc.datasource.extractors.name;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.dto.name.ForeignAliasDto;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class NameExtractor {

  private static final Pattern EXTRACT_NAME =
      Pattern.compile("(?<firstNamePart>.*)(\\((?<alias>[^A-Za-z0-9]+)\\))(?<secondNamePart>.*)");
  private static final Pattern IS_ORIGINAL_SCRIPT = Pattern.compile("[^A-Za-z0-9]+");
  private static final Pattern IS_NUMERIC_SCRIPT_NAME = Pattern.compile("^[0-9]{4}(\\s+[0-9]{4})*$");

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
        .collect(Collectors.joining(" "));
  }

  static Stream<String> extractNameAndOriginalScriptAliases(String name) {
    var names = new ArrayList<String>();

    if (name != null && !name.strip().isEmpty()) {
      var matcher = EXTRACT_NAME.matcher(name);

      if (matcher.matches()) {
        var firstNamePart = matcher.group("firstNamePart").strip();
        var secondNamePart = matcher.group("secondNamePart").strip();
        var alias = matcher.group("alias").strip();

        names.add(String.join(" ", firstNamePart, secondNamePart).strip());
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
    return String.join(" ", names);
  }

  static List<String> collectCountryMatchingAliases(
      List<ForeignAliasDto> foreignAliases, List<String> countries) {
    var values = new ArrayList<String>();

    if (foreignAliases != null && !foreignAliases.isEmpty()) {
      foreignAliases.stream().filter(Objects::nonNull).forEach(foreignAlias -> {
        if (CountryLanguageMatcher.matches(List.of(foreignAlias.getLanguage()), countries)) {
          values.add(foreignAlias.getName());
        }
      });
    }
    return values;
  }

  static Party applyOriginalScriptEnhancements(List<String> alertedParty, List<String> matchParty, List<String> nnsParty) {
    var party = keepOnlyOriginalScriptNamesIfAvailable(alertedParty, matchParty, nnsParty);

    var apWithoutChineseNumeric =
        removeRedundantNumericChineseScript(party.getAlertedPartyIndividuals());

    var wlWithoutChineseNumeric =
        removeRedundantNumericChineseScript(party.getWatchlistPartyIndividuals());

    var nnsWithoutChineseNumeric =
        removeRedundantNumericChineseScript(party.getNnsIndividuals());

    return new Party(apWithoutChineseNumeric, wlWithoutChineseNumeric, nnsWithoutChineseNumeric);
  }

  private static Party keepOnlyOriginalScriptNamesIfAvailable(
      List<String> apNames, List<String> mpNames, List<String> nnsNames) {
    if (apNames != null && mpNames != null) {
      var apOriginalScriptNames =
          apNames.stream().filter(NameExtractor::isOriginalScript).collect(Collectors.toList());
      var mpOriginalScriptNames =
          mpNames.stream().filter(NameExtractor::isOriginalScript).collect(Collectors.toList());
      var nnsNamesOriginalScriptNames =
          nnsNames.stream().filter(NameExtractor::isOriginalScript).collect(Collectors.toList());

      if (!apOriginalScriptNames.isEmpty() && !mpOriginalScriptNames.isEmpty()) {
        return new Party(apOriginalScriptNames, mpOriginalScriptNames, nnsNamesOriginalScriptNames);
      }
    }
    return new Party(apNames, mpNames, nnsNames);
  }

  private static List<String> removeRedundantNumericChineseScript(List<String> names) {
    if (names == null || names.isEmpty()) {
      return Collections.emptyList();
    }

    if (names.stream().anyMatch((NameExtractor::isOriginalScript))) {
      var filteredNames = names.stream()
          .filter(Objects::nonNull)
          .filter(name -> !isNumericScriptName(name))
          .collect(Collectors.toList());

      if (!filteredNames.isEmpty()) {
        return filteredNames;
      }
    }
    return names.stream().filter(Objects::nonNull).collect(Collectors.toList());
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
