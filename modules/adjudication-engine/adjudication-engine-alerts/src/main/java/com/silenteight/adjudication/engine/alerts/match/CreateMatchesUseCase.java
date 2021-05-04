package com.silenteight.adjudication.engine.alerts.match;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Match;
import com.silenteight.adjudication.engine.alerts.match.MatchRepository.SortIndexOnly;
import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toUnmodifiableList;

@RequiredArgsConstructor
@Service
class CreateMatchesUseCase {

  @NonNull
  private final MatchRepository repository;

  @Transactional
  List<Match> createMatches(Iterable<NewAlertMatches> newAlertMatches) {
    return StreamSupport
        .stream(newAlertMatches.spliterator(), false)
        .flatMap(am -> createAlertMatches(am.getParentAlert(), am.getMatches()))
        .collect(toUnmodifiableList());
  }

  private Stream<Match> createAlertMatches(String parentAlert, List<Match> matches) {
    validateMatchIndexes(matches);

    var alertId = ResourceName.create(parentAlert).getLong("alerts");
    var lastSortIndex = repository
        .findFirstByAlertIdOrderBySortIndexDesc(alertId)
        .map(SortIndexOnly::getSortIndex)
        .orElse(0);

    return IntStream.range(0, matches.size())
        .mapToObj(i -> {
          var match = matches.get(i);
          var sortIndex = match.getIndex() != 0 ? match.getIndex() : lastSortIndex + i + 1;
          return createEntity(alertId, match, sortIndex);
        })
        .map(repository::save)
        .map(MatchEntity::toMatch);
  }

  private void validateMatchIndexes(List<Match> matches) {
    Set<Integer> uniqueIndex = new TreeSet<>();

    var firstDuplicate = matches
        .stream()
        .mapToInt(Match::getIndex)
        .filter(idx -> idx != 0)
        .filter(idx -> !uniqueIndex.add(idx))
        .findAny();

    if (firstDuplicate.isPresent()) {
      throw new IllegalArgumentException("Index " + firstDuplicate.getAsInt() + " is duplicated.");
    }
  }

  private static MatchEntity createEntity(long alertId, Match match, int sortIndex) {
    return MatchEntity.builder()
        .alertId(alertId)
        .clientMatchIdentifier(match.getMatchId())
        .labels(match.getLabelsMap())
        .sortIndex(sortIndex)
        .build();
  }
}
