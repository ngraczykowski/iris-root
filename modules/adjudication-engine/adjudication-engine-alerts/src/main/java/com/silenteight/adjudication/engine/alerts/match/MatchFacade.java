package com.silenteight.adjudication.engine.alerts.match;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Match;

import org.springframework.stereotype.Service;

import java.util.List;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
@Service
public class MatchFacade {

  @NonNull
  private final CreateMatchesUseCase createMatchesUseCase;

  @Nonnull
  public List<Match> createMatches(@NonNull Iterable<NewAlertMatches> matchRequests) {
    return createMatchesUseCase.createMatches(matchRequests);
  }
}
