package com.silenteight.payments.bridge.agents;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public class MatchtextFirstTokenOfAddressAgentHelper {

  public static boolean isMatchtextFirstTokenOfAddress(
      @NonNull List<String> matchingTexts, @NonNull List<String> addresses) {
    List<String> strippedAndUpperMatchingTexts = matchingTexts.stream()
        .map(String::strip)
        .map(String::toUpperCase)
        .collect(Collectors.toList());

    List<String> strippedAndUpperAddresses = addresses.stream()
        .map(String::strip)
        .map(String::toUpperCase)
        .collect(Collectors.toList());

    for (String matchingText : strippedAndUpperMatchingTexts) {
      for (String address : strippedAndUpperAddresses) {
        if (address.startsWith(matchingText)) {
          return true;
        }
      }
    }
    return false;
  }

}
