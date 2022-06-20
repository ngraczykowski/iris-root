/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.feeding.domain.agent.input.feature;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.AlertedParty;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.MatchedParty;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.WlName;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.WlNameType;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.name.v1.*;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NameFeatureFactory implements FeatureFactory {

  @Override
  public Feature create(Alert alert, Match match) {
    Set<String> apNames = getApNameValues(alert.alertedParty(), match);
    Set<WlName> wlNames = getWlNameValues(alert.alertedParty(), match.matchedParty());

    return NameFeatureInputOut.builder()
        .feature(getFeatureName())
        .alertedPartyNames(createAlertedPartyNames(apNames))
        .watchlistNames(createWatchlistNames(wlNames))
        .alertedPartyType(determineApType(match.matchedParty().apType()))
        .matchingTexts(match.details().getMatchingTexts().stream().toList())
        .build();
  }

  private static List<AlertedPartyNameOut> createAlertedPartyNames(Set<String> apNames) {
    return apNames.stream()
        .map(apName -> AlertedPartyNameOut.builder()
            .name(apName)
            .build())
        .toList();
  }

  private static List<WatchlistNameOut> createWatchlistNames(Set<WlName> wlNames) {
    return wlNames.stream()
        .map(wlName -> WatchlistNameOut.builder()
            .name(wlName.getName())
            .type(mapToNameTypeOut(wlName.getType()))
            .build())
        .toList();
  }

  private static NameTypeOut mapToNameTypeOut(WlNameType wlNameType) {
    if (wlNameType == WlNameType.ALIAS) {
      return NameTypeOut.ALIAS;
    }
    return NameTypeOut.REGULAR;
  }

  private static EntityTypeOut determineApType(String apType) {
    if (apType.equals("I")) {
      return EntityTypeOut.INDIVIDUAL;
    } else if (apType.equals("C")) {
      return EntityTypeOut.ORGANIZATION;
    } else {
      return EntityTypeOut.ENTITY_TYPE_UNSPECIFIED;
    }
  }

  private static Set<String> getApNameValues(AlertedParty alertedParty, Match match) {
    Collection<String> matchedNames = match.details().getMatchedApNames();
    List<String> apValues = new ArrayList<>();
    if (!matchedNames.isEmpty()) {
      apValues.addAll(matchedNames);
    } else {
      apValues.add(alertedParty.apName());
      apValues.removeAll(alertedParty.apNameSynonyms());
      apValues.addAll(alertedParty.apNameSynonyms());
    }
    var originalCnNames = alertedParty.apOriginalCnNames();
    if (!originalCnNames.isEmpty() && !match.matchedParty().wlOriginalCnNames().isEmpty()) {
      apValues.addAll(originalCnNames);
    }
    return apValues
        .stream()
        .filter(StringUtils::isNotEmpty)
        .map(StringUtils::normalizeSpace)
        .collect(Collectors.toSet());
  }

  private static Set<WlName> getWlNameValues(AlertedParty alertedParty, MatchedParty matchedParty) {
    var wlNames = matchedParty
        .wlNames()
        .stream()
        .map(name -> new WlName(name.name(), WlNameType.valueOf(name.type())))
        .collect(Collectors.toSet());
    if (!alertedParty.apOriginalCnNames().isEmpty()) {
      matchedParty.wlOriginalCnNames().stream()
          .map(name -> new WlName(name.name(), WlNameType.valueOf(name.type())))
          .forEach(wlNames::add);
    }
    return wlNames;
  }

  protected String getFeatureName() {
    return "features/name";
  }
}
