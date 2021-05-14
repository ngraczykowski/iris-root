package com.silenteight.hsbc.datasource.feature.nationaliddocument;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

import java.util.stream.Stream;

public interface NationalIdDocumentQuery {

  Stream<String> apNationalIds();

  Stream<String> mpNationalIds();

  interface Factory {

    NationalIdDocumentQuery create(MatchData matchData);
  }
}
