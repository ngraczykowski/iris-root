package com.silenteight.hsbc.datasource.feature.otherdocument;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

import java.util.stream.Stream;

public interface OtherDocumentQuery {

  Stream<String> apOtherDocuments();

  Stream<String> mpOtherDocuments();

  Stream<String> mpEdqTaxNumber();

  interface Factory {

    OtherDocumentQuery create(MatchData matchData);
  }
}
