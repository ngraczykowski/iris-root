package com.silenteight.hsbc.datasource.extractors.document;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.feature.document.PassportNumberDocumentQuery;

import java.util.stream.Stream;

@RequiredArgsConstructor
class PassportNumberDocumentQueryFacade implements PassportNumberDocumentQuery {

  private final MatchData matchData;
  private static final DocumentExtractor DOCUMENT_EXTRACTOR = new DocumentExtractor();

  @Override
  public Stream<String> apPassportNumbers() {
    return extractApDocuments().getPassportNumbers().stream();
  }

  @Override
  public Stream<String> mpPassportNumbers() {
    return extractMpDocuments().getPassportNumbers().stream();
  }

  private Document extractApDocuments() {
    return DOCUMENT_EXTRACTOR.convertAlertedPartyDocumentNumbers(matchData.getCustomerIndividuals());
  }

  private Document extractMpDocuments() {
    return DOCUMENT_EXTRACTOR.convertMatchedPartyDocumentNumbers(matchData);
  }
}
