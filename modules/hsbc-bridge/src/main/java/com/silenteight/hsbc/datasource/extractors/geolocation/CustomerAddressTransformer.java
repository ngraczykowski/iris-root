package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static com.silenteight.hsbc.datasource.extractors.geolocation.GeoLocationExtractor.SignType.SPACE;
import static java.lang.String.format;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;

@Builder
@Getter
class CustomerAddressTransformer {

  private static final String WORD_PATTERN = "\\b%s\\b";

  private List<String> names;
  private List<String> addresses;
  private List<String> countries;

  public String getAddressWithoutNamesAndWithCountries() {
    return addresses.stream()
        .map(this::removeNames)
        .map(this::addCountries)
        .distinct()
        .map(GeoLocationExtractor::stripAndUpper)
        .collect(joining(SPACE.getSign()));
  }

  public String getAddressWithCountry() {
    return addresses
        .stream()
        .map(this::addCountries)
        .collect(joining(SPACE.getSign()));
  }

  private String removeNames(String address) {
    var allInOnePattern = compile(names.stream().map(name -> format(WORD_PATTERN, name)).collect(joining("|")), CASE_INSENSITIVE);
    var allInOneMatcher = allInOnePattern.matcher(address);

    var sb = new StringBuilder();
    while (allInOneMatcher.find()) {
      allInOneMatcher.appendReplacement(sb, "");
    }
    allInOneMatcher.appendTail(sb);
    return sb.toString().strip().replaceAll("\\s+", " ");
  }

  private String addCountries(String address) {
    return countries.stream()
        .filter(country -> !containsCountry(address, country))
        .map(country -> format(" %s", country))
        .collect(joining("", address, ""));
  }

  private boolean containsCountry(String address, String country) {
    return compile(format(WORD_PATTERN, country), CASE_INSENSITIVE).matcher(address).find();
  }

}
