package com.silenteight.hsbc.datasource.extractors.name;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class CountryLanguageMatcher {

  private static final List<String> COUNTRIES = List.of(
      "CN", "CHINA",
      "HK", "HONG KONG",
      "SG", "SINGAPORE",
      "TW", "TAIWAN");

  private static final List<String> LANGUAGES = List.of("zh(-.+)?");

  static boolean matches(List<String> languages, List<String> countries) {
    if ((countries != null && !countries.isEmpty()) &&
        (languages != null && !languages.isEmpty())) {

      var countryPatterns = compilePatterns(COUNTRIES, true, true);
      var languagePatterns = compilePatterns(LANGUAGES, true, true);

      var countriesMatches = anyMatches(countryPatterns, countries);
      var languagesMatches = anyMatches(languagePatterns, languages);

      return countriesMatches && languagesMatches;
    }
    return false;
  }

  private static boolean anyMatches(List<Pattern> patterns, List<String> values) {
    for (var pattern : patterns) {
      for (var value : values) {
        if (value != null && pattern.matcher(value).matches()) {
          return true;
        }
      }
    }
    return false;
  }

  private static List<Pattern> compilePatterns(
      List<String> groups, boolean hasBoundaries, boolean noCaseIntense) {
    return addPatterns(groups, hasBoundaries, noCaseIntense);
  }

  private static List<Pattern> addPatterns(
      List<String> groups, boolean hasBoundaries, boolean noCaseIntense) {
    var patterns = new ArrayList<Pattern>();

    groups.stream()
        .filter(Objects::nonNull)
        .forEach(regex -> {
          if (hasBoundaries && noCaseIntense) {
            patterns.add(Pattern.compile("\\b" + regex + "\\b", Pattern.CASE_INSENSITIVE));
          } else if (hasBoundaries) {
            patterns.add(Pattern.compile("\\b" + regex + "\\b"));
          } else if (noCaseIntense) {
            patterns.add(Pattern.compile(regex, Pattern.CASE_INSENSITIVE));
          } else {
            patterns.add(Pattern.compile(regex));
          }
        });
    return patterns;
  }
}
