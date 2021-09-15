package com.silenteight.payments.bridge.svb.etl.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.svb.etl.model.AlertedPartyData;
import com.silenteight.payments.bridge.svb.etl.model.CreateAlertedPartyEntitiesRequest;
import com.silenteight.payments.bridge.svb.etl.model.MessageFieldStructure;
import com.silenteight.payments.bridge.svb.etl.port.CreateAlertedPartyEntitiesUseCase;
import com.silenteight.payments.bridge.svb.etl.util.StringUtil;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.silenteight.payments.bridge.agents.model.AlertedPartyKey.ALERTED_ADDRESS_KEY;
import static com.silenteight.payments.bridge.agents.model.AlertedPartyKey.ALERTED_COUNTRY_TOWN_KEY;
import static com.silenteight.payments.bridge.agents.model.AlertedPartyKey.ALERTED_NAME_KEY;
import static com.silenteight.payments.bridge.agents.model.AlertedPartyKey.NO_MATCH;
import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

@Service
@RequiredArgsConstructor
class CreateAlertPartyEntities implements CreateAlertedPartyEntitiesUseCase {

  public Map<AlertedPartyKey, String> create(CreateAlertedPartyEntitiesRequest request) {

    Map<AlertedPartyKey, String> alertPartyEntities = new HashMap<>();
    MessageFieldStructure messageFieldStructure =
        request.getMessageFieldStructure();

    Pattern textPattern;

    for (String matchingText : request.getAllMatchingText()) {

      matchingText = matchingText.strip();
      textPattern = getPattern(messageFieldStructure, matchingText);
      boolean noMatchCond = true;

      List<String> names = getNames(request.getAlertedPartyData());
      if (matchesPattern(textPattern, names)) {
        noMatchCond = false;
        alertPartyEntities.put(ALERTED_NAME_KEY, names.get(0));
      }

      List<String> addresses = getAddresses(request.getAlertedPartyData());
      if (matchesPattern(textPattern, addresses)) {
        alertPartyEntities.put(ALERTED_ADDRESS_KEY, addresses.get(0));
        noMatchCond = false;
      }

      List<String> ctryTowns = getCtryTowns(request.getAlertedPartyData());
      if (matchesPattern(textPattern, ctryTowns)) {
        noMatchCond = false;
        alertPartyEntities.put(ALERTED_COUNTRY_TOWN_KEY, ctryTowns.get(0));
      }

      if (noMatchCond) {
        if (!alertPartyEntities.containsKey(NO_MATCH)) {
          alertPartyEntities.put(NO_MATCH, matchingText);
        } else {
          var oldValues = alertPartyEntities.get(NO_MATCH);
          var newValues = oldValues.concat(format(", %s", matchingText));
          alertPartyEntities.put(NO_MATCH, newValues);
        }
      }
    }

    return alertPartyEntities;
  }

  @NotNull
  private static Pattern getPattern(
      MessageFieldStructure messageFieldStructure, String matchingText) {
    Pattern textPattern;
    String matchingTextToUpperCase = matchingText.toUpperCase();
    String matchingTextPattern = Pattern.quote(matchingTextToUpperCase);

    Matcher matcher = StringUtil.createMatcherFromLineBreakinPattern(matchingTextPattern);

    if (matcher.find()) {
      textPattern = StringUtil.getPattern(messageFieldStructure, matchingTextToUpperCase);
    } else {
      textPattern = compile(matchingTextPattern);
    }
    return textPattern;
  }

  private static List<String> getCtryTowns(AlertedPartyData alertedPartyData) {
    return alertedPartyData.getCtryTowns();
  }

  private static List<String> getAddresses(AlertedPartyData alertedPartyData) {
    return alertedPartyData.getAddresses();
  }

  private static List<String> getNames(AlertedPartyData alertedPartyData) {
    return alertedPartyData.getNames();
  }

  private static boolean matchesPattern(Pattern textPattern, List<String> location) {
    return location != null && !location.isEmpty() && textPattern.matcher(location.get(0)).find();
  }
}
