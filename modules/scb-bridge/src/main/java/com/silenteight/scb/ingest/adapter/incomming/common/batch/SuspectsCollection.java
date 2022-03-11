package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper.GnsMatchCalculator;
import com.silenteight.scb.ingest.adapter.incomming.common.gender.GenderDetector;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchDetails;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchedParty;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

import static java.util.Comparator.comparing;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@RequiredArgsConstructor
public class SuspectsCollection {

  private final Collection<Suspect> suspects;
  private GenderDetector genderDetector = new GenderDetector();

  @Nonnull
  List<Match> makeMatches(@NonNull AlertContext alertContext) {
    List<Suspect> sorted = suspects.stream()
        .sorted(comparing(SuspectsCollection::getSuspectIndex))
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
    MatchedParty party = suspect.makeWatchlistParty(alertContext.getTypeOfRec(), genderDetector);
    MatchDetails details = suspect.makeMatchDetails(
        suspect.getMergedMatchingTexts(),
        alertContext.getPartyName(),
        alertContext.getPartyAlternateNames());

    return Match
        .builder()
        .id(party.id().toBuilder().id(UUID.randomUUID()).build())
        .matchedParty(party)
        .flags(calculateFlags(suspect, alertContext))
        .index(index)
        .details(details)
        .build();
  }

  private static int calculateFlags(Suspect suspect, AlertContext alertContext) {
    GnsMatchCalculator gnsMatchCalculator = new GnsMatchCalculator(
        suspect, alertContext.getLastDecBatchId(), alertContext.isLastDecisionPresent());
    return gnsMatchCalculator.calculateFlags();
  }

  @Nonnull
  public Stream<Suspect> streamAsSuspects() {
    return suspects.stream();
  }

  boolean hasSuspects() {
    return !suspects.isEmpty();
  }

  public int size() {
    return suspects.size();
  }

  static class MissingSuspectIndexException extends IllegalStateException {

    private static final long serialVersionUID = 7253807810633210445L;

    MissingSuspectIndexException(Suspect s) {
      super(String.format("Suspect does not have index, ofacId: %s", s.getOfacId()));
    }
  }
}
