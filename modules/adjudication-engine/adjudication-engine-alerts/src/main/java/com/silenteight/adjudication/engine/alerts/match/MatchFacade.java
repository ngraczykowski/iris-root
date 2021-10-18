package com.silenteight.adjudication.engine.alerts.match;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Match;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
@Service
@Slf4j
public class MatchFacade {

  @NonNull
  private final CreateMatchesUseCase createMatchesUseCase;

  @Nonnull
  public List<Match> createMatches(@NonNull Iterable<NewAlertMatches> matchRequests) {
    var newMatches = createMatchesUseCase.createMatches(matchRequests);

    log.info(
        "Created new matches: count={}, names={}", newMatches.size(),
        newMatches.stream().map(Match::getName).collect(Collectors.joining(", ")));

    return newMatches;
  }
}
