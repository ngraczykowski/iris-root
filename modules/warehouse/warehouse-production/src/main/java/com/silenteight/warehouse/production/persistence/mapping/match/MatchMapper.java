package com.silenteight.warehouse.production.persistence.mapping.match;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v2.Match;
import com.silenteight.warehouse.production.persistence.common.PayloadConverter;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class MatchMapper {

  @NonNull
  private final PayloadConverter payloadConverter;

  public List<MatchDefinition> toMatchDefinitions(List<Match> matches) {
    return matches.stream().map(this::toMatchDefinition).collect(toList());
  }

  private MatchDefinition toMatchDefinition(Match match) {
    return MatchDefinition.builder()
        .name(match.getName())
        .matchId(match.getDiscriminator())
        .payload(payloadConverter.convertPayload(match.getPayload()))
        .build();
  }
}
