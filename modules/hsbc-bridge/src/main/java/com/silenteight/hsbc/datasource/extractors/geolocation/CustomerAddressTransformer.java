package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.Builder;
import lombok.Getter;

import com.silenteight.hsbc.datasource.extractors.geolocation.GeoLocationExtractor.SignType;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        .collect(Collectors.joining(SignType.SPACE.getSign()));
  }

  public String getAddressWithCountry() {
    return addresses
        .stream()
        .map(this::addCountries)
        .collect(Collectors.joining(SignType.SPACE.getSign()));
  }

  private String removeNames(String address) {
    var allInOnePattern = Pattern.compile(names.stream().map(name -> String.format(WORD_PATTERN, name)).collect(
        Collectors.joining("|")), Pattern.CASE_INSENSITIVE);
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
        .map(country -> String.format(" %s", country))
        .collect(Collectors.joining("", address, ""));
  }

  private boolean containsCountry(String address, String country) {
    return Pattern.compile(String.format(WORD_PATTERN, country), Pattern.CASE_INSENSITIVE).matcher(address).find();
  }

}
