package com.silenteight.hsbc.datasource.feature.passportnumberdocument;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

import java.util.stream.Stream;

public interface PassportNumberDocumentQuery {

  Stream<String> apPassportNumbers();

  Stream<String> mpPassportNumbers();

  interface Factory {

    PassportNumberDocumentQuery create(MatchData matchData);
  }
}
