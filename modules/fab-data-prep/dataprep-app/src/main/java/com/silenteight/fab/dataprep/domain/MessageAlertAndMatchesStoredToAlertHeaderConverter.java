package com.silenteight.fab.dataprep.domain;

import com.silenteight.proto.fab.api.v1.AlertHeader;
import com.silenteight.proto.fab.api.v1.Match;
import com.silenteight.proto.fab.api.v1.MessageAlertAndMatchesStored;
import com.silenteight.proto.fab.api.v1.StoredMatch;

import org.springframework.core.convert.converter.Converter;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class MessageAlertAndMatchesStoredToAlertHeaderConverter
    implements Converter<MessageAlertAndMatchesStored, AlertHeader> {

  private final StoredMatchToMatchConverter storedMatchToMatchConverter =
      new StoredMatchToMatchConverter();

  @Override
  public AlertHeader convert(MessageAlertAndMatchesStored source) {
    List<Match> matches = source.getStoredMatchesList().stream()
        .map(storedMatchToMatchConverter::convert)
        .collect(toList());

    return AlertHeader
        .newBuilder()
        .setAlertName(source.getAlertName())
        .addAllMatches(matches)
        .build();
  }

  private static class StoredMatchToMatchConverter implements Converter<StoredMatch, Match> {

    @Override
    public Match convert(StoredMatch source) {
      return Match
          .newBuilder()
          .setMatchId(source.getMatchId())
          .setMatchName(source.getMatchName())
          .build();
    }
  }
}
