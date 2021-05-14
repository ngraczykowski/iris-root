package com.silenteight.hsbc.datasource.extractors.document;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.datamodel.PrivateListEntity;
import com.silenteight.hsbc.datasource.feature.otherdocument.OtherDocumentQuery;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class OtherDocumentQueryFacade implements OtherDocumentQuery {

  private final MatchData matchData;
  private static final DocumentExtractor documentExtractor = new DocumentExtractor();

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
    return documentExtractor.convertAlertedPartyDocumentNumbers(matchData.getCustomerIndividual());
  }

  private Document extractMpDocuments() {
    return documentExtractor.convertMatchedPartyDocumentNumbers(matchData);
  }
}
