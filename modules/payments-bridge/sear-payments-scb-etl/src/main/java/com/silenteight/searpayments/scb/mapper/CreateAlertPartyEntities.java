package com.silenteight.searpayments.scb.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.scb.etl.response.AlertedPartyData;
import com.silenteight.searpayments.scb.etl.response.HitData;
import com.silenteight.searpayments.scb.etl.response.MessageFieldStructure;
import com.silenteight.searpayments.scb.util.StringUtil;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

@RequiredArgsConstructor
class CreateAlertPartyEntities {

  private static final String NO_MATCH = "NO_MATCH";
  @NonNull private final HitData requestHitDto;

  Map<String, String> create() {

    Map<String, String> alertPartyEntities = new HashMap<>();

    List<String> allMatchingTexts = requestHitDto.getHitAndWlPartyData().getAllMatchingTexts();

    AlertedPartyData alertedPartyData = requestHitDto.getAlertedPartyData();
    MessageFieldStructure messageFieldStructure =
        alertedPartyData.getMessageFieldStructure();

    Pattern textPattern;

    for (String matchingText : allMatchingTexts) {

      matchingText = matchingText.strip();
      textPattern = getPattern(messageFieldStructure, matchingText);
      boolean noMatchCond = true;

      List<String> names = getNames(alertedPartyData);
      if (matchesPattern(textPattern, names)) {
        noMatchCond = false;
        alertPartyEntities.put("NAME", names.get(0));
      }

      List<String> addresses = getAddresses(alertedPartyData);
      if (matchesPattern(textPattern, addresses)) {
        alertPartyEntities.put("ADDRESS", addresses.get(0));
        noMatchCond = false;
      }

      List<String> ctryTowns = getCtryTowns(alertedPartyData);
      if (matchesPattern(textPattern, ctryTowns)) {
        noMatchCond = false;
        alertPartyEntities.put("COUNTRY TOWN", ctryTowns.get(0));
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
