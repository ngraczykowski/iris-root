package com.silenteight.customerbridge.cbs.alertmapper;

import lombok.NonNull;

import com.silenteight.customerbridge.common.gender.GenderDetector;
import com.silenteight.customerbridge.common.hitdetails.model.Suspect;
import com.silenteight.proto.serp.scb.v1.ScbMatchDetails;
import com.silenteight.proto.serp.v1.alert.Match;
import com.silenteight.proto.serp.v1.alert.Party;
import com.silenteight.protocol.utils.Uuids;
import com.silenteight.sep.base.common.protocol.AnyUtils;

import java.util.Collection;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

class MatchCollector {

  private final GenderDetector genderDetector = new GenderDetector();

  List<Match> collectMatches(
      @NonNull Collection<Suspect> suspects,
      AlertContext alertContext) {
    List<Suspect> sorted = suspects.stream()
        .sorted(comparing(MatchCollector::getSuspectIndex))
        .collect(toList());

    return range(0, sorted.size())
        .mapToObj(i -> makeMatch(i, sorted.get(i), alertContext))
        .collect(toList());
  }

  private static int getSuspectIndex(Suspect suspect) {
    return ofNullable(suspect.getIndex())
        .orElseThrow(() -> new MissingSuspectIndexException(suspect));
  }

  private Match makeMatch(int index, Suspect suspect, AlertContext alertContext) {
    Party party = suspect.makeWatchlistParty(alertContext.getTypeOfRec(), genderDetector);
    ScbMatchDetails details = suspect.makeMatchDetails(
        suspect.getMergedMatchingTexts(),
        alertContext.getPartyName(),
        alertContext.getPartyAlternateNames());

    return Match
        .newBuilder()
        .setId(party.getId().toBuilder().setId(Uuids.random()))
        .setMatchedParty(party)
        .setFlags(calculateFlags(suspect, alertContext))
        .setIndex(index)
        .setDetails(AnyUtils.pack(details))
        .build();
  }

  private static int calculateFlags(Suspect suspect, AlertContext alertContext) {
    GnsMatchCalculator gnsMatchCalculator = new GnsMatchCalculator(
        suspect,
        alertContext.getLastDecBatchId(),
        alertContext.isLastDecisionPresent());
    return gnsMatchCalculator.calculateFlags();
  }

  static class MissingSuspectIndexException extends IllegalStateException {

    private static final long serialVersionUID = 7253807810633210445L;

    MissingSuspectIndexException(Suspect s) {
      super(String.format("Suspect does not have index, ofacId: %s", s.getOfacId()));
    }
  }
}
