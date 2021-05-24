package com.silenteight.hsbc.datasource.extractors.document;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.feature.nationaliddocument.NationalIdDocumentQuery;

import java.util.stream.Stream;

@RequiredArgsConstructor
class NationalIdDocumentQueryFacade implements NationalIdDocumentQuery {

  private final MatchData matchData;
  private static final DocumentExtractor documentExtractor = new DocumentExtractor();

  @Override
  public Stream<String> apNationalIds() {
    return extractApDocuments().getNationalIds().stream();
  }

  @Override
  public Stream<String> mpNationalIds() {
    return extractMpDocuments().getNationalIds().stream();
  }

  private Document extractApDocuments() {
    return documentExtractor.convertAlertedPartyDocumentNumbers(matchData.getCustomerIndividual());
  }

  private Document extractMpDocuments() {
    return documentExtractor.convertMatchedPartyDocumentNumbers(matchData);
  }
}
