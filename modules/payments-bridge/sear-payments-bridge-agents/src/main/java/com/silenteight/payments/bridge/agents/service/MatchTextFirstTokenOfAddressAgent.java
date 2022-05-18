package com.silenteight.payments.bridge.agents.service;

import lombok.NonNull;

import com.silenteight.payments.bridge.agents.model.MatchtextFirstTokenOfAddressAgentRequest;
import com.silenteight.payments.bridge.agents.model.MatchtextFirstTokenOfAddressAgentResponse;
import com.silenteight.payments.bridge.agents.port.MatchTextFirstTokenOfAddressUseCase;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.payments.bridge.agents.model.MatchtextFirstTokenOfAddressAgentResponse.NO;
import static com.silenteight.payments.bridge.agents.model.MatchtextFirstTokenOfAddressAgentResponse.YES;

@Service
class MatchTextFirstTokenOfAddressAgent implements MatchTextFirstTokenOfAddressUseCase {

  @NonNull
  public MatchtextFirstTokenOfAddressAgentResponse invoke(
      MatchtextFirstTokenOfAddressAgentRequest request) {

    return isMatchTextFirstTokenOfAddress(request.getMatchingTexts(), request.getAddresses()) ? YES
                                                                                              : NO;
  }

  static boolean isMatchTextFirstTokenOfAddress(
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
