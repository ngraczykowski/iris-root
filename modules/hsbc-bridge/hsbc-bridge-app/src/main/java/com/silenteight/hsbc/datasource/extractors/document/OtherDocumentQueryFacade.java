package com.silenteight.hsbc.datasource.extractors.document;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.datamodel.PrivateListEntity;
import com.silenteight.hsbc.datasource.feature.document.OtherDocumentQuery;

import java.util.stream.Stream;

@RequiredArgsConstructor
class OtherDocumentQueryFacade implements OtherDocumentQuery {

  private final MatchData matchData;
  private static final DocumentExtractor DOCUMENT_EXTRACTOR = new DocumentExtractor();

  @Override
  public Stream<String> apOtherDocuments() {
    return extractApDocuments().getOtherDocuments().stream();
  }

  @Override
  public Stream<String> mpOtherDocuments() {
    return extractMpDocuments().getOtherDocuments().stream();
  }

  @Override
  public Stream<String> mpEdqTaxNumber() {
    return matchData.getPrivateListEntities().stream()
        .map(PrivateListEntity::getEdqTaxNumber);
  }

  private Document extractApDocuments() {
    return DOCUMENT_EXTRACTOR.convertAlertedPartyDocumentNumbers(matchData.getCustomerIndividuals());
  }

  private Document extractMpDocuments() {
    return DOCUMENT_EXTRACTOR.convertMatchedPartyDocumentNumbers(matchData);
  }
}
