package com.silenteight.scb.feeding.domain.agentinput;

import com.silenteight.scb.ingest.adapter.incomming.common.WlName;
import com.silenteight.scb.ingest.adapter.incomming.common.WlNameType;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertedParty;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchedParty;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.name.v1.*;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NameAgentInputCreator implements AgentInput {

  @Override
  public AgentInputIn<Feature> createAgentInput(Alert alert, Match match) {
    Set<String> apNames = getApNameValues(alert.alertedParty(), match.details().matchedApNames());
    Set<WlName> wlNames = getWlNameValues(match.matchedParty());

    return AgentInputIn.builder()
        .alert(alert.details().alertName())
        .match(match.details().matchName())
        .featureInputs(List.of(
            NameFeatureInputOut.builder()
                .feature("features/name")
                .alertedPartyNames(createAlertedPartyNames(apNames))
                .watchlistNames(createWatchlistNames(wlNames))
                .alertedPartyType(determineApType(match.matchedParty().apType()))
                .matchingTexts(match.details().matchingTexts().stream().toList())
                .build()
        ))
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
        .collect(Collectors.toList());
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

  private static Set<String> getApNameValues(
      AlertedParty alertedParty, Collection<String> matchedNames) {
    List<String> apValues = new ArrayList<>();

    if (!matchedNames.isEmpty()) {
      apValues.addAll(matchedNames);
    } else {
      apValues.add(alertedParty.apName());
      apValues.removeAll(alertedParty.apNameSynonyms());
      apValues.addAll(alertedParty.apNameSynonyms());
    }

    return apValues
        .stream()
        .map(StringUtils::normalizeSpace)
        .collect(Collectors.toSet());
  }

  private static Set<WlName> getWlNameValues(MatchedParty matchedParty) {
    return matchedParty
        .wlNames()
        .stream()
        .map(name -> new WlName(name.name(), WlNameType.valueOf(name.type())))
        .collect(Collectors.toSet());
  }
}
