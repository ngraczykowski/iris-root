package com.silenteight.hsbc.datasource.feature.nationaliddocument;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

import java.util.stream.Stream;

public interface NationalIdDocumentQuery {

  Stream<String> apNationalIds();

  Stream<String> mpNationalIds();

  Stream<String> apCountries();

  Stream<String> mpCountries();

  interface Factory {

    NationalIdDocumentQuery create(MatchData matchData);
  }
}
