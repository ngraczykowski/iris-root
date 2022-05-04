package com.silenteight.adjudication.engine.alerts.match;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Match;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

import static com.google.common.collect.Lists.partition;
import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.StreamSupport.stream;

@RequiredArgsConstructor
@Service
class CreateMatchesUseCase {

  //TODO(iwnek) make it configurable and testable
  private static final int FETCH_LATEST_INDEXES_PARTITION_SIZE = 1_024;
  private static final int SAVE_PARTITION_SIZE = 1_024;

  @NonNull
  private final MatchRepository repository;

  @Timed(value = "ae.alerts.use_cases", extraTags = { "package", "match" })
  @Transactional
  List<MatchEntity> createMatches(Iterable<NewAlertMatches> newAlertMatches) {

    var matchesToSave = getStream(newAlertMatches)
        .flatMap(CreateMatchesUseCase::createAlertMatches)
        .collect(toUnmodifiableList());

    return saveAllMatches(matchesToSave);
  }

  private static Stream<MatchEntity> createAlertMatches(NewAlertMatches newAlertMatches) {

    var alertId = getAlertId(newAlertMatches.getParentAlert());
    var matches = newAlertMatches.getMatches();

    validateMatchIndexes(matches);

    return matches.stream().map(item -> createEntity(alertId, item));
  }

  private static void validateMatchIndexes(List<Match> matches) {
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

  private static MatchEntity createEntity(long alertId, Match match) {
    return MatchEntity.builder()
        .alertId(alertId)
        .clientMatchIdentifier(match.getMatchId())
        .labels(match.getLabelsMap())
        .sortIndex(0)
        .build();
  }

  @Nonnull
  private List<MatchEntity> saveAllMatches(List<MatchEntity> matchesToSave) {
    return partition(matchesToSave, SAVE_PARTITION_SIZE)
        .stream()
        .flatMap(entities -> getStream(repository.saveAll(entities)))
        .collect(toUnmodifiableList());
  }

  @Nonnull
  private static <T> Stream<T> getStream(Iterable<T> m) {
    return stream(m.spliterator(), false);
  }

  private static long getAlertId(String parentAlert) {
    return ResourceName.create(parentAlert).getLong("alerts");
  }
}
