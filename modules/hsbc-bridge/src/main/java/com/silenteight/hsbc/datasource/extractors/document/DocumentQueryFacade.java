package com.silenteight.hsbc.datasource.extractors.document;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.datamodel.PrivateListEntity;
import com.silenteight.hsbc.datasource.feature.document.DocumentQuery;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class DocumentQueryFacade implements DocumentQuery {

  private final MatchData matchData;
  private static final DocumentExtractor documentExtractor = new DocumentExtractor();

  @Override
  public Stream<String> apDocumentNumbers() {
    return documentExtractor
        .convertAlertedPartyDocumentNumbers(matchData.getCustomerIndividual())
        .getAllDocumentsNumbers()
        .stream();
  }

  @Override
  public Stream<String> mpDocumentNumbers() {
    return documentExtractor
        .convertMatchedPartyDocumentNumbers(matchData)
        .getAllDocumentsNumbers()
        .stream();
  }

  @Override
  public Stream<String> mpEdqTaxNumber() {
    return matchData.getPrivateListEntities().stream()
        .map(PrivateListEntity::getEdqTaxNumber);
  }
}
