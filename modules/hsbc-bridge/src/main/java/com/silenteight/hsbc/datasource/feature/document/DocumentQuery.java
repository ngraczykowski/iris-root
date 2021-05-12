package com.silenteight.hsbc.datasource.feature.document;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

import java.util.stream.Stream;

public interface DocumentQuery {

  Stream<String> apDocumentNumbers();

  Stream<String> mpDocumentNumbers();

  Stream<String> mpEdqTaxNumber();

  interface Factory {

    DocumentQuery create(MatchData matchData);
  }
}
