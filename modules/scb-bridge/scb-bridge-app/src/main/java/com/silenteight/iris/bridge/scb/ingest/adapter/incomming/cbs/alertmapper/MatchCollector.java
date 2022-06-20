/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertmapper;

import lombok.NonNull;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gender.GenderDetector;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.MatchDetails;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.MatchedParty;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static java.util.Comparator.comparing;
import static java.util.Optional.ofNullable;
import static java.util.stream.IntStream.range;

class MatchCollector {

  private final GenderDetector genderDetector = new GenderDetector();

  List<Match> collectMatches(
      @NonNull Collection<Suspect> suspects,
      AlertContext alertContext) {
    List<Suspect> sorted = suspects.stream()
        .sorted(comparing(MatchCollector::getSuspectIndex))
        .toList();

    return range(0, sorted.size())
        .mapToObj(i -> makeMatch(i, sorted.get(i), alertContext))
        .toList();
  }

  private static int getSuspectIndex(Suspect suspect) {
    return ofNullable(suspect.getIndex())
        .orElseThrow(() -> new MissingSuspectIndexException(suspect));
  }

  private Match makeMatch(int index, Suspect suspect, AlertContext alertContext) {
    MatchedParty party = suspect.makeWatchlistParty(alertContext.getTypeOfRec(), genderDetector);
    MatchDetails details = suspect.makeMatchDetails(
        suspect.getMergedMatchingTexts(),
        alertContext.getPartyName(),
        alertContext.getPartyAlternateNames());

    return Match
        .builder()
        .id(party.id().toBuilder().id(UUID.randomUUID()).build())
        .matchedParty(party)
        .index(index)
        .details(details)
        .build();
  }

  static class MissingSuspectIndexException extends IllegalStateException {

    private static final long serialVersionUID = 7253807810633210445L;

    MissingSuspectIndexException(Suspect s) {
      super(String.format("Suspect does not have index, ofacId: %s", s.getOfacId()));
    }
  }
}
